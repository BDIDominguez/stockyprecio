package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class RecursoDuplicadoException extends BaseException {
    public RecursoDuplicadoException(String detalle) {
        super("Recurso ya existe", "GEN-409-002", detalle, HttpStatus.CONFLICT);
    }
}
