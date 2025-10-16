package com.stock.backend.dto;


import com.stock.backend.entity.Stock;

public record StockDTO (
        Long id,
        Long sucursal,
        Double cantidad,
        Double reserva
        ){
    public StockDTO (Stock datos){
        this(
                datos.getId(),
                datos.getSucursal(),
                datos.getCantidad(),
                datos.getReserva()
        );
    }
}
