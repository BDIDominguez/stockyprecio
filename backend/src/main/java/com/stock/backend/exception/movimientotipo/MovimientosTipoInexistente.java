package com.stock.backend.exception.movimientotipo;

import com.stock.backend.exception.BaseException;

public class MovimientosTipoInexistente extends BaseException {
    public MovimientosTipoInexistente(String detalle) {
        super("No existe dicho movimiento", "ERROR-005", detalle);
    }
}
