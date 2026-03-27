package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class RecursoEnUsoException extends BaseException {
    public RecursoEnUsoException(String detalle) {
        super("Recurso en uso", "GEN-409-003", detalle, HttpStatus.CONFLICT);
    }
}
