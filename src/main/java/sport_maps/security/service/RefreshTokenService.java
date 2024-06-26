package sport_maps.security.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.util.WebUtils;
import sport_maps.commons.service.AbstractService;
import sport_maps.commons.util.mapper.Mapper;
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
public class RefreshTokenService extends AbstractService<RefreshToken, RefreshTokenDao, Mapper> {
    @Value("${time.refresh}")
    private Long refreshExpiration;

    @Override
    @Autowired
    protected void setDao(RefreshTokenDao refreshTokenDao) {
        this.dao = refreshTokenDao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public String generateRefreshToken(User user) {
        String token = generateNewToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        save(refreshToken);
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
            delete(refreshToken.getId());
            return ResponseCookie.from("user_info").httpOnly(true)
                    .secure(true).maxAge(0).path("/").build();
        }
        return null;
    }

    public RefreshToken getRefreshToken(String token) {
        return dao.findByToken(token).orElseThrow(EntityNotFoundException::new);
    }
}
