package com.stock.backend.service.categoria;

import com.stock.backend.entity.Categoria;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaFacadeService {
    private final CategoriaBuscarPorNombreService categoriaBuscarPorNombre;
    private final CategoriaConsultarTodosActivosService categoriaConsultarTodosActivos;
    private final CategoriaConsultarTodosInactivosService categoriaConsultarTodosInactivos;
    private final CategoriaConsultarTodosService categoriaConsultarTodos;
    private final CategoriaCrearService categoriaCrear;
    private final CategoriaModificarService categoriaModificar;
    private final CategoriaEliminarService categoriaEliminar;
    private final CategoriaBuscarPorIdService categoriaBuscarPorId;

    public Optional<Categoria> buscarPorNombre(String nombre){
        return categoriaBuscarPorNombre.consultar(nombre);
    }

    public List<Categoria> consultarTodosActivos(){
        return categoriaConsultarTodosActivos.consultar();
    }
    public List<Categoria> consultarTodosInactivos() {
        return  categoriaConsultarTodosInactivos.consultar();
    }
    public List<Categoria> consultarTodos(){
        return categoriaConsultarTodos.consultar();
    }
    public Categoria crear(Categoria datos){
        return categoriaCrear.crear(datos);
    }
    public Categoria modificar(Categoria datos, Long id){
        return categoriaModificar.modificar(datos, id);
    }
    public Categoria eliminar(Long id){
        return categoriaEliminar.eliminar(id);
    }
    public Optional<Categoria> consultarPorId(Long categoria) {
        return categoriaBuscarPorId.buscar(categoria);
    }
}
