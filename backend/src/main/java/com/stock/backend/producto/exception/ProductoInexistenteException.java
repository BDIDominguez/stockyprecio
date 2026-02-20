package com.stock.backend.producto.exception;

import com.stock.backend.common.exception.BaseException;

public class ProductoInexistenteException extends BaseException {
    public ProductoInexistenteException(String detalle) {
        super("No existe dicho producto", "ERROR-005", detalle);
    }
}
