package com.stock.backend.movimientoTipo.exception;

import com.stock.backend.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class MovimientosTipoInexistente extends BaseException {
    public MovimientosTipoInexistente(String detalle) {
        super("No existe dicho movimiento", "ERROR-005", detalle, HttpStatus.NOT_FOUND);
    }
}
