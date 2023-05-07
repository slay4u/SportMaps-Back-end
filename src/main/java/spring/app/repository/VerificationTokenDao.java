package spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.domain.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenDao extends JpaRepository<VerificationToken, Long> {

    @Query(value = "SELECT * FROM public.token" +
            " WHERE token = ?1", nativeQuery = true)
    Optional<VerificationToken> findByToken(String token);
}
