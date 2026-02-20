package com.stock.backend.common.exception;

public class RecursoNoEncontradoException extends BaseException {
    public RecursoNoEncontradoException(String detalle) {
        super("Recurso no encontrado", "ERROR-0002", detalle);
    }
}
