package com.example.onboarding.domain.user.dto.response;

import com.example.onboarding.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class UserSignUpResponse {
    @Schema(name = "username", description = "아이디")
    private final String username;
    @Schema(name = "nickname", description = "닉네임")
    private final String nickname;
    @Schema(name = "authorities", description = "권한 리스트")
    private final List<AuthoritiesResponse> authorities;

    public UserSignUpResponse(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.authorities = List.of(new AuthoritiesResponse(user.getAuthorities()));
    }
}
