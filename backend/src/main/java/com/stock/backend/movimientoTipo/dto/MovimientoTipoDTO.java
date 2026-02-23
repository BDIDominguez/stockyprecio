package com.stock.backend.movimientoTipo.dto;

public record MovimientoTipoDTO(
    Long id,
    String siglas,
    String nombre,
    String descripcion,
    Integer afectaStock,
    Boolean requiereSucursalOrigen,
    Boolean requiereSucursalDestino,
    Boolean activo) {

}
