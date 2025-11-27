package com.stock.backend.service.tipomovimiento;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.repository.MovimientoTipoRepository;
import com.stock.backend.service.MovimientoTipoService;
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
