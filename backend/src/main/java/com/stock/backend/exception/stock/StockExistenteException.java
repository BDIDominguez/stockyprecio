package com.stock.backend.exception.stock;

import com.stock.backend.exception.BaseException;

public class StockExistenteException extends BaseException {
    public StockExistenteException(String detalle) {
        super("Ese codigo ya existe para esa sucursdal", "ERROR-006", detalle);
    }
}
