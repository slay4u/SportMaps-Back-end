package sport_maps.security.general;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

@Service
public class JwtProvider {
    private Key key;

    @Value("${time.jwt}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() {
        key = secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String email, String authorities, String username) {
        return Jwts.builder().setSubject(email).signWith(key).setIssuer(username).setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .claim("role", authorities.replaceAll("[^A-Z]", ""))
                .compact();
    }

    public void validateToken(String jwt) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }

    public String getUserEmailFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
    }
}
