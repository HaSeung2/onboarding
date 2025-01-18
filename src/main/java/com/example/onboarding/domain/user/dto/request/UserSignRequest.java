package com.example.onboarding.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @Builder
    public UserSignRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
