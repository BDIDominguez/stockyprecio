package com.stock.backend.sucursal.controller;

import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.dto.SucursalNuevaDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.mapper.SucursalMapper;
import com.stock.backend.sucursal.mapper.SucursalNuevaMapper;
import com.stock.backend.sucursal.service.SucursalService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sucursal")
@AllArgsConstructor
@Validated
public class SucursalController {

    private final SucursalService service;
    private final SucursalMapper mapper;
    private final SucursalNuevaMapper nuevaMapper;

    @Operation(
            summary = "Obtener siguiente codigo disponible",
            description = "Calcula el siguiente codigo sugerido para una nueva sucursal. " +
                    "Es una ayuda para el frontend y no una garantia absoluta de disponibilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Codigo sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "205")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo() {
        return service.siguienteCodigo();
    }

    @Operation(
            summary = "Consultar sucursales",
            description = "Devuelve una lista paginada de sucursales, con filtro opcional por nombre y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parametros de busqueda invalidos")
    })
    @GetMapping("")
    public ResponseEntity<Page<SucursalDTO>> consultar(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {

        Page<SucursalDTO> resultado;
        if (nombre != null) {
            resultado = service.buscarPorNombreIgnoreCase(nombre, activo, page, size, sort).map(mapper::toDto);
        } else {
            resultado = service.consultarTodas(activo, page, size, sort).map(mapper::toDto);
        }
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Consultar sucursal por codigo",
            description = "Devuelve una sucursal especifica a partir de su codigo de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<SucursalDTO> consultarPorCodigo(@PathVariable @Positive Long codigo) {
        SucursalDTO respuesta = mapper.toDto(service.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con el codigo: " + codigo)));
        return ResponseEntity.ok(respuesta);
    }

    @Operation(
            summary = "Crear sucursal",
            description = "Crea una nueva sucursal validando previamente que el codigo no exista."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucursal creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Codigo duplicado")
    })
    @PostMapping("")
    public ResponseEntity<SucursalDTO> crearSucursal(@RequestBody @Valid SucursalNuevaDTO dto) {
        Sucursal sucursal = service.crear(nuevaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(sucursal));
    }

    @Operation(
            summary = "Modificar sucursal",
            description = "Actualiza una sucursal identificada por su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
            @ApiResponse(responseCode = "409", description = "Codigo duplicado")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<SucursalDTO> modificarSucursal(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody SucursalDTO dto) {
        Sucursal respuesta = service.modificar(codigo, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(respuesta));
    }

    @Operation(
            summary = "Desactivar sucursal",
            description = "Marca una sucursal como inactiva a partir de su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucursal desactivada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivarSucursal(@PathVariable @Positive Long codigo) {
        service.desactivarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Activar sucursal",
            description = "Marca una sucursal como activa a partir de su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucursal activada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @PatchMapping("/{codigo}/activar")
    public ResponseEntity<Void> activarSucursal(@PathVariable @Positive Long codigo) {
        service.activarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }
}
