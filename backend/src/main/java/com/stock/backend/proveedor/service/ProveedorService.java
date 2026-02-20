package com.stock.backend.proveedor.service;

import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;


    public List<Proveedor> consultarTodos() {
        return proveedorRepository.findAll();
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

    public void actualizarProveedor(Proveedor actual) {
        proveedorRepository.save(actual);
    }

    public Proveedor buscarPorCodigo(Long codigo) {
        return proveedorRepository.findByCodigo(codigo);
    }
}
