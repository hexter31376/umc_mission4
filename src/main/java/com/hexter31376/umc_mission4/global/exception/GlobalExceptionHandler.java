package com.hexter31376.umc_mission4.global.exception;

import com.hexter31376.umc_mission4.global.apiPayload.ApiErrorResponse;
import com.hexter31376.umc_mission4.global.apiPayload.code.GeneralErrorCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(GeneralErrorCode.BAD_REQUEST.getStatus())
                .body(new ApiErrorResponse(GeneralErrorCode.BAD_REQUEST.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(GeneralErrorCode.BAD_REQUEST.getStatus())
                .body(new ApiErrorResponse(GeneralErrorCode.BAD_REQUEST.getCode(), "validation failed"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(GeneralErrorCode.NOT_FOUND.getStatus())
                .body(new ApiErrorResponse(GeneralErrorCode.NOT_FOUND.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAll(Exception ex) {
        return ResponseEntity.status(GeneralErrorCode.INTERNAL_ERROR.getStatus())
                .body(new ApiErrorResponse(GeneralErrorCode.INTERNAL_ERROR.getCode(), ex.getMessage()));
    }
}

