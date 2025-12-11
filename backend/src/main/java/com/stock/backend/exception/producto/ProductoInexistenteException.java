package com.stock.backend.exception.producto;

import com.stock.backend.exception.BaseException;

public class ProductoInexistenteException extends BaseException {
    public ProductoInexistenteException(String detalle) {
        super("No existe dicho producto", "ERROR-005", detalle);
    }
}
