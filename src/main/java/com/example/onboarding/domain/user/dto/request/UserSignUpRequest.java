package com.example.onboarding.domain.user.dto.request;

import com.example.onboarding.domain.user.entity.User;
import com.example.onboarding.domain.user.entity.Authorities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserSignUpRequest {
    @NotBlank
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;

    public User toEntity(){
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .authorities(Authorities.ROLE_USER)
                .build();
    }
}
