package com.stock.backend.exception;

public abstract class BaseException extends RuntimeException{
    private final String codigo;
    private final String detalle;

    public BaseException(String message, String codigo, String detalle) {
        super(message);
        this.codigo = codigo;
        this.detalle = detalle;
    }

    public String getCodigo(){return codigo;}
    public String getDetalle(){return detalle;}
}
