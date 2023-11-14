package sport_maps.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport_maps.security.domain.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    void deleteByToken(String token);
}
