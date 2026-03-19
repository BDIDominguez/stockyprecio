package com.stock.backend.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final String codigo;
    private final String detalle;
    private final HttpStatus status;

    public BaseException(String message, String codigo, String detalle, HttpStatus status) {
        super(message);
        this.codigo = codigo;
        this.detalle = detalle;
        this.status = status;
    }

    public String getCodigo() { return codigo; }
    public String getDetalle() { return detalle; }
    public HttpStatus getStatus() { return status; }
}
