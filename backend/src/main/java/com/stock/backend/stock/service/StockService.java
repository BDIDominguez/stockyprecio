package com.stock.backend.stock.service;

import com.stock.backend.common.exception.OperacionNoValidaExeption;
import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.exception.StockExistenteException;
import com.stock.backend.stock.exception.StockInexistenteException;
import com.stock.backend.stock.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StockService {

    private final StockRepository repository;

    public Optional<Stock> buscarPorCodigoYSucursal(Long codigo, Long sucursal) {
        return repository.findByCodigoAndSucursal(codigo, sucursal);
    }

    public Stock consultarExistente(Long codigo, Long sucursal) {
        return repository.findByCodigoAndSucursal(codigo, sucursal)
                .orElseThrow(() -> new StockInexistenteException(
                        "No existe stock para el codigo " + codigo + " en la sucursal " + sucursal));
    }

    public Stock guardar(Stock stock) {
        validarDatosBase(stock.getCodigo(), stock.getSucursal());

        repository.findByCodigoAndSucursal(stock.getCodigo(), stock.getSucursal()).ifPresent(existe -> {
            throw new StockExistenteException(
                    "Ya existe stock para el codigo " + stock.getCodigo() + " en la sucursal " + stock.getSucursal());
        });

        if (stock.getCantidad() == null) {
            stock.setCantidad(0.00);
        }
        if (stock.getReserva() == null) {
            stock.setReserva(0.00);
        }
        if (stock.getReserva() < 0) {
            throw new OperacionNoValidaExeption("La reserva no puede ser negativa.");
        }

        return repository.save(stock);
    }

    public Stock crearInicial(Long codigo, Long sucursal) {
        return guardar(Stock.builder()
                .codigo(codigo)
                .sucursal(sucursal)
                .cantidad(0.00)
                .reserva(0.00)
                .build());
    }

    public Stock sumar(Long codigo, Long sucursal, Double cantidad) {
        validarMovimiento(cantidad);
        Stock actual = consultarExistente(codigo, sucursal);
        actual.sumar(cantidad);
        return repository.save(actual);
    }

    public Stock restar(Long codigo, Long sucursal, Double cantidad) {
        validarMovimiento(cantidad);
        Stock actual = consultarExistente(codigo, sucursal);
        actual.restar(cantidad);
        return repository.save(actual);
    }

    public Stock actualizarReserva(Long codigo, Long sucursal, Double reserva) {
        if (reserva == null) {
            throw new OperacionNoValidaExeption("La reserva es obligatoria.");
        }
        Stock actual = consultarExistente(codigo, sucursal);
        if (reserva < 0) {
            throw new OperacionNoValidaExeption("La reserva no puede ser negativa.");
        }
        actual.actualizarReserva(reserva);
        return repository.save(actual);
    }

    private void validarDatosBase(Long codigo, Long sucursal) {
        if (codigo == null || codigo <= 0) {
            throw new OperacionNoValidaExeption("El codigo debe ser mayor a cero.");
        }
        if (sucursal == null || sucursal <= 0) {
            throw new OperacionNoValidaExeption("La sucursal debe ser mayor a cero.");
        }
    }

    private void validarMovimiento(Double cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new OperacionNoValidaExeption("La cantidad del movimiento debe ser mayor a cero.");
        }
    }
}
