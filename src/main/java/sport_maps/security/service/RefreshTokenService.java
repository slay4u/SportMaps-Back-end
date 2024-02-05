package sport_maps.security.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.util.WebUtils;
import sport_maps.security.dao.RefreshTokenDao;
import sport_maps.security.domain.RefreshToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.security.domain.User;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private final RefreshTokenDao refreshTokenDao;

    @Value("${time.refresh}")
    private Long refreshExpiration;

    public RefreshTokenService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    public String generateRefreshToken(User user) {
        String token = generateNewToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshTokenDao.save(refreshToken);
        return token;
    }

    private String generateNewToken() {
        return new StringBuilder().append(UUID.randomUUID()).append("-").append(UUID.randomUUID()).reverse()
                .append("-").append(String.valueOf(System.currentTimeMillis()).substring(7)).toString();
    }

    public RefreshToken validateToken(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        if (Instant.now().isAfter(refreshToken.getExpiryDate())) {
            SecurityContextHolder.clearContext();
            throw new CredentialsExpiredException("Refresh token expired.");
        }
        return refreshToken;
    }

    public ResponseCookie deleteToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "user_info");
        if (cookie != null && !cookie.getValue().isEmpty()) {
            RefreshToken refreshToken = getRefreshToken(cookie.getValue());
            SecurityContextHolder.clearContext();
            refreshTokenDao.deleteById(refreshToken.getId());
            return ResponseCookie.from("user_info").httpOnly(true)
                    .secure(true).maxAge(0).path("/").build();
        }
        return null;
    }

    private RefreshToken getRefreshToken(String token) {
        return refreshTokenDao.findByToken(token).orElseThrow(() -> new EntityNotFoundException("Invalid refresh token."));
    }
}
