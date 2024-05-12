package sport_maps.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
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
import sport_maps.security.general.JwtProvider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserDao userDao;

    @Mock
    private EmailService emailService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private VerificationTokenDao verificationTokenDao;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService service;

    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "tokenExpiration", 8640000L);
        passwordEncoder = new BCryptPasswordEncoder();
        user = new User();
        user.setRole(Role.ADMIN);
        user.setEnabled(false);
        user.setPassword("12qw34erQ++");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test1@gmail.com");
    }

    @Test
    void signup() {
        RegisterRequest registerRequest = new RegisterRequest("Test", "Test", "test1@gmail.com", "12qw34erQ++");
        when(userDao.save(any(User.class))).thenReturn(user);
        service.signup(registerRequest);
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void verifyToken() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken("some12symbols34");
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(23232344));
        verificationTokenDao.save(verificationToken);
        when(verificationTokenDao.findByToken(any(String.class))).thenReturn(Optional.of(verificationToken));
        when(userDao.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        service.verifyToken(verificationToken.getToken());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest("test1@gmail.com", "12qw34erQ++");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        org.springframework.security.core.userdetails.User principal = mock(org.springframework.security.core.userdetails.User.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getUsername()).thenReturn("test1@gmail.com");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        when(principal.getAuthorities()).thenReturn(authorities);
        when(jwtProvider.generateToken(user.getEmail(), "[ADMIN]", "Test|Test")).thenReturn("5re1ve8r4ver681v8b4t81ve6r1ve68r1v");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("8erv1er8g49erv18er4v9e4v");
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(23232344));
        when(refreshTokenService.generateRefreshToken(any(User.class))).thenReturn(refreshToken.getToken());
        AuthenticationResponse response = service.login(loginRequest);
        assertEquals("5re1ve8r4ver681v8b4t81ve6r1ve68r1v", response.token());
        assertEquals("8erv1er8g49erv18er4v9e4v", response.userInfo().getValue());
    }

    @Test
    void refreshToken() {
        Cookie cookie = new Cookie("user_info", "8erv1er8g49erv18er4v9e4v");
        MockedStatic<WebUtils> mockedStatic = mockStatic(WebUtils.class);
        mockedStatic.when(() -> WebUtils.getCookie(request, "user_info")).thenReturn(cookie);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("8erv1er8g49erv18er4v9e4v");
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(23232344));
        lenient().when(refreshTokenService.validateToken(cookie.getValue())).thenReturn(refreshToken);
        lenient().when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(jwtProvider.generateToken(any(String.class), any(String.class), any(String.class))).thenReturn("5re1ve8r4ver681v8b4t81ve6r1ve68r1v");
        String refreshed = service.refreshToken(request);
        assertEquals("5re1ve8r4ver681v8b4t81ve6r1ve68r1v", refreshed);
    }
}
