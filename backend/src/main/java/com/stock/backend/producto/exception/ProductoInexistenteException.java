package com.stock.backend.producto.exception;

import com.stock.backend.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProductoInexistenteException extends BaseException {
    public ProductoInexistenteException(String detalle) {
        super("No existe dicho producto", "PROD-404-001", detalle, HttpStatus.NOT_FOUND);
    }
}
