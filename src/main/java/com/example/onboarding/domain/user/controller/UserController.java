package com.example.onboarding.domain.user.controller;

import com.example.onboarding.common.jwt.dto.request.TokenRequest;
import com.example.onboarding.common.jwt.dto.response.TokenResponse;
import com.example.onboarding.domain.user.dto.request.UserSignRequest;
import com.example.onboarding.domain.user.dto.request.UserSignUpRequest;
import com.example.onboarding.domain.user.dto.response.UserSignUpResponse;
import com.example.onboarding.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입 / 로그인 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @Parameter(name = "username", description = "아이디")
    @Parameter(name = "password", description = "비밀번호")
    @Parameter(name = "nickname", description = "닉네임")
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signUp(
            @RequestBody @Valid UserSignUpRequest signUpRequest
    ){
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }

    @Operation(summary = "로그인")
    @Parameter(name = "username", description = "아이디")
    @Parameter(name = "password", description = "비밀번호")
    @PostMapping("/sign")
    public ResponseEntity<TokenResponse> sign(
            @RequestBody @Valid UserSignRequest signRequest
    ){
        TokenResponse tokenResponse = userService.sign(signRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .body(tokenResponse);
    }

    @Operation(summary = "토큰 재 발급")
    @Parameter(name = "accessToken", description = "Access 토큰")
    @Parameter(name = "refreshToken", description = "Refresh 토큰")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody TokenRequest tokenRequest
    ){
        TokenResponse tokenResponse = userService.refreshToken(tokenRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .body(tokenResponse);
    }
}
