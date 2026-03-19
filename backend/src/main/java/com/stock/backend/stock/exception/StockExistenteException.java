package com.stock.backend.stock.exception;

import com.stock.backend.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class StockExistenteException extends BaseException {
    public StockExistenteException(String detalle) {
        super("Ya existe stock para ese codigo y sucursal", "STK-409-001", detalle, HttpStatus.CONFLICT);
    }
}
