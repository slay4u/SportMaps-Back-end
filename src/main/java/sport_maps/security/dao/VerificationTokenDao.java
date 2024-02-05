package sport_maps.security.dao;

import org.springframework.stereotype.Repository;
import sport_maps.security.domain.VerificationToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationTokenDao extends TokenDao<VerificationToken> {
    Optional<VerificationToken> findByToken(String token);
    void deleteAllByExpiryDateLessThan(Instant expiryDate);
}
