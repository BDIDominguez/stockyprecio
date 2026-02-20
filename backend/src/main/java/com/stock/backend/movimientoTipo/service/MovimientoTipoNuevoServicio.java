package com.stock.backend.movimientoTipo.service;

import com.stock.backend.movimientoTipo.entity.MovimientoTipo;
import com.stock.backend.movimientoTipo.repository.MovimientoTipoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovimientoTipoNuevoServicio {
    private final MovimientoTipoRepository repository;

    public MovimientoTipo crearNuevo(MovimientoTipo nuevo){
        MovimientoTipo respuesta = repository.save(nuevo);
        return respuesta;
    }
}
