package com.stock.backend.dto;


import com.stock.backend.entity.Stock;

public record StockDTO (
        Long id,
        Long codigo,
        Long sucursal,
        Double cantidad,
        Double reserva
        ){}

