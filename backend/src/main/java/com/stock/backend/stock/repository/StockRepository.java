package com.stock.backend.stock.repository;

import com.stock.backend.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findBySucursal(Long sucursal);

    Optional<Stock> findByCodigoAndSucursal(Long codigo, Long sucursal);

    Optional<Stock> findByCodigo(Long codigo);
}
