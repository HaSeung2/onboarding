package com.example.onboarding.common.jwt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    @Schema(name = "token", description = "Access 토큰")
    private final String token;
    @Schema(name = "refreshToken", description = "refresh 토큰")
    private final String refreshToken;

    @Builder
    public TokenResponse(String token , String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
