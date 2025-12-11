package com.stock.backend.exception.producto;

import com.stock.backend.exception.BaseException;

public class ProductoExistenteException extends BaseException {
    public ProductoExistenteException(String detalle) {
        super("Ya existe dicho producto", "ERROR-005", detalle);
    }
}
