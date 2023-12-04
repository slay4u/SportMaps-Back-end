package sport_maps.security.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.CredentialsExpiredException;
import sport_maps.mail.EmailService;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.AuthenticationResponse;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RefreshTokenRequest;
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

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
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
        user.setCreated(Instant.now());
        user.setEnabled(false); // once validated, will be set to true
        user.setRole(Role.ADMIN); // only admins for now
        userDao.save(user);
        emailService.sendHtmlEmail(registerRequest.email(), "http://localhost:8090/api/v1/auth/" + generateVerificationToken(user));
    }

    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenDao.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Provided token doesn't exist."));
        User user = verificationToken.getUser();
        if (Instant.now().isAfter(verificationToken.getExpiryDate())) {
            emailService.sendHtmlEmail(user.getEmail(), "http://localhost:8090/api/v1/auth/" + generateVerificationToken(user));
            throw new CredentialsExpiredException("Verification token expired.");
        }
        fetchUserAndEnable(verificationToken);
        verificationTokenDao.deleteById(verificationToken.getId());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = getAuthentication(loginRequest);
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        String token = jwtProvider.generateToken(principal.getUsername());
        User userByEmail = getByEmail(loginRequest.email());
        return new AuthenticationResponse(token,
                userByEmail.getFirstName() + "|" + userByEmail.getLastName(),
                String.valueOf(userByEmail.getRole()),
                refreshTokenService.generateRefreshToken().getToken());
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

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        refreshTokenService.validateToken(refreshToken.refreshToken());
        String token = jwtProvider.generateToken(refreshToken.email());
        User userByEmail = getByEmail(refreshToken.email());
        return new AuthenticationResponse(token,
                userByEmail.getFirstName() + "|" + userByEmail.getLastName(),
                String.valueOf(userByEmail.getRole()),
                refreshToken.refreshToken());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(tokenExpiration));
        verificationTokenDao.save(verificationToken);
        return token;
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User presentUser = getByEmail(email);
        presentUser.setEnabled(true);
        userDao.save(presentUser);
    }

    private User getByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
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
        String toCheck = registerRequest.email();
        Optional<User> byEmail = userDao.findByEmail(toCheck);
        if (byEmail.isPresent()) {
            throw new EntityExistsException("User with that email already exists.");
        }
    }

    private void validateNewUser(RegisterRequest registerRequest) {
        if (registerRequest.firstName().isBlank() || isValidUsername(registerRequest.firstName())) {
            throw new IllegalArgumentException("First name is not valid.");
        }
        if (registerRequest.lastName().isBlank() || isValidUsername(registerRequest.lastName())) {
            throw new IllegalArgumentException("Last name is not valid.");
        }
        if (registerRequest.email().isBlank()) {
            throw new IllegalArgumentException("Email is not valid.");
        }
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
