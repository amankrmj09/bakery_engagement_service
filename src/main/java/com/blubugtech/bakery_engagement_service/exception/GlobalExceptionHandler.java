package com.blubugtech.bakery_engagement_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.blubakery.common.core.exception.handler.BaseExceptionHandler;
import org.blubakery.common.core.exception.handler.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex, WebRequest request) {
        log.error("Engagement service error: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("ENGAGEMENT_ERROR")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.badRequest().body(error);
    }
}
