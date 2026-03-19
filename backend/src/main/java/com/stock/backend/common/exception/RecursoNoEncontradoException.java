package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class RecursoNoEncontradoException extends BaseException {
    public RecursoNoEncontradoException(String detalle) {
        super("Recurso no encontrado", "GEN-404-001", detalle, HttpStatus.NOT_FOUND);
    }
}
