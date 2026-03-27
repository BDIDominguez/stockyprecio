package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public class CalculoInconsistenteException extends BaseException {
    public CalculoInconsistenteException(String detalle) {
        super("Inconsistencia de calculo", "PREC-409-001", detalle, HttpStatus.CONFLICT);
    }
}
