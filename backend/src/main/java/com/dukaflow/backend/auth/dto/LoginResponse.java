package com.dukaflow.backend.auth.dto;

import java.time.Instant;
import java.util.List;

public record LoginResponse(
        boolean success,
        String message,
        Data data,
        Instant timestamp) {

    public record Data(
            String accessToken,
            String refreshToken,
            String tokenType,
            long expiresInSeconds,
            User user) {
    }

    public record User(
            Long id,
            String fullName,
            String email,
            Role role,
            Business business,
            List<Branch> branches) {
    }

    public record Role(
            Long id,
            String name) {
    }

    public record Business(
            Long id,
            String name) {
    }

    public record Branch(
            Long id,
            String code,
            String name) {
    }
}
