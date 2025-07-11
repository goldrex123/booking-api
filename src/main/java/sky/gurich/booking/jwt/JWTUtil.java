package sky.gurich.booking.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTUtil {

    private SecretKey secretKey;
    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${spring.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String userId, Map<String,Object> claims) {
        Map<String, Object> accessTokenClaims = new HashMap<>(claims != null ? claims : Map.of());
        accessTokenClaims.put("tokenType", "access");
        return buildToken(userId, accessTokenExpiration, accessTokenClaims);
    }

    public String generateRefreshToken(String userId) {
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put("tokenType", "refresh");
        return buildToken(userId, refreshTokenExpiration, accessTokenClaims);
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String getTokenType(String token) {
        return String.valueOf(Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("tokenType"));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다 - {}", e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 JWT 토큰입니다.");
        } catch (JwtException | IllegalArgumentException e) {
            log.error("잘못된 jwt 토큰 값 입니다 - {}", e.getMessage());
            throw new JwtException("잘못된 JWT 토큰입니다.");
        }
    }

    private String buildToken(String userId, long expirationMs, Map<String,Object> claims) {
        return Jwts.builder()
                .subject(userId)
                .claims(claims != null ? claims : Map.of())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }
}
