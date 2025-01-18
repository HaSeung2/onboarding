package com.example.onboarding.jwt;

import com.example.onboarding.common.jwt.JwtUtil;
import com.example.onboarding.common.jwt.dto.response.TokenResponse;
import com.example.onboarding.domain.user.entity.Authorities;
import com.example.onboarding.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Date;

@SpringBootTest
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.secret.key}")
    private String key;
    private SecretKey secretKey;
    @Value("${jwt.expired.test}")
    private Long expired;
    private User user;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(key);
        user = User.builder()
                .username("dldl")
                .authorities(Authorities.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("jwt 토큰 생성 테스트")
    void createJwtTest(){
        TokenResponse token = jwtUtil.createToken(1L, user.getUsername(), user.getAuthorities());
        assert token != null;
    }

    @Test
    @DisplayName("만료기간 지난 토큰 일 시 false 반환")
    void expiredJwtTest() throws InterruptedException {
        Date now = new Date();
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        String accessToken =  Jwts.builder()
                .claim("sub", 1L)
                .claim("username", user.getUsername())
                .claim("authorities", user.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expired))
                .signWith(secretKey)
                .compact();
       Thread.sleep(1500);
       boolean flag = jwtUtil.validateToken(accessToken);
       assert !flag;
    }

    @Test
    @DisplayName("유효하지 않은 토큰 일 시 false 반환")
    void isValidJwtTest(){
        boolean flag = jwtUtil.validateToken("wqeqwirj.asfnasf.asf");
        assert !flag;
    }

    @Test
    @DisplayName("검증 테스트 성공")
    void sucessToken(){
        TokenResponse token = jwtUtil.createToken(1L, user.getUsername(), user.getAuthorities());
        boolean flag = jwtUtil.validateToken(token.getToken());
        assert flag;
    }
}
