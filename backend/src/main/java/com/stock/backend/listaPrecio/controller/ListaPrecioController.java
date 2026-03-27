package com.stock.backend.listaPrecio.controller;

import com.stock.backend.listaPrecio.dto.ListaPrecioDTO;
import com.stock.backend.listaPrecio.dto.ListaPrecioNuevaDTO;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import com.stock.backend.listaPrecio.mapper.ListaPrecioMapper;
import com.stock.backend.listaPrecio.mapper.ListaPrecioNuevaMapper;
import com.stock.backend.listaPrecio.service.ListaPrecioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/listas-precios")
@AllArgsConstructor
@Validated
public class ListaPrecioController {
    private final ListaPrecioService service;
    private final ListaPrecioMapper listaPrecioMapper;
    private final ListaPrecioNuevaMapper listaPrecioNuevaMapper;

    @Operation(
            summary = "Obtener siguiente codigo disponible",
            description = "Calcula el siguiente codigo sugerido para una nueva lista de precio. " +
                    "Es una ayuda para el frontend y no una garantia absoluta de disponibilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Codigo sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "3101")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo() {
        return service.siguienteCodigo();
    }

    @Operation(
            summary = "Consultar listas de precios",
            description = "Devuelve una lista paginada de listas de precios, con filtro opcional por nombre y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de listas de precios obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parametros de busqueda invalidos")
    })
    @GetMapping("")
    public ResponseEntity<Page<ListaPrecioDTO>> consultarTodos(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {
        Page<ListaPrecioDTO> respuesta;
        if (nombre != null) {
            respuesta = service.buscarPorNombreIgnoreCase(nombre, activo, page, size, sort)
                    .map(listaPrecioMapper::toDto);
        } else {
            respuesta = service.consultarTodos(activo, page, size, sort)
                    .map(listaPrecioMapper::toDto);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Consultar lista de precio por codigo")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ListaPrecioDTO> consultarPorCodigo(@PathVariable @Positive Long codigo) {
        return ResponseEntity.ok(listaPrecioMapper.toDto(service.consultarPorCodigo(codigo)));
    }

    @Operation(summary = "Crear lista de precio")
    @PostMapping("")
    public ResponseEntity<ListaPrecioDTO> crear(@Valid @RequestBody ListaPrecioNuevaDTO dto) {
        ListaPrecio creada = service.crear(listaPrecioNuevaMapper.toEntidad(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(listaPrecioMapper.toDto(creada));
    }

    @Operation(summary = "Modificar lista de precio")
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ListaPrecioDTO> modificar(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ListaPrecioNuevaDTO dto) {
        ListaPrecio actualizada = service.modificar(codigo, listaPrecioNuevaMapper.toEntidad(dto));
        return ResponseEntity.ok(listaPrecioMapper.toDto(actualizada));
    }

    @Operation(summary = "Desactivar lista de precio")
    @DeleteMapping("/codigo/{codigo}")
    public ResponseEntity<Void> desactivar(@PathVariable @Positive Long codigo) {
        service.desactivar(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar lista de precio")
    @PatchMapping("/codigo/{codigo}/activar")
    public ResponseEntity<Void> activar(@PathVariable @Positive Long codigo) {
        service.activar(codigo);
        return ResponseEntity.noContent().build();
    }
}
