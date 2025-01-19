package com.example.onboarding.domain.user.service;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.common.exception.ErrorCode;
import com.example.onboarding.common.jwt.JwtUtil;
import com.example.onboarding.common.jwt.dto.request.TokenRequest;
import com.example.onboarding.common.jwt.dto.response.TokenResponse;
import com.example.onboarding.domain.user.dto.request.UserSignRequest;
import com.example.onboarding.domain.user.dto.request.UserSignUpRequest;
import com.example.onboarding.domain.user.dto.response.UserSignUpResponse;
import com.example.onboarding.domain.user.entity.Authorities;
import com.example.onboarding.domain.user.entity.User;
import com.example.onboarding.domain.user.entity.UserRefreshToken;
import com.example.onboarding.domain.user.repository.UserRefreshTokenRepository;
import com.example.onboarding.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRefreshTokenRepository refreshTokenRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccessTest(){
        User user = User.builder()
                .username("test")
                .password("1234")
                .nickname("test")
                .authorities(Authorities.ROLE_USER)
                .build();
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .username("tests")
                .password("1234")
                .nickname("test")
                .build();
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("1234");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserSignUpResponse response = userService.signUp(userSignUpRequest);

        assert response.getUsername().equals(user.getUsername());
    }

    @Test
    @DisplayName("중복 계정명일 경우 예외처리")
    void signUpDuplicationTest(){
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .username("tests")
                .password("1234")
                .nickname("test")
                .build();

        when(userRepository.existsByUsername(any())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class ,() -> userService.signUp(userSignUpRequest));
        assert ErrorCode.DUPLICATION_USER_NAME.getMessage().equals(exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("로그인 성공 시 Access / Refresh 토큰 발급")
    void loginSuccessTest(){
        User user = User.builder()
                .username("test")
                .password("1234")
                .nickname("test")
                .authorities(Authorities.ROLE_USER)
                .build();
        UserSignRequest userSignRequest = UserSignRequest.builder()
                .username("test")
                .password("1234")
                .build();
        TokenResponse tokenResponse = TokenResponse.builder()
                .token("token")
                .refreshToken("refreshToken")
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.createToken(any(), any(), any())).thenReturn(tokenResponse);

        TokenResponse createdToken = userService.sign(userSignRequest);

        assert createdToken.getToken() != null;
        assert createdToken.getRefreshToken() != null;
        assert tokenResponse.getToken().equals(createdToken.getToken());
        assert tokenResponse.getRefreshToken().equals(createdToken.getRefreshToken());
    }

    @Test
    @DisplayName("찾을 수 없는 계정일 경우 예외처리")
    void loginNotFoundUserTest(){
        UserSignRequest userSignRequest = UserSignRequest.builder()
                .username("test")
                .password("1234")
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class ,() -> userService.sign(userSignRequest));
        assert ErrorCode.USER_NAME_NOT_MATCH.getMessage().equals(exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("비밀번호가 틀릴 경우 예외처리")
    void loginNotPasswordTest(){
        UserSignRequest userSignRequest = UserSignRequest.builder()
                .username("test")
                .password("1234")
                .build();

        User user = User.builder()
                .username("test")
                .password("1234")
                .nickname("test")
                .authorities(Authorities.ROLE_USER)
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class ,() -> userService.sign(userSignRequest));
        assert ErrorCode.USER_PASSWORD_NOT_MATCH.getMessage().equals(exception.getErrorCode().getMessage());
    }
    
    @Test
    @DisplayName("RefreshToken을 이용한 Access / RefreshToken 재발급 성공 테스트")
    void refreshTokenSuccessTest(){
        TokenRequest tokenRequest = TokenRequest.builder()
                .token("accessToken")
                .refreshToken("refreshToken")
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .token("retryAccessToken")
                .refreshToken("retryRefreshToken")
                .build();

        User user = User.builder()
                .username("test")
                .password("1234")
                .nickname("test")
                .authorities(Authorities.ROLE_USER)
                .build();

        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(jwtUtil.getUserId(any())).thenReturn("1");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtUtil.createToken(any(), any(), any())).thenReturn(tokenResponse);

        TokenResponse createdToken = userService.refreshToken(tokenRequest);

        assert createdToken.getToken() != null;
        assert createdToken.getRefreshToken() != null;
        assert createdToken.getToken().equals("retryAccessToken");
        assert createdToken.getRefreshToken().equals("retryRefreshToken");
    }

    @Test
    @DisplayName("RefreshToken이 만료되었을 경우 예외처리")
    void refreshTokenExpiredTest(){
        TokenRequest tokenRequest = TokenRequest.builder()
                .token("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(jwtUtil.validateToken(any())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class ,() -> userService.refreshToken(tokenRequest));
        assert ErrorCode.EXPIRED_REFRESH_TOKEN.getMessage().equals(exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("찾을 수 없는 유저일 경우 예외")
    void refreshTokenNotFountUserTest(){
        TokenRequest tokenRequest = TokenRequest.builder()
                .token("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(jwtUtil.getUserId(any())).thenReturn("1");
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class ,() -> userService.refreshToken(tokenRequest));
        assert ErrorCode.USER_ID_NOT_MATCH.getMessage().equals(exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("이미 사용한 RefreshToken인 경우 예외처리")
    void refreshTokenUsedTest(){
        TokenRequest tokenRequest = TokenRequest.builder()
                .token("accessToken")
                .refreshToken("refreshToken")
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .token("retryAccessToken")
                .refreshToken("retryRefreshToken")
                .build();

        UserRefreshToken reTokenResponse = UserRefreshToken.builder()
                .userId(1L)
                .refreshToken("dadaswqe")
                .build();

        User user = User.builder()
                .username("test")
                .password("1234")
                .nickname("test")
                .authorities(Authorities.ROLE_USER)
                .build();

        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(jwtUtil.getUserId(any())).thenReturn("1");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtUtil.createToken(any(), any(), any())).thenReturn(tokenResponse);
        when(refreshTokenRepository.findByUserId(any())).thenReturn(Optional.ofNullable(reTokenResponse));

        CustomException exception = assertThrows(CustomException.class ,() -> userService.refreshToken(tokenRequest));
        assert ErrorCode.USED_REFRESH_TOKEN.getMessage().equals(exception.getErrorCode().getMessage());
    }
}