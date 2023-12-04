package sport_maps.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport_maps.security.domain.VerificationToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationTokenDao extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteAllByExpiryDateLessThan(Instant expiryDate);
}
