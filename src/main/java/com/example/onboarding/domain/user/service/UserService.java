package com.example.onboarding.domain.user.service;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.common.exception.ErrorCode;
import com.example.onboarding.common.jwt.JwtUtil;
import com.example.onboarding.common.jwt.dto.request.TokenRequest;
import com.example.onboarding.common.jwt.dto.response.TokenResponse;
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
    private final UserRefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity(passwordEncoder.encode(userSignUpRequest.getPassword()));
        if(userRepository.existsByUsername(user.getUsername())){
            throw new CustomException(ErrorCode.DUPLICATION_USER_NAME);
        }
        return new UserSignUpResponse(userRepository.save(user));
    }

    public TokenResponse sign(UserSignRequest signRequest) {
        User user = userRepository.findByUsername(signRequest.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NAME_NOT_MATCH));
        if(!passwordEncoder.matches(signRequest.getPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.USER_PASSWORD_NOT_MATCH);
        }
        return this.createAndSaveToken(user, null);
    }

    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        if(!jwtUtil.validateToken(tokenRequest.getRefreshToken())){
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        Long userId = Long.valueOf(jwtUtil.getUserId(tokenRequest.getToken()));
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_ID_NOT_MATCH));
        return this.createAndSaveToken(user, tokenRequest.getRefreshToken());
    }

    private TokenResponse createAndSaveToken(User user, String requestRefreshToken) {
        TokenResponse tokenResponse = this.createToken(user);
        this.saveRefreshToken(user, requestRefreshToken, tokenResponse);
        return tokenResponse;
    }

    private TokenResponse createToken(User user) {
        return jwtUtil.createToken(user.getId(), user.getUsername(), user.getAuthorities());
    }

    private void saveRefreshToken(User user, String requestRefreshToken, TokenResponse tokenResponse) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserId(user.getId())
                .map(token ->{
                    this.validateRefreshToken(requestRefreshToken, token.getRefreshToken());
                    token.updateRefreshToken(tokenResponse.getRefreshToken());
                    return token;
                })
                .orElseGet(() -> UserRefreshToken.builder()
                        .userId(user.getId())
                        .refreshToken(tokenResponse.getRefreshToken())
                        .build()
                );
        refreshTokenRepository.save(userRefreshToken);
    }

    private void validateRefreshToken(String requestRefreshToken, String oldRefreshToken) {
        if(requestRefreshToken != null && !oldRefreshToken.equals(requestRefreshToken)){
            throw new CustomException(ErrorCode.USED_REFRESH_TOKEN);
        }
    }
}
