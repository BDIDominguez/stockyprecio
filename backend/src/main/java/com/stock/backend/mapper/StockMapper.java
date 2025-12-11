package com.stock.backend.mapper;

import com.stock.backend.dto.StockDTO;
import com.stock.backend.entity.Stock;

public class StockMapper {
    public static StockDTO toDto(Stock origen){
        return new StockDTO(origen.getId(), origen.getCodigo(), origen.getSucursal(), origen.getCantidad(), origen.getReserva());
    }

    public static  Stock toEntity(StockDTO origen){
        return new Stock(origen.id(), origen.codigo(), origen.sucursal(), origen.cantidad(), origen.reserva());
    }
}
