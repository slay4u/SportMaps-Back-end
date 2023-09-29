package spring.app.modules.security.service;

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
import spring.app.modules.commons.exception.AuthenticationException;
import spring.app.modules.commons.validation.PersonDtoValidator;
import spring.app.modules.security.dao.UserDao;
import spring.app.modules.security.dao.VerificationTokenDao;
import spring.app.modules.security.domain.VerificationToken;
import spring.app.modules.security.general.JwtProvider;
import spring.app.modules.security.dto.RefreshTokenRequest;
import spring.app.modules.security.domain.Role;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.dto.AuthenticationResponse;
import spring.app.modules.security.dto.LoginRequest;
import spring.app.modules.security.dto.RegisterRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

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
    private final PersonDtoValidator personValidator;

    @Value("${signup.token.time}")
    private Long tokenExpiration;

    public String signup(RegisterRequest registerRequest) {
        //personValidator.validateNewUser(registerRequest);
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false); // once validated, will be set to true
        user.setRole(Role.ADMIN); // only admins for now

        userDao.save(user);

        return generateVerificationToken(user);
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
        if (currentUser.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(findUserByEmail(currentUser.get().getUsername()));
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
}
