package com.example.onboarding.domain.user.dto.response;

import com.example.onboarding.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserSignUpResponse {
    private final String username;
    private final String nickname;
    private final List<AuthoritiesResponse> authorities;

    public UserSignUpResponse(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.authorities = List.of(new AuthoritiesResponse(user.getAuthorities()));
    }
}
