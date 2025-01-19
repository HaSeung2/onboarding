package com.example.onboarding.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 유저 에러
    USER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "로그인을 다시 시도해주세요."),
    USER_NAME_NOT_MATCH(HttpStatus.NOT_FOUND, "로그인을 다시 시도해주세요."),
    DUPLICATION_USER_NAME(HttpStatus.BAD_REQUEST, "사용할 수 없는 계정명입니다."),
    USER_ID_NOT_MATCH(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    // 토큰 에러
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "다시 로그인 해주세요."),
    USED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "이미 사용한 RefreshToken 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
