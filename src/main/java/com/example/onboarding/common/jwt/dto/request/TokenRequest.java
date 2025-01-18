package com.example.onboarding.common.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenRequest {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;

    @Builder
    public TokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
