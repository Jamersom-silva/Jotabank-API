package com.jamersom.jotabank.common.errors;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String path,
        Map<String, String> fieldErrors
) {
    public static ApiError of(int status, String error, String path) {
        return new ApiError(OffsetDateTime.now(), status, error, path, null);
    }

    public static ApiError of(int status, String error, String path, Map<String, String> fieldErrors) {
        return new ApiError(OffsetDateTime.now(), status, error, path, fieldErrors);
    }
}
