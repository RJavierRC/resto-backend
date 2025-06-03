package mx.edu.resto.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Importación corregida
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "CambiaEstaFraseSecretaSuperLargaParaJWT123!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes()); // Tipo corregido

    public String generateToken(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60 * 60 * 4)))
                .signWith(key)
                .compact();
    }

    public Claims validate(String token) {
        return Jwts.parser()
                .verifyWith(key) // Ahora acepta SecretKey
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}