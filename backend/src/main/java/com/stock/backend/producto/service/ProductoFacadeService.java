package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductoFacadeService {
    private ProductoConsultarTodosActivosServices productoConsultarTodos;
    private ProductoConsultarTodosInactivosServices productoConsultarTodosInactivos;
    private ProductoConsultarPorIdService productoConsultarPorId;
    private ProductoConsultaPorCodigoService productoConsultaPorCodigo;
    private ProductoCrearService productoCrear;
    private ProductoModificarService productoModificar;

    public List<Producto> consultarTodosActivos(){
        return productoConsultarTodos.consultarTodos();
    }

    public List<Producto> consultarTodosInactivos(){
        return productoConsultarTodosInactivos.consultarTodos();
    }

    public Optional<Producto> consultarPorId(Long id){
        return productoConsultarPorId.consultar(id);
    }

    public Optional<Producto> consultarPorCodigo(Long codigo){
        return productoConsultaPorCodigo.consultar(codigo);
    }

    public Producto crear(Producto nuevo){
        return productoCrear.nuevo(nuevo);
    }

    public Producto actualizar(Long id, Producto modificacion){
        return productoModificar.actualizar(id, modificacion);
    }
}
