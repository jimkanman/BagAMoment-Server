package com.jkm.jimkanman.util;

import com.jkm.jimkanman.security.TokenCategory;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final long expiredMs;
    private final SecretKey secretKey;
    JwtUtil(@Value("${jwt.secret.key}") final String secret,
            @Value("${jwt.access.expire}") final long expire){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        expiredMs = expire * 1000;
    }

    public TokenCategory getTokenCategory(String token) {
        String tokenCategory = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
        return TokenCategory.valueOf(tokenCategory);
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    /** Access Token 발급 */
    public String issueAccessToken(Long id, TokenCategory tokenCatetory) {
        return Jwts.builder()
                .claim("category", tokenCatetory.name())
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /** Access Token 검증 */
    public boolean validateToken(String token) {
        // TODO
        return false;
    }
}
