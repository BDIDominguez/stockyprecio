package com.stock.backend.proveedor.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.repository.ProveedorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public Page<Proveedor> consultarTodos(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return proveedorRepository.findByActivo(activo, pageable);
    }

    public Proveedor crear(Proveedor nuevo) {
        proveedorRepository.findByCodigo(nuevo.getCodigo()).ifPresent(proveedor -> {
            throw new CodigoRepetidoException("Ya existe un proveedor con el codigo: " + nuevo.getCodigo());
        });
        return proveedorRepository.save(nuevo);
    }

    public Long siguienteCodigo() {
        Proveedor proveedor = proveedorRepository.findTopByOrderByCodigoDesc();
        if (proveedor == null || proveedor.getCodigo() == null) {
            return 1L;
        }
        return proveedor.getCodigo() + 1;
    }

    public Proveedor modificar(Long codigo, Proveedor actual) {
        Proveedor existe = proveedorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un proveedor con el codigo: " + codigo));

        if (actual.getCodigo() != null && !Objects.equals(actual.getCodigo(), existe.getCodigo())) {
            proveedorRepository.findByCodigo(actual.getCodigo()).ifPresent(proveedor -> {
                throw new CodigoRepetidoException("Ya existe un proveedor con el codigo: " + actual.getCodigo());
            });
        }

        existe.actualizar(actual);
        return proveedorRepository.save(existe);
    }

    public Optional<Proveedor> buscarPorCodigo(Long codigo) {
        return proveedorRepository.findByCodigo(codigo);
    }

    public Page<Proveedor> buscarPorNombreIgnoreCase(String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return proveedorRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable);
    }

    public void desactivarPorCodigo(Long codigo) {
        Proveedor proveedor = buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado para el codigo: " + codigo));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    public void activarPorCodigo(Long codigo) {
        Proveedor proveedor = buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado para el codigo: " + codigo));
        proveedor.setActivo(true);
        proveedorRepository.save(proveedor);
    }
}
