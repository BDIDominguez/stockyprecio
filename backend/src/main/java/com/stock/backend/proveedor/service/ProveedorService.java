package com.stock.backend.proveedor.service;

import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.proveedor.dto.ProveedorDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.mapper.ProveedorMapper;
import com.stock.backend.proveedor.repository.ProveedorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public Page<Proveedor> consultarTodos(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return proveedorRepository.findByActivo(activo, pageable);
    }

    public Proveedor consultarPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }

    public Proveedor crearProveedor(Proveedor nuevo) {
        return proveedorRepository.save(nuevo);
    }

    public Optional<Proveedor> consultarPorID(Long id) {
        return proveedorRepository.findById(id);
    }

    public void eliminaProveedor(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long codigo, Proveedor actual) {
        Proveedor existe = proveedorRepository.findByCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("No existe un proveedor con el codigo: " + codigo));
        existe.actualizar(actual);
        return  proveedorRepository.save(existe);
    }

    public Optional<Proveedor> buscarPorCodigo(Long codigo) {
        return proveedorRepository.findByCodigo(codigo);
    }

    public Page<Proveedor> buscarPorNombreIgnoreCase(@Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    public void desactivarPorCodigo(@Valid @Size(min = 1) Long codigo) {
        Proveedor proveedor = buscarPorCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("Proveedor no encontrado para el codigo: " + codigo));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    public void activarPorCodigo(Long codigo) {
        Proveedor proveedor = buscarPorCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("Proveedor no encontrado para el codigo: " + codigo));
        proveedor.setActivo(true);
        proveedorRepository.save(proveedor);
    }
}
