package com.stock.backend.dto;

import com.stock.backend.entity.Categoria;
import com.stock.backend.entity.Producto;
import com.stock.backend.entity.Proveedor;
import com.stock.backend.entity.Stock;

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

