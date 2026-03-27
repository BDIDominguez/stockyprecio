package com.stock.backend.componenteCosto.controller;

import com.stock.backend.componenteCosto.dto.ComponenteCostoDTO;
import com.stock.backend.componenteCosto.dto.ComponenteCostoNuevoDTO;
import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import com.stock.backend.componenteCosto.mapper.ComponenteCostoMapper;
import com.stock.backend.componenteCosto.mapper.ComponenteCostoNuevoMapper;
import com.stock.backend.componenteCosto.service.ComponenteCostoService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/componentes-costo")
@AllArgsConstructor
@Validated
public class ComponenteCostoController {
    private final ComponenteCostoService service;
    private final ComponenteCostoMapper mapper;
    private final ComponenteCostoNuevoMapper nuevoMapper;

    @Operation(
            summary = "Obtener siguiente codigo disponible",
            description = "Calcula el siguiente codigo sugerido para un nuevo componente de costo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Codigo sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "2101")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo() {
        return service.siguienteCodigo();
    }

    @Operation(summary = "Consultar componentes de costo")
    @GetMapping("")
    public ResponseEntity<Page<ComponenteCostoDTO>> consultarTodos(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {

        Page<ComponenteCostoDTO> respuesta;
        if (nombre != null) {
            respuesta = service.buscarPorNombreIgnoreCase(nombre, activo, page, size, sort)
                    .map(mapper::toDto);
        } else {
            respuesta = service.consultarTodos(activo, page, size, sort)
                    .map(mapper::toDto);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Consultar componente de costo por codigo")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ComponenteCostoDTO> consultarPorCodigo(@PathVariable @Positive Long codigo) {
        return ResponseEntity.ok(mapper.toDto(service.consultarPorCodigo(codigo)));
    }

    @Operation(summary = "Crear componente de costo")
    @PostMapping("")
    public ResponseEntity<ComponenteCostoDTO> crear(@Valid @RequestBody ComponenteCostoNuevoDTO dto) {
        ComponenteCosto creado = service.crear(nuevoMapper.toEntidad(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(creado));
    }

    @Operation(summary = "Modificar componente de costo")
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ComponenteCostoDTO> modificar(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ComponenteCostoNuevoDTO dto) {
        ComponenteCosto actualizado = service.modificar(codigo, nuevoMapper.toEntidad(dto));
        return ResponseEntity.ok(mapper.toDto(actualizado));
    }

    @Operation(summary = "Desactivar componente de costo")
    @DeleteMapping("/codigo/{codigo}")
    public ResponseEntity<Void> desactivar(@PathVariable @Positive Long codigo) {
        service.desactivar(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar componente de costo")
    @PatchMapping("/codigo/{codigo}/activar")
    public ResponseEntity<Void> activar(@PathVariable @Positive Long codigo) {
        service.activar(codigo);
        return ResponseEntity.noContent().build();
    }
}
