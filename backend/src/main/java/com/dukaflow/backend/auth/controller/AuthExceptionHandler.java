package com.dukaflow.backend.auth.controller;

import com.dukaflow.backend.common.api.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.oauth2.jwt.JwtException;
import com.dukaflow.backend.auth.exception.InvalidRefreshTokenException;
import com.dukaflow.backend.auth.exception.AuthenticatedUserNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = ApiErrorResponse.of(
                "INVALID_CREDENTIALS",
                "Invalid email, username or password.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleJwtException(
            JwtException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = ApiErrorResponse.of(
                "AUTHENTICATION_REQUIRED",
                "Invalid or expired authentication token.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = ApiErrorResponse.of(
                "AUTHENTICATION_REQUIRED",
                "Invalid refresh token.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(AuthenticatedUserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticatedUserNotFoundException(
            AuthenticatedUserNotFoundException exception,
            HttpServletRequest request) {

        ApiErrorResponse response = ApiErrorResponse.of(
                "AUTHENTICATION_REQUIRED",
                "Authenticated user was not found.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        List<ApiErrorResponse.ErrorDetail> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiErrorResponse.ErrorDetail(
                        error.getField(),
                        error.getDefaultMessage()))
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                false,
                "VALIDATION_ERROR",
                "Request validation failed.",
                errors,
                java.time.Instant.now(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
