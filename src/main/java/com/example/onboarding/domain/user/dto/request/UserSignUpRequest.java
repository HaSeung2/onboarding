package com.example.onboarding.domain.user.dto.request;

import com.example.onboarding.domain.user.entity.User;
import com.example.onboarding.domain.user.entity.Authorities;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    public User toEntity(String encodePassword) {
        return User.builder()
                .username(username)
                .password(encodePassword)
                .nickname(nickname)
                .authorities(Authorities.ROLE_USER)
                .build();
    }

    @Builder
    public UserSignUpRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
