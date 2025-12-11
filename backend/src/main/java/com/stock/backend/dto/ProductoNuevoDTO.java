package com.stock.backend.dto;

import com.stock.backend.entity.Producto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoNuevoDTO (
    @NotNull(message = "Código es obligatorio")
    Long codigo,
    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 200, message = "Nombre no puede exceder 200 caracteres")
    String nombre,
    @Size(max = 1000, message = "Descripción muy larga")
    String descripcion,
    @NotNull
    Long categoriaId,
    @NotNull
    Long proveedorId,
    @Min(value = 0, message = "Stock mínimo no puede ser negativo")
    Double stockMinimo,
    Boolean manejaStock,
    Boolean activo

){}
