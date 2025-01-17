package com.example.onboarding.common.jwt;

import com.example.onboarding.common.jwt.dto.response.TokenResponse;
import com.example.onboarding.domain.user.entity.Authorities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TIME = 1000 * 60 * 60 * 24 * 7;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String key){
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public String getUsername(String token) {
        return this.getClaims(token).get("username", String.class);
    }

    public String getUserId(String refreshToken) {
        return this.getClaims(refreshToken).get("sub", String.class);
    }

    public TokenResponse createToken(Long id, String username, Authorities authorities) {
        Date now = new Date();
        String accessToken =  Jwts.builder()
                                    .claim("sub", id.toString())
                                    .claim("username", username)
                                    .claim("authorities", authorities.name())
                                    .setIssuedAt(now)
                                    .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                                    .signWith(secretKey)
                                    .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TIME))
                .signWith(secretKey)
                .compact();

        return TokenResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
