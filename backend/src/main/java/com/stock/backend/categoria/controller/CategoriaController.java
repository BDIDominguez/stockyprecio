package com.stock.backend.categoria.controller;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.categoria.dto.CategoriaNuevaDTO;
import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.mapper.CategoriaMapper;
import com.stock.backend.categoria.mapper.CategoriaNuevaMapper;
import com.stock.backend.categoria.service.CategoriaFacadeService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor
public class CategoriaController {

    private final CategoriaFacadeService service;
    private final CategoriaMapper categoriaMapper;
    private final CategoriaNuevaMapper categoriaNuevaMapper;

    @GetMapping("")
    public Page<CategoriaDTO> consultarCategorias(@RequestParam(defaultValue = "true") Boolean activo,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "nombre") String sort){
        return service.consultarTodos(activo, page, size, sort).map(categoriaMapper::toDto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<CategoriaDTO>> buscarPorNombre(@RequestParam @Size(min=2, message = "debe ingresar almenos 2 caracteres") String nombre,
                                                              Pageable pageable){
        Page<CategoriaDTO> resultado = service.buscarPorNombreIgnoreCase(nombre, pageable).map(categoriaMapper::toDto);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaDTO> consultarCategoriaPorNombre(@PathVariable String nombre){
        CategoriaDTO categoria = categoriaMapper.toDto(service.buscarPorNombre(nombre).
                orElseThrow(() -> new RecursoNoEncontradoException("no se encontro una categoria con el nombre: " + nombre)));
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CategoriaDTO> consultarCategoriaPorCodigo(@PathVariable Long codigo){
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(service.buscarPorCodigo(codigo).
                orElseThrow(() -> new RecursoNoEncontradoException("no se encontro una categoria con codigo: " + codigo)));
        return ResponseEntity.ok(categoriaDTO);
    }

    @PostMapping("")
    public ResponseEntity<CategoriaDTO> crearCategoria(@Validated @RequestBody CategoriaNuevaDTO categoria){
        Categoria respuesta = service.crear(categoriaNuevaMapper.toEntidad(categoria));
        return ResponseEntity.ok(categoriaMapper.toDto(respuesta));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<CategoriaDTO> modificarCategoria(@PathVariable Long codigo, @RequestBody CategoriaDTO categoria){
        Categoria existe = service.modificar(categoriaMapper.toEntidad(categoria),codigo);
        return ResponseEntity.ok(categoriaMapper.toDto(existe));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<CategoriaDTO> desactivarCategoria(@PathVariable Long codigo){
        Categoria respuesta = service.desactivarPorCodigo(codigo);
        return ResponseEntity.ok(categoriaMapper.toDto(respuesta));
    }

    @PatchMapping("/{codigo}/activar")
    public ResponseEntity<CategoriaDTO> activarCategoria(@PathVariable Long codigo){
        Categoria respuesta = service.activarPorCodigo(codigo);
        return ResponseEntity.ok(categoriaMapper.toDto(respuesta));
    }
}
