package com.example.onboarding.common.exception.dto.response;

import com.example.onboarding.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomExceptionResponse {
    private final String message;
    private final HttpStatus httpStatus;

    @Builder
    public CustomExceptionResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
