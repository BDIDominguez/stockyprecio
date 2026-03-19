package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class OperacionNoValidaExeption extends BaseException {
    public OperacionNoValidaExeption(String detalle) {
        super("Operacion no permitida", "GEN-400-003", detalle, HttpStatus.BAD_REQUEST);
    }
}
