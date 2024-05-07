package sport_maps.security.general;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Service
public class JwtProvider {
    private static final SecretKey key = hmacShaKeyFor(Jwts.SIG.HS512.key().build().toString().getBytes());

    @Value("${time.jwt}")
    private Long jwtExpiration;

    public String generateToken(String email, String authorities, String username) {
        return Jwts.builder().subject(email).signWith(key).issuer(username).issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(jwtExpiration)))
                .claim("role", authorities.replaceAll("[^A-Z]", "")).compact();
    }

    public void validateToken(String jwt) {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
    }

    public String getUserEmailFromJwt(String jwt) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload().getSubject();
    }
}
