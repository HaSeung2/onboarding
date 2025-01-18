package com.example.onboarding.jwt;

import com.example.onboarding.common.jwt.JwtUtil;
import com.example.onboarding.common.jwt.dto.response.TokenResponse;
import com.example.onboarding.domain.user.entity.Authorities;
import com.example.onboarding.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private User user;

    @BeforeEach
    public void setUp(){
        jwtUtil = new JwtUtil(secretKey);
        user = User.builder()
                .username("dldl")
                .authorities(Authorities.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("jwt 토큰 생성 테스트")
    public void createJwtTest(){
        TokenResponse token = jwtUtil.createToken(1L, user.getUsername(), user.getAuthorities());
        assert token != null;
    }
}
