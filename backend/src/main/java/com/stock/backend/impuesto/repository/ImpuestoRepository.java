package com.stock.backend.impuesto.repository;

import com.stock.backend.impuesto.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
    Optional<Impuesto> findByCodigo(String codigo);
    List<Impuesto> findAllByActivoTrue();
    List<Impuesto> findAllByActivoFalse();
}
