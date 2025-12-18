package com.stock.backend.exception;

public class RecursoDuplicadoException extends BaseException {
    public RecursoDuplicadoException(String detalle) {
        super("Recurso ya existe", "ERROR-0002", detalle);
    }
}
