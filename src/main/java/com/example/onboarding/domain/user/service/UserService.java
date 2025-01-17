package com.example.onboarding.domain.user.service;

import com.example.onboarding.domain.user.dto.request.UserSignUpRequest;
import com.example.onboarding.domain.user.dto.response.UserSignUpResponse;
import com.example.onboarding.domain.user.entity.User;
import com.example.onboarding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity();
        if(userRepository.existsByUsername(user.getUsername())){
            throw new IllegalArgumentException("사용할 수 없는 계정명입니다.");
        }
        user.passwordEncoder(passwordEncoder.encode(user.getPassword()));
        return new UserSignUpResponse(userRepository.save(user));
    }
}
