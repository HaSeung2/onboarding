package com.example.onboarding.common.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    private final String token;
    private final String refreshToken;

    @Builder
    public TokenResponse(String token , String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
