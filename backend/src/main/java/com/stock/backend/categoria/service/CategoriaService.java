package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRepository;
import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repository;

    public Page<Categoria> consultarTodos(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByActivo(activo, pageable);
    }

    public Categoria crear(Categoria datos) {
        repository.findByCodigo(datos.getCodigo()).ifPresent(c -> {
            throw new CodigoRepetidoException("Ya existe ese codigo en la tabla " + datos.getCodigo());
        });
        return repository.save(datos);
    }

    public Categoria modificar(Categoria datos, Long codigo) {
        Categoria existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una Categoria con Codigo: " + codigo));
        existe.actualizar(datos);
        return repository.save(existe);
    }

    public Categoria desactivarPorCodigo(Long codigo) {
        Categoria existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una Categoria con ese Codigo: " + codigo));
        existe.setActivo(false);
        return repository.save(existe);
    }

    public Optional<Categoria> buscarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo);
    }

    public Categoria activarPorCodigo(Long codigo) {
        Categoria existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una Categoria con ese Codigo: " + codigo));
        existe.setActivo(true);
        return repository.save(existe);
    }

    public Page<Categoria> buscarPorNombreIgnoreCase(String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable);
    }

    public Long siguienteCodigo() {
        Categoria categoria = repository.findTopByOrderByCodigoDesc();
        if (categoria == null || categoria.getCodigo() == null) {
            return 1L;
        }
        return categoria.getCodigo() + 1;
    }
}
