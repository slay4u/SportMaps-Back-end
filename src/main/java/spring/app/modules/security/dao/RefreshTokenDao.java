package spring.app.modules.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.security.domain.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {

    @Query(value = "SELECT * FROM public.refresh_token" +
            " WHERE token = ?1", nativeQuery = true)
    Optional<RefreshToken> findByToken(String token);

    @Query
    void deleteByToken(String token);
}
