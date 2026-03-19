package com.stock.backend.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> manejar(BaseException ex) {
        ApiError body = new ApiError(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getCodigo(),
                ex.getMessage(),
                ex.getDetalle(),
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (!errores.containsKey(error.getField())) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
        });

        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "GEN-400-001",
                "Error de validacion",
                "Uno o mas campos del request no son validos.",
                errores,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> manejarConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errores.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "GEN-400-001",
                "Error de validacion",
                "Uno o mas parametros no cumplen las restricciones.",
                errores,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex) {
        String detalle = "El parametro '" + ex.getName() + "' tiene un valor con tipo incorrecto.";

        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "GEN-400-004",
                "Parametro invalido",
                detalle,
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "GEN-400-002",
                "JSON mal formado",
                "El cuerpo de la peticion no tiene un formato JSON valido o no puede convertirse al DTO esperado.",
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInesperado(Exception ex) {
        ApiError body = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "GEN-500-001",
                "Error interno inesperado",
                ex.getMessage(),
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
