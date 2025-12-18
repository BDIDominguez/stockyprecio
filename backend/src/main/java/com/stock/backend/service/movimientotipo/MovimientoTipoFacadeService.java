package com.stock.backend.service.movimientotipo;

import com.stock.backend.entity.MovimientoTipo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovimientoTipoFacadeService {
    private final MovimientoTipoConsultaPorSiglasService consultaPorSiglasService;
    private final MovimientoTipoConsultaTodosService consultaTodosService;
    private final MovimientoTipoNuevoServicio nuevoServicio;
    private final MovimientoTipoEstadoServicio estadoServicio;

    public MovimientoTipo consultarPorSiglas(String siglas){
        return consultaPorSiglasService.consulta(siglas);
    }

    public List<MovimientoTipo> consultaTodos(){
        return consultaTodosService.consultarTodos();
    }

    public MovimientoTipo nuevo(MovimientoTipo nuevo){
        return nuevoServicio.crearNuevo(nuevo);
    }

    public MovimientoTipo desactivar(MovimientoTipo movimiento) { return estadoServicio.desactivar(movimiento);  }

    public MovimientoTipo activar(MovimientoTipo movimiento) { return  estadoServicio.activar(movimiento); }
}
