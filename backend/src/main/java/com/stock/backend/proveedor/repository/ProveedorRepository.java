package com.stock.backend.proveedor.repository;

import com.stock.backend.proveedor.entity.Proveedor;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Proveedor findByNombre(String nombre);
    Optional<Proveedor> findByCodigo(Long codigo);

    Page<Proveedor> findByNombreContainingIgnoreCase(@Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre, Pageable pageable);

    Page<Proveedor> findByActivo(Boolean activo, Pageable pageable);
}
