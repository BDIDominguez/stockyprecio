package com.stock.backend.producto.exception;

import com.stock.backend.common.exception.BaseException;

public class ProductoExistenteException extends BaseException {
    public ProductoExistenteException(String detalle) {
        super("Ya existe dicho producto", "ERROR-005", detalle);
    }
}
