package com.stock.backend.sucursal.repository;

import com.stock.backend.sucursal.entity.Sucursal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    Optional<Sucursal> findByCodigo(Long codigo);
    Page<Sucursal> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);
    Page<Sucursal> findByActivo(Boolean activo, Pageable pageable);
    List<Sucursal> findAllByActivoTrue();
    Sucursal findTopByOrderByCodigoDesc();
}
