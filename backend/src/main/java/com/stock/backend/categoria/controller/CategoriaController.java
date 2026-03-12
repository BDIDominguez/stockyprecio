package com.stock.backend.categoria.controller;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.categoria.dto.CategoriaNuevaDTO;
import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.mapper.CategoriaMapper;
import com.stock.backend.categoria.mapper.CategoriaNuevaMapper;
import com.stock.backend.categoria.service.CategoriaFacadeService;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor
@Validated
public class CategoriaController {

    private final CategoriaFacadeService service;
    private final CategoriaMapper categoriaMapper;
    private final CategoriaNuevaMapper categoriaNuevaMapper;

    @Operation(
            summary = "Obtener siguiente código disponible",
            description = "Calcula el siguiente código sugerido para una nueva categoría. " +
                    "Este valor se obtiene a partir del código máximo registrado en el sistema. " +
                    "El valor devuelto es solo una sugerencia para facilitar la carga de datos " +
                    "desde el frontend y puede ser modificado por el usuario antes de guardar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Código sugerido obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "1102")
                    )
            )
    })
    @GetMapping("/siguiente-codigo")
    public Long siguienteCodigo(){
        return service.siguienteCodigo();
    }

    @Operation(
            summary = "Consultar categorías",
            description = "Devuelve una lista paginada de categorías. " +
                    "Puede filtrar por nombre (búsqueda parcial) y por estado activo. " +
                    "Si se envía el parámetro 'nombre', se realizará una búsqueda por coincidencia " +
                    "en el nombre de la categoría ignorando mayúsculas/minúsculas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de categorías obtenida correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de búsqueda inválidos"
            )
    })
    @GetMapping("")
    public ResponseEntity<Page<CategoriaDTO>> consultarCategorias(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {

        Page<CategoriaDTO> resultado;

        if (nombre != null) {
            resultado = service.buscarPorNombreIgnoreCase(nombre, page, size, sort)
                    .map(categoriaMapper::toDto);
        } else {
            resultado = service.consultarTodos(activo, page, size, sort)
                    .map(categoriaMapper::toDto);
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CategoriaDTO> consultarCategoriaPorCodigo(@PathVariable @Positive Long codigo){
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(service.buscarPorCodigo(codigo).
                orElseThrow(() -> new RecursoNoEncontradoException("no se encontro una categoria con codigo: " + codigo)));
        return ResponseEntity.ok(categoriaDTO);
    }

    @PostMapping("")
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaNuevaDTO categoria){
        Categoria respuesta = service.crear(categoriaNuevaMapper.toEntidad(categoria));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMapper.toDto(respuesta));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<CategoriaDTO> modificarCategoria(@PathVariable Long codigo, @RequestBody CategoriaDTO categoria){
        Categoria existe = service.modificar(categoriaMapper.toEntidad(categoria),codigo);
        return ResponseEntity.ok(categoriaMapper.toDto(existe));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> desactivarCategoria(@PathVariable Long codigo){
        //Categoria respuesta = service.desactivarPorCodigo(codigo);
        service.desactivarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{codigo}/activar")
    public ResponseEntity<Void> activarCategoria(@PathVariable Long codigo){
        //Categoria respuesta = service.activarPorCodigo(codigo);
        service.activarPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }
}
