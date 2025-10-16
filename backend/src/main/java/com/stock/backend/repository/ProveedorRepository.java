package com.stock.backend.repository;

import com.stock.backend.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Proveedor findByNombre(String nombre);
    Proveedor findByCodigo(Long codigo);

}
