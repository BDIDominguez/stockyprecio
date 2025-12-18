package com.stock.backend.service.movimientotipo;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.repository.MovimientoTipoRepository;
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
