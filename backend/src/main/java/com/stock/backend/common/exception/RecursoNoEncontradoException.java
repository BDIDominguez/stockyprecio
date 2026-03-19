package com.stock.backend.common.exception;

public class RecursoNoEncontradoException extends BaseException {
    public RecursoNoEncontradoException(String detalle) {
        super("Recurso no encontrado", "GEN-404-001", detalle);
    }
}
