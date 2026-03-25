package com.stock.backend.listaPrecio.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import com.stock.backend.listaPrecio.repository.ListaPrecioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ListaPrecioService {
    private final ListaPrecioRepository repository;

    public List<ListaPrecio> consultarTodos(Boolean activo) {
        if (activo == null) {
            return repository.findAll();
        }
        return activo ? repository.findAllByActivoTrue() : repository.findAllByActivoFalse();
    }

    public Optional<ListaPrecio> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public ListaPrecio consultarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe lista de precio con codigo: " + codigo));
    }

    public ListaPrecio crear(ListaPrecio datos) {
        repository.findByCodigo(datos.getCodigo()).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe una lista de precio con codigo: " + datos.getCodigo());
        });
        return repository.save(datos);
    }

    public ListaPrecio modificar(String codigo, ListaPrecio datos) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        if (datos.getCodigo() != null && !datos.getCodigo().equalsIgnoreCase(existe.getCodigo())) {
            repository.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
                throw new CodigoRepetidoException("Ya existe una lista de precio con codigo: " + datos.getCodigo());
            });
        }
        existe.actualizar(datos);
        return repository.save(existe);
    }

    public void desactivar(String codigo) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activar(String codigo) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        existe.setActivo(true);
        repository.save(existe);
    }
}
