package com.dukaflow.backend.auth.dto;

import java.time.Instant;

public record RefreshTokenResponse(
        boolean success,
        String message,
        Data data,
        Instant timestamp) {

    public record Data(
            String accessToken,
            String refreshToken,
            String tokenType,
            long expiresInSeconds) {
    }
}
