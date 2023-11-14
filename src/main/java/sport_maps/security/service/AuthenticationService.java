package sport_maps.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.CredentialsExpiredException;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public String signup(RegisterRequest registerRequest) {
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

        return generateVerificationToken(user);
    }

    public void verifyToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenDao.findByToken(token);
        if(verificationToken.isEmpty()) {
            throw new CredentialsExpiredException("Provided token does not exist!");
        }
        if(Instant.now().isAfter(verificationToken.get().getExpiryDate())) {
            throw new CredentialsExpiredException("Verification token expired!");
        }
        fetchUserAndEnable(verificationToken.get());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = getAuthentication(loginRequest);
        String token = jwtProvider.generateToken(authenticate);
        User userByEmail = findUserByEmail(loginRequest.email());
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
        return currentUser.map(user -> findUserByEmail(user.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken.refreshToken());
        String token = jwtProvider.generateTokenWithUserEmail(refreshToken.email());
        User userByEmail = findUserByEmail(refreshToken.email());
        return new AuthenticationResponse(token,
                userByEmail.getIdUser(),
                userByEmail.getFirstName(),
                userByEmail.getLastName(),
                String.valueOf(userByEmail.getRole()),
                String.valueOf(ZonedDateTime
                        .ofInstant(Instant.now().plusMillis(
                                        jwtProvider.getJwtExpirationInMillis()),
                                ZoneId.of("EET"))),
                refreshToken.refreshToken());
    }

    public User getUserByEmail(String email) {
        return findUserByEmail(email);
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
        if (user.isEmpty()) {
            throw new CredentialsExpiredException("User not found with email " + email);
        }
        return user.get();
    }

    private Authentication getAuthentication(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(), loginRequest.password()));
        } catch (Exception e) {
            throw new InsufficientAuthenticationException("Credentials are not valid!");
        }
        return authenticate;
    }

    private void validateExistingUser(RegisterRequest registerRequest) {
        String toCheck = registerRequest.email();
        Optional<User> byEmail = userDao.findByEmail(toCheck);
        if(byEmail.isPresent()) {
            throw new CredentialsExpiredException("User with email " + toCheck + " already exists!");
        }
    }

    private void validateNewUser(RegisterRequest registerRequest) {
        if (registerRequest.firstName().isBlank() || isValidUsername(registerRequest.firstName())) {
            throw new IllegalArgumentException("User's first name is not valid");
        }
        if (registerRequest.lastName().isBlank() || isValidUsername(registerRequest.lastName())) {
            throw new IllegalArgumentException("User's last name is not valid");
        }
        if (registerRequest.email().isBlank()) {
            throw new IllegalArgumentException("User's email is not valid");
        }
        if (registerRequest.password().isBlank() || isValidPassword(registerRequest.password())) {
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
