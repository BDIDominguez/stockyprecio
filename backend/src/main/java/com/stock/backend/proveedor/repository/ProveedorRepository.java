package com.stock.backend.proveedor.repository;

import com.stock.backend.proveedor.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Proveedor findByNombre(String nombre);
    Proveedor findByCodigo(Long codigo);

}
