package com.stock.backend.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        int status,
        String error,
        String codigo,
        String mensaje,
        String detalle,
        Map<String, String> errores,
        LocalDateTime timestamp
) {
}
