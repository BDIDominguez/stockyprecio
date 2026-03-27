package com.stock.backend.componenteCosto.repository;

import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponenteCostoRepository extends JpaRepository<ComponenteCosto, Long> {
    Page<ComponenteCosto> findByActivo(Boolean activo, Pageable pageable);
    Page<ComponenteCosto> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);
    Optional<ComponenteCosto> findByCodigo(Long codigo);
    List<ComponenteCosto> findAllByActivoTrueOrderByEtapaAplicacionAscCodigoAsc();
    ComponenteCosto findTopByOrderByCodigoDesc();
}
