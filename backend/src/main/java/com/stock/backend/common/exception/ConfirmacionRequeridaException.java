package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class ConfirmacionRequeridaException extends BaseException {
    public ConfirmacionRequeridaException(String detalle) {
        super("Confirmacion requerida", "PREC-409-002", detalle, HttpStatus.CONFLICT);
    }
}
