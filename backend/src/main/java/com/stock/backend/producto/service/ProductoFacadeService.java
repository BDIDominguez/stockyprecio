package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductoFacadeService {
    private final ProductoConsultarTodosActivosServices productoConsultarTodosActivos;
    private final ProductoConsultarTodosInactivosServices productoConsultarTodosInactivos;
    private final ProductoConsultaPorCodigoService productoConsultaPorCodigo;
    private final ProductoCrearService productoCrear;
    private final ProductoModificarService productoModificar;
    private final ProductoConsultarTodosServices productoConsultarTodos;

    public List<Producto> consultarTodos(){
        return productoConsultarTodos.consultarTodos();
    }

    public List<Producto> consultarTodosActivos(){
        return productoConsultarTodosActivos.consultarTodosActivos();
    }

    public List<Producto> consultarTodosInactivos(){
        return productoConsultarTodosInactivos.consultarTodos();
    }

    public Optional<Producto> consultarPorCodigo(Long codigo){
        return productoConsultaPorCodigo.consultar(codigo);
    }

    public Producto crear(Producto nuevo){
        return productoCrear.nuevo(nuevo);
    }

    public Producto actualizarPorCodigo(Long codigo, Producto modificacion){
        return productoModificar.actualizarPorCodigo(codigo, modificacion);
    }

    public Long siguienteCodigo() {
        return productoConsultaPorCodigo.siguienteCodigo();
    }
}
