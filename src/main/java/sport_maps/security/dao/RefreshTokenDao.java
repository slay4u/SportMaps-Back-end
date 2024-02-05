package sport_maps.security.dao;

import org.springframework.stereotype.Repository;
import sport_maps.security.domain.RefreshToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenDao extends TokenDao<RefreshToken> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByExpiryDateLessThan(Instant expiryDate);
}
