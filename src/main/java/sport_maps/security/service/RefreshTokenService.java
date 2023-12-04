package sport_maps.security.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CredentialsExpiredException;
import sport_maps.security.dao.RefreshTokenDao;
import sport_maps.security.domain.RefreshToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private final RefreshTokenDao refreshTokenDao;

    @Value("${time.refresh}")
    private Long refreshExpirationInMillis;

    public RefreshTokenService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationInMillis));
        return refreshTokenDao.save(refreshToken);
    }

    public void validateToken(String token) {
        RefreshToken refreshToken = refreshTokenDao.findByToken(token).orElseThrow(() -> new EntityNotFoundException("Invalid refresh token."));
        if (Instant.now().isAfter(refreshToken.getExpiryDate())) {
            SecurityContextHolder.clearContext();
            throw new CredentialsExpiredException("Refresh token expired.");
        }
    }

    public void deleteToken(String token) {
        SecurityContextHolder.clearContext();
        refreshTokenDao.deleteByToken(token);
    }
}
