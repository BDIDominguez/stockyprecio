package com.stock.backend.repository;

import com.stock.backend.entity.MovimientoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovimientoTipoRepository extends JpaRepository<MovimientoTipo, Long> {
    Optional<MovimientoTipo> findBySiglas(String siglas);
}
