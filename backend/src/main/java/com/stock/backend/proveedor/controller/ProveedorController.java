package com.stock.backend.proveedor.controller;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.proveedor.dto.ProveedorDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.mapper.ProveedorMapper;
import com.stock.backend.proveedor.service.ProveedorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@AllArgsConstructor
@Validated
public class ProveedorController {

    private final ProveedorService servicio;
    private final ProveedorMapper mapper;

    @GetMapping("")
    public ResponseEntity<Page<ProveedorDTO>> consutlarProveedores(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort){

        Page<ProveedorDTO> resultado;
        if (nombre != null) {
            resultado = servicio.buscarPorNombreIgnoreCase(nombre, page, size, sort).map(mapper::toDto);
        } else {
            resultado = servicio.consultarTodos(activo, page, size, sort).map(mapper::toDto);
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("")
    public ResponseEntity<ProveedorDTO> crearProveedor(@Valid @RequestBody ProveedorDTO nuevo){
        Proveedor respuesta = servicio.crearProveedor(mapper.toEntity(nuevo));
        return ResponseEntity.ok(mapper.toDto(respuesta));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivarProveedor(@Valid @Size(min = 1) @PathVariable Long codigo){
        servicio.desactivarPorCodigo(codigo);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{codigo}/activar")
    public ResponseEntity<Void> activarProveedor(@PathVariable Long codigo){
        servicio.activarPorCodigo(codigo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(@PathVariable Long codigo, @Valid @RequestBody ProveedorDTO dto){
        ProveedorDTO respuesta = mapper.toDto(servicio.actualizarProveedor(codigo, mapper.toEntity(dto)));
        return ResponseEntity.ok(respuesta);
    }

}
