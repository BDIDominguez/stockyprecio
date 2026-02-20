package com.stock.backend.stock.exception;

import com.stock.backend.common.exception.BaseException;

public class StockInexistenteException extends BaseException {
    public StockInexistenteException(String detalle) {
        super("Sin registro de Stock", "ERROR-004", detalle);
    }
}
