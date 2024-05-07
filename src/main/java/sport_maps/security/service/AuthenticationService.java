package sport_maps.security.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.util.WebUtils;
import sport_maps.mail.EmailService;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.RefreshToken;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.AuthenticationResponse;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.security.general.JwtProvider;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final VerificationTokenDao verificationTokenDao;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    @Value("${time.signup}")
    private Long tokenExpiration;

    public AuthenticationService(BCryptPasswordEncoder passwordEncoder, UserDao userDao, VerificationTokenDao verificationTokenDao, JwtProvider jwtProvider,
                                 AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.verificationTokenDao = verificationTokenDao;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    public void signup(RegisterRequest registerRequest) {
        validateNewUser(registerRequest);
        validateExistingUser(registerRequest);
        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEnabled(false);
        user.setRole(Role.ADMIN); // only admins for now
        userDao.save(user);
        emailService.sendHtmlEmail(registerRequest.email(), "http://localhost:3000/verification/" + generateVerificationToken(user));
    }

    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenDao.findByToken(token).orElseThrow(EntityNotFoundException::new);
        User user = verificationToken.getUser();
        if (Instant.now().isAfter(verificationToken.getExpiryDate())) {
            emailService.sendHtmlEmail(user.getEmail(), "http://localhost:3000/verification/" + generateVerificationToken(user));
            throw new CredentialsExpiredException("Verification token expired.");
        }
        fetchUserAndEnable(verificationToken);
        verificationTokenDao.deleteById(verificationToken.getId());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = getAuthentication(loginRequest);
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        User userByEmail = getByEmail(loginRequest.email());
        String token = jwtProvider.generateToken(principal.getUsername(), principal.getAuthorities().toString(),
                userByEmail.getFirstName() + "|" + userByEmail.getLastName());
        return new AuthenticationResponse(token, ResponseCookie.from("user_info", refreshTokenService.generateRefreshToken(userByEmail))
                .httpOnly(true).secure(true).maxAge(7 * 24 * 60 * 60).path("/").build());
    }

    private Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal();
        return Optional.of(principal);
    }

    public Optional<User> getDomainUser() {
        Optional<org.springframework.security.core.userdetails.User> currentUser = getCurrentUser();
        return currentUser.map(user -> getByEmail(user.getUsername()));
    }

    public String refreshToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "user_info");
        if (cookie != null && !cookie.getValue().isEmpty()) {
            RefreshToken refreshToken = refreshTokenService.validateToken(cookie.getValue());
            User userByEmail = getByEmail(refreshToken.getUser().getEmail());
            return jwtProvider.generateToken(userByEmail.getEmail(), userByEmail.getRole().toString(),
                    userByEmail.getFirstName() + "|" + userByEmail.getLastName());
        }
        return null;
    }

    private String generateVerificationToken(User user) {
        String token = generateNewToken();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(tokenExpiration));
        verificationTokenDao.save(verificationToken);
        return token;
    }

    private String generateNewToken() {
        SecureRandom random = new SecureRandom();
        Base64.Encoder encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[64];
        random.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes).replaceAll("[^A-Za-z0-9]", "");
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User presentUser = getByEmail(email);
        presentUser.setEnabled(true);
        userDao.save(presentUser);
    }

    private User getByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    private Authentication getAuthentication(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(), loginRequest.password()));
        } catch (Exception e) {
            throw new InsufficientAuthenticationException("Credentials are not valid.");
        }
        return authenticate;
    }

    private void validateExistingUser(RegisterRequest registerRequest) {
        if (userDao.existsByEmail(registerRequest.email())) throw new EntityExistsException("User with that email already exists.");
    }

    private void validateNewUser(RegisterRequest registerRequest) {
        if (registerRequest.firstName().isBlank() || isValidUsername(registerRequest.firstName())) {
            throw new IllegalArgumentException("First name is not valid.");
        }
        if (registerRequest.lastName().isBlank() || isValidUsername(registerRequest.lastName())) {
            throw new IllegalArgumentException("Last name is not valid.");
        }
        if (registerRequest.email().isBlank()) throw new IllegalArgumentException("Email is not valid.");
        if (registerRequest.password().isBlank() || isValidPassword(registerRequest.password())) {
            throw new IllegalArgumentException("Password is not valid.");
        }
    }

    private boolean isValidUsername(String name) {
        String regex = "^(?=.{2,30}$)[A-Z][a-zA-Z]*(?:\\h+[A-Z][a-zA-Z]*)*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return !m.matches();
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[{}:#@!;\\[_'`\\],\".\\/~?*\\-$^+=\\\\<>]).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return !m.matches();
    }
}
