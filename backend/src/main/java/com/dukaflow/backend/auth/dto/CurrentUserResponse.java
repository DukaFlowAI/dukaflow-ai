package com.dukaflow.backend.auth.dto;

import java.time.Instant;

public record CurrentUserResponse(
        boolean success,
        String message,
        LoginResponse.User data,
        Instant timestamp) {
}
