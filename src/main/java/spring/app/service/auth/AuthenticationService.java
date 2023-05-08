package spring.app.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.domain.VerificationToken;
import spring.app.dto.user.RefreshTokenRequest;
import spring.app.repository.VerificationTokenDao;
import spring.app.security.JwtProvider;
import spring.app.domain.Role;
import spring.app.domain.User;
import spring.app.dto.user.AuthenticationResponse;
import spring.app.dto.user.LoginRequest;
import spring.app.dto.user.RegisterRequest;
import spring.app.exception.AuthenticationException;
import spring.app.repository.UserDao;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final VerificationTokenDao verificationTokenDao;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${signup.token.time}")
    private Long tokenExpiration;

    public void signup(RegisterRequest registerRequest) {
        validateNewUser(registerRequest);
        validateExistingUser(registerRequest);
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false); // once validated, will be set to true
        user.setRole(Role.ADMIN); // only admins for now

        userDao.save(user);

        String token = generateVerificationToken(user);
        System.out.println("http://localhost:8090/sport-maps/v1/auth/accountVerification/" + token);
    }

    public void verifyToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenDao.findByToken(token);
        if(verificationToken.isEmpty()) {
            throw new AuthenticationException("Provided token does not exist!");
        }
        if(Instant.now().isAfter(verificationToken.get().getExpiryDate())) {
            throw new AuthenticationException("Verification token expired!");
        }
        fetchUserAndEnable(verificationToken.get());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = getAuthentication(loginRequest);
        String token = jwtProvider.generateToken(authenticate);
        User userByEmail = findUserByEmail(loginRequest.getEmail());
        return new AuthenticationResponse(token,
                userByEmail.getIdUser(),
                userByEmail.getFirstName(),
                userByEmail.getLastName(),
                String.valueOf(userByEmail.getRole()),
                String.valueOf(ZonedDateTime
                        .ofInstant(Instant.now().plusMillis(
                                        jwtProvider.getJwtExpirationInMillis()),
                                ZoneId.of("EET"))),
                refreshTokenService.generateRefreshToken().getToken());
    }

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(principal);
    }

    // for favourites
    public User getDomainUser() {
        org.springframework.security.core.userdetails.User currentUser =
                getCurrentUser().orElseThrow();
        return findUserByEmail(currentUser.getUsername());
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserEmail(refreshToken.getEmail());
        User userByEmail = findUserByEmail(refreshToken.getEmail());
        return new AuthenticationResponse(token,
                userByEmail.getIdUser(),
                userByEmail.getFirstName(),
                userByEmail.getLastName(),
                String.valueOf(userByEmail.getRole()),
                String.valueOf(ZonedDateTime
                        .ofInstant(Instant.now().plusMillis(
                                        jwtProvider.getJwtExpirationInMillis()),
                                ZoneId.of("EET"))),
                refreshToken.getRefreshToken());
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
        String email = verificationToken.getUser().getUsername();
        User presentUser = findUserByEmail(email);
        presentUser.setEnabled(true);
        userDao.save(presentUser);
    }

    private User findUserByEmail(String email) {
        Optional<User> user = userDao.findByEmail(email);
        if(user.isEmpty()) {
            throw new AuthenticationException("User not found with email " + email);
        }
        return user.get();
    }

    private Authentication getAuthentication(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new InsufficientAuthenticationException("Credentials are not valid!");
        }
        return authenticate;
    }

    private void validateExistingUser(RegisterRequest registerRequest) {
        String toCheck = registerRequest.getEmail();
        Optional<User> byEmail = userDao.findByEmail(toCheck);
        if(byEmail.isPresent()) {
            throw new AuthenticationException("User with email " + toCheck + " already exists!");
        }
    }

    private void validateNewUser(RegisterRequest registerRequest) {
        if (registerRequest.getFirstName().isBlank() || Objects.isNull(registerRequest.getFirstName())
                || isValidUsername(registerRequest.getFirstName())) {
            throw new IllegalArgumentException("User's first name is not valid");
        }
        if (registerRequest.getLastName().isBlank() || Objects.isNull(registerRequest.getLastName())
                || isValidUsername(registerRequest.getLastName())) {
            throw new IllegalArgumentException("User's last name is not valid");
        }
        if (registerRequest.getEmail().isBlank() || Objects.isNull(registerRequest.getEmail())) {
            throw new IllegalArgumentException("User's email is not valid");
        }
        if (registerRequest.getPassword().isBlank() || Objects.isNull(registerRequest.getPassword())
                || isValidPassword(registerRequest.getPassword())) {
            throw new IllegalArgumentException("User's password is not valid");
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
