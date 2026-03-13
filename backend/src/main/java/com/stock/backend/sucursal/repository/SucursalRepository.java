package com.stock.backend.sucursal.repository;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Optional<Sucursal> findByCodigo(Long codigo);

    Page<Sucursal> findByNombreContainingIgnoreCase(@Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre, Pageable pageable);

    Page<Sucursal> findByActivo(Boolean activo, Pageable pageable);

}
