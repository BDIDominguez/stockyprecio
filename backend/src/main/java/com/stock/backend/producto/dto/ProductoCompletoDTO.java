package com.stock.backend.producto.dto;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.stock.entity.Stock;

import java.time.LocalDateTime;

public record ProductoCompletoDTO(Long codigo,
                                  String nombre,
                                  String descripcion,
                                  Categoria categoria,
                                  Proveedor proveedor,
                                  Stock stock,
                                  Boolean manejaStock ,
                                  Boolean activo,
                                  LocalDateTime fechaCreacion,
                                  LocalDateTime fechaModificacion
) { }

