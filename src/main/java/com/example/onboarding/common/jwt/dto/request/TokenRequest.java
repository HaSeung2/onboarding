package com.example.onboarding.common.jwt.dto.request;

import lombok.Getter;

@Getter
public class TokenRequest {
    private String accessToken;
    private String refreshToken;
}
