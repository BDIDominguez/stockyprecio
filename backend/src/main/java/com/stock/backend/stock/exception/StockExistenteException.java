package com.stock.backend.stock.exception;

import com.stock.backend.common.exception.BaseException;

public class StockExistenteException extends BaseException {
    public StockExistenteException(String detalle) {
        super("Ese codigo ya existe para esa sucursdal", "ERROR-006", detalle);
    }
}
