package com.dukaflow.backend.common.api;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        boolean success,
        String code,
        String message,
        List<ErrorDetail> errors,
        Instant timestamp,
        String path) {

    public record ErrorDetail(
            String field,
            String message) {
    }

    public static ApiErrorResponse of(
            String code,
            String message,
            String path) {

        return new ApiErrorResponse(
                false,
                code,
                message,
                List.of(),
                Instant.now(),
                path);
    }
}
