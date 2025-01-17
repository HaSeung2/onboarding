package com.example.onboarding.domain.user.dto.response;

import com.example.onboarding.domain.user.entity.Authorities;
import lombok.Getter;

@Getter
public class AuthoritiesResponse {
    private final Authorities authorityName;

    public AuthoritiesResponse(Authorities authorities) {
        this.authorityName = authorities;
    }
}
