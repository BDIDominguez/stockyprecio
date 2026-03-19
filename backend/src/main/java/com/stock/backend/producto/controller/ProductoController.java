package com.stock.backend.producto.controller;

import com.stock.backend.producto.dto.ProductoDTO;
import com.stock.backend.producto.dto.ProductoNuevoDTO;
import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.exception.ProductoInexistenteException;
import com.stock.backend.producto.mapper.ProductoMapper;
import com.stock.backend.producto.mapper.ProductoNuevoMapper;
import com.stock.backend.producto.service.ProductoFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
@Validated
public class ProductoController {

    private final ProductoFacadeService servicio;
    private final ProductoMapper productoMapper;
    private final ProductoNuevoMapper productoNuevoMapper;

    @Operation(
            summary = "Obtener siguiente codigo disponible",
            description = "Calcula el siguiente codigo sugerido para un nuevo producto. " +
                    "Es una ayuda para el frontend y no una garantia absoluta de disponibilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Codigo sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "3201")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo() {
        return servicio.siguienteCodigo();
    }

    @Operation(
            summary = "Listar productos activos",
            description = "Devuelve todos los productos activos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping("")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<Producto> lista = servicio.consultarTodosActivos();
        List<ProductoDTO> dtos = lista.stream().map(productoMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Consultar producto por codigo",
            description = "Devuelve un producto especifico a partir de su codigo de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> productoPorCodigo(@PathVariable @Positive Long codigo) {
        Producto producto = servicio.consultarPorCodigo(codigo)
                .orElseThrow(() -> new ProductoInexistenteException("No existe producto con codigo: " + codigo));
        return ResponseEntity.ok(productoMapper.toDto(producto));
    }

    @Operation(
            summary = "Modificar producto por codigo",
            description = "Actualiza un producto identificado por su codigo de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Codigo duplicado")
    })
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ProductoNuevoDTO producto) {
        Producto respuesta = servicio.actualizarPorCodigo(codigo, productoNuevoMapper.toEntidad(producto));
        return ResponseEntity.ok(productoMapper.toDto(respuesta));
    }
}
