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
    Boolean activo,
    @NotNull(message = "Tiene que tener sucursal si o si.")
    Long sucursal
){
    public ProductoNuevoDTO(Producto producto, Long suc) {
        this(producto.getCodigo(), producto.getNombre(),
                producto.getDescripcion(), producto.getCategoria(),
                producto.getProveedor(), producto.getStockMinimo(),
                producto.getManejaStock(), producto.getActivo(), suc);
    }

    public Producto convertir() {
        return Producto.builder()
                .codigo(this.codigo)
                .nombre(this.nombre)
                .descripcion(this.descripcion)
                .categoria(this.categoriaId)
                .proveedor(this.proveedorId)
                .stockMinimo(this.stockMinimo != null ? this.stockMinimo : 0)
                .manejaStock(this.manejaStock != null ? this.manejaStock : true)
                .activo(this.activo != null ? this.activo : true)
                .build();
    }
}
