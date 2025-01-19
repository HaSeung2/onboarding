package com.example.onboarding.common.exception;

import com.example.onboarding.common.exception.dto.response.CustomExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> customExceptionHandler(CustomException e) {
        return ResponseEntity
                .status(
                        e.getErrorCode().getHttpStatus()
                )
                .body(
                        CustomExceptionResponse.builder()
                                .errorCode(e.getErrorCode())
                                .build()
                );
    }
}
