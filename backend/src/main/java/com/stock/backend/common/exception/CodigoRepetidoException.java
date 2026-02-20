package com.stock.backend.common.exception;

public class CodigoRepetidoException extends BaseException {
    public CodigoRepetidoException(String detalle) {
        super("Ese codigo ya existe", "ERROR-0002", detalle);
    }
}
