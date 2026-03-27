package com.stock.backend.listaPrecio.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import com.stock.backend.listaPrecio.repository.ListaPrecioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ListaPrecioService {
    private final ListaPrecioRepository repository;

    public Page<ListaPrecio> consultarTodos(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByActivo(activo, pageable);
    }

    public Page<ListaPrecio> buscarPorNombreIgnoreCase(String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable);
    }

    public Optional<ListaPrecio> buscarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo);
    }

    public ListaPrecio consultarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe lista de precio con codigo: " + codigo));
    }

    public ListaPrecio crear(ListaPrecio datos) {
        repository.findByCodigo(datos.getCodigo()).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe una lista de precio con codigo: " + datos.getCodigo());
        });
        return repository.save(datos);
    }

    public ListaPrecio modificar(Long codigo, ListaPrecio datos) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        if (datos.getCodigo() != null && !datos.getCodigo().equals(existe.getCodigo())) {
            repository.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
                throw new CodigoRepetidoException("Ya existe una lista de precio con codigo: " + datos.getCodigo());
            });
        }
        existe.actualizar(datos);
        return repository.save(existe);
    }

    public void desactivar(Long codigo) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activar(Long codigo) {
        ListaPrecio existe = consultarPorCodigo(codigo);
        existe.setActivo(true);
        repository.save(existe);
    }

    public Long siguienteCodigo() {
        ListaPrecio listaPrecio = repository.findTopByOrderByCodigoDesc();
        if (listaPrecio == null || listaPrecio.getCodigo() == null) {
            return 1L;
        }
        return listaPrecio.getCodigo() + 1;
    }
}
