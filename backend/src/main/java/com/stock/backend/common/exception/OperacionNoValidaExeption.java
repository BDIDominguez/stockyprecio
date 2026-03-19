package com.stock.backend.common.exception;

public class OperacionNoValidaExeption extends BaseException {
    public OperacionNoValidaExeption(String detalle) {
        super("Operación no permitida", "GEN-400-001", detalle);
    }
}
