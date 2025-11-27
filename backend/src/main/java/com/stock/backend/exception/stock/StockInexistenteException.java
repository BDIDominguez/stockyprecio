package com.stock.backend.exception.stock;

import com.stock.backend.exception.BaseException;

public class StockInexistenteException extends BaseException {
    public StockInexistenteException(String detalle) {
        super("Sin registro de Stock", "ERROR-004", detalle);
    }
}
