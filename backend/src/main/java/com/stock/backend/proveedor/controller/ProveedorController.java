package com.stock.backend.proveedor.controller;

import com.stock.backend.proveedor.dto.ProveedorDTO;
import com.stock.backend.proveedor.dto.ProveedorNuevoDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.mapper.ProveedorMapper;
import com.stock.backend.proveedor.mapper.ProveedorNuevoMapper;
import com.stock.backend.proveedor.service.ProveedorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/proveedores")
@AllArgsConstructor
@Validated
public class ProveedorController {

    private final ProveedorService servicio;
    private final ProveedorMapper mapper;
    private final ProveedorNuevoMapper nuevoMapper;

    @Operation(
            summary = "Obtener siguiente codigo disponible",
            description = "Calcula el siguiente codigo sugerido para un nuevo proveedor. " +
                    "Es una ayuda para el frontend y no una garantia absoluta de disponibilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Codigo sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "1203")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo() {
        return servicio.siguienteCodigo();
    }

    @Operation(
            summary = "Consultar proveedores",
            description = "Devuelve una lista paginada de proveedores, con filtro opcional por nombre y estado activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parametros de busqueda invalidos")
    })
    @GetMapping("")
    public ResponseEntity<Page<ProveedorDTO>> consultarProveedores(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {

        Page<ProveedorDTO> resultado;
        if (nombre != null) {
            resultado = servicio.buscarPorNombreIgnoreCase(nombre, activo, page, size, sort).map(mapper::toDto);
        } else {
            resultado = servicio.consultarTodos(activo, page, size, sort).map(mapper::toDto);
        }
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Consultar proveedor por codigo",
            description = "Devuelve un proveedor especifico a partir de su codigo de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProveedorDTO> consultarProveedorPorCodigo(@PathVariable @Positive Long codigo) {
        ProveedorDTO proveedor = mapper.toDto(servicio.buscarPorCodigo(codigo)
                .orElseThrow(() -> new com.stock.backend.common.exception.RecursoNoEncontradoException(
                        "No existe un proveedor con el codigo: " + codigo)));
        return ResponseEntity.ok(proveedor);
    }

    @Operation(
            summary = "Crear proveedor",
            description = "Crea un nuevo proveedor validando previamente que el codigo no exista."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Codigo duplicado")
    })
    @PostMapping("")
    public ResponseEntity<ProveedorDTO> crearProveedor(@Valid @RequestBody ProveedorNuevoDTO nuevo) {
        Proveedor respuesta = servicio.crear(nuevoMapper.toEntity(nuevo));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(respuesta));
    }

    @Operation(
            summary = "Desactivar proveedor",
            description = "Marca un proveedor como inactivo a partir de su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivarProveedor(@PathVariable @Positive Long codigo) {
        servicio.desactivarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Activar proveedor",
            description = "Marca un proveedor como activo a partir de su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PatchMapping("/{codigo}/activar")
    public ResponseEntity<Void> activarProveedor(@PathVariable @Positive Long codigo) {
        servicio.activarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Modificar proveedor",
            description = "Actualiza los datos de un proveedor identificado por su codigo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Codigo duplicado")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ProveedorDTO dto) {
        ProveedorDTO respuesta = mapper.toDto(servicio.modificar(codigo, mapper.toEntity(dto)));
        return ResponseEntity.ok(respuesta);
    }
}
