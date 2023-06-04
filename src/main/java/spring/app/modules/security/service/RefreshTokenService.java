package spring.app.modules.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.security.dao.RefreshTokenDao;
import spring.app.modules.security.domain.RefreshToken;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenDao refreshTokenDao;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenDao.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenDao.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid refresh token!"));
    }

    public void deleteRefreshToken(String token) {
        SecurityContextHolder.clearContext();
        refreshTokenDao.deleteByToken(token);
    }
}
