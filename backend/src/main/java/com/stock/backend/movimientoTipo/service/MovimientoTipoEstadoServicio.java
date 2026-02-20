package com.stock.backend.movimientoTipo.service;

import com.stock.backend.movimientoTipo.entity.MovimientoTipo;
import com.stock.backend.movimientoTipo.exception.MovimientosTipoInexistente;
import com.stock.backend.movimientoTipo.repository.MovimientoTipoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovimientoTipoEstadoServicio {
    private final MovimientoTipoRepository repository;

    public MovimientoTipo desactivar(MovimientoTipo movimiento){
        MovimientoTipo existe = repository.findById(movimiento.getId()).orElseThrow(() -> new MovimientosTipoInexistente("no existe el movimiento solicitado."));
        existe.setActivo(false);
        return repository.save(existe);
    }

    public MovimientoTipo activar(MovimientoTipo movimiento){
        MovimientoTipo existe = repository.findById(movimiento.getId()).orElseThrow(() -> new MovimientosTipoInexistente("no existe el movimiento solicitado."));
        existe.setActivo(true);
        return repository.save(existe);
    }
}
