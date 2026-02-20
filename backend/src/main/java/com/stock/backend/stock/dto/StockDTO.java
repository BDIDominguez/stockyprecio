package com.stock.backend.stock.dto;

public record StockDTO (
        Long id,
        Long codigo,
        Long sucursal,
        Double cantidad,
        Double reserva
        ){}

