package com.stock.backend.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> manejar(BaseException ex){
        return ResponseEntity.badRequest().body(
                Map.of("mensaje", ex.getMessage(),
                        "codigo", ex.getCodigo(),
                        "detalle", ex.getDetalle(),
                        "timestamp", LocalDateTime.now())
        );
    }
}
