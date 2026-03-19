package com.stock.backend.proveedor.repository;

import com.stock.backend.proveedor.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByCodigo(Long codigo);
    Page<Proveedor> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);
    Page<Proveedor> findByActivo(Boolean activo, Pageable pageable);
    Proveedor findTopByOrderByCodigoDesc();
}
