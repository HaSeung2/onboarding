package com.example.onboarding.common.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenRequest {
    @NotBlank
    private String token;
    @NotBlank
    private String refreshToken;

    @Builder
    public TokenRequest(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
