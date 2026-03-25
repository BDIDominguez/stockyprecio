package com.stock.backend.impuesto.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.impuesto.entity.Impuesto;
import com.stock.backend.impuesto.repository.ImpuestoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImpuestoService {
    private final ImpuestoRepository repository;

    public List<Impuesto> consultarTodos(Boolean activo) {
        if (activo == null) {
            return repository.findAll();
        }
        return activo ? repository.findAllByActivoTrue() : repository.findAllByActivoFalse();
    }

    public Optional<Impuesto> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public Impuesto consultarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe impuesto con codigo: " + codigo));
    }

    public Impuesto crear(Impuesto datos) {
        repository.findByCodigo(datos.getCodigo()).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe un impuesto con codigo: " + datos.getCodigo());
        });
        return repository.save(datos);
    }

    public Impuesto modificar(String codigo, Impuesto datos) {
        Impuesto existe = consultarPorCodigo(codigo);
        if (datos.getCodigo() != null && !datos.getCodigo().equalsIgnoreCase(existe.getCodigo())) {
            repository.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
                throw new CodigoRepetidoException("Ya existe un impuesto con codigo: " + datos.getCodigo());
            });
        }
        existe.actualizar(datos);
        return repository.save(existe);
    }

    public void desactivar(String codigo) {
        Impuesto existe = consultarPorCodigo(codigo);
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activar(String codigo) {
        Impuesto existe = consultarPorCodigo(codigo);
        existe.setActivo(true);
        repository.save(existe);
    }
}
