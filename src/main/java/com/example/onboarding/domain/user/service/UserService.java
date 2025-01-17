package com.example.onboarding.domain.user.service;

import com.example.onboarding.common.jwt.JwtUtil;
import com.example.onboarding.common.jwt.dto.TokenResponse;
import com.example.onboarding.domain.user.dto.request.UserSignRequest;
import com.example.onboarding.domain.user.dto.request.UserSignUpRequest;
import com.example.onboarding.domain.user.dto.response.UserSignUpResponse;
import com.example.onboarding.domain.user.entity.User;
import com.example.onboarding.domain.user.entity.UserRefreshToken;
import com.example.onboarding.domain.user.repository.UserRefreshTokenRepository;
import com.example.onboarding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRefreshTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity();
        if(userRepository.existsByUsername(user.getUsername())){
            throw new IllegalArgumentException("사용할 수 없는 계정명입니다.");
        }
        user.passwordEncoder(passwordEncoder.encode(user.getPassword()));
        return new UserSignUpResponse(userRepository.save(user));
    }

    public TokenResponse sign(UserSignRequest signRequest) {
        User user = userRepository.findByUsername(signRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("로그인을 다시 시도해주세요."));
        if(!passwordEncoder.matches(signRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("로그인을 다시 시도해주세요.");
        }
        TokenResponse tokenResponse = jwtUtil.createToken(user.getId(), user.getNickname(), user.getAuthorities());
        UserRefreshToken userRefreshToken = tokenRepository.findByUserId(user.getId())
                        .map(token ->{
                            token.updateRefreshToken(tokenResponse.getRefreshToken());
                            return token;
                        })
                        .orElseGet(() -> UserRefreshToken.builder()
                                .userId(user.getId())
                                .refreshToken(tokenResponse.getRefreshToken())
                                .build()
                        );
        tokenRepository.save(userRefreshToken);
        return tokenResponse;
    }
}
