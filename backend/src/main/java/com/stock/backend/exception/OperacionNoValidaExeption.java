package com.stock.backend.exception;

public class OperacionNoValidaExeption extends BaseException {
    public OperacionNoValidaExeption(String detalle) {
        super("Operación no permitida", "ERROR-0001", detalle);
    }
}
