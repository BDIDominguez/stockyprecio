package com.stock.backend.sucursal.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.repository.SucursalRepository;
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
public class SucursalService {

    private final SucursalRepository repository;

    public Page<Sucursal> consultarTodas(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByActivo(activo, pageable);
    }

    public Optional<Sucursal> buscarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo);
    }

    public Page<Sucursal> buscarPorNombreIgnoreCase(String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable);
    }

    public Sucursal crear(Sucursal nueva) {
        repository.findByCodigo(nueva.getCodigo()).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe una sucursal con el codigo: " + nueva.getCodigo());
        });
        return repository.save(nueva);
    }

    public Sucursal modificar(Long codigo, Sucursal actualizacion) {
        Sucursal existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con el codigo: " + codigo));

        if (actualizacion.getCodigo() != null && !Objects.equals(actualizacion.getCodigo(), existe.getCodigo())) {
            repository.findByCodigo(actualizacion.getCodigo()).ifPresent(otra -> {
                throw new CodigoRepetidoException("Ya existe una sucursal con el codigo: " + actualizacion.getCodigo());
            });
        }

        existe.actualizar(actualizacion);
        return repository.save(existe);
    }

    public void desactivarPorCodigo(Long codigo) {
        Sucursal existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con el codigo: " + codigo));
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activarPorCodigo(Long codigo) {
        Sucursal existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con el codigo: " + codigo));
        existe.setActivo(true);
        repository.save(existe);
    }

    public Long siguienteCodigo() {
        Sucursal sucursal = repository.findTopByOrderByCodigoDesc();
        if (sucursal == null || sucursal.getCodigo() == null) {
            return 1L;
        }
        return sucursal.getCodigo() + 1;
    }
}
