package sport_maps.security.service;

import jakarta.persistence.EntityNotFoundException;
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

    public RefreshTokenService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenDao.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenDao.findRefreshTokenByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid refresh token!"));
    }

    public void deleteRefreshToken(String token) {
        SecurityContextHolder.clearContext();
        refreshTokenDao.deleteByToken(token);
    }
}
