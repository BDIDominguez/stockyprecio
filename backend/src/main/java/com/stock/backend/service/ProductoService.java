package com.stock.backend.service;

import com.stock.backend.entity.Producto;
import com.stock.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    //@Autowired
    //private StockService stocks;

    @Autowired
    private ProveedorService proveedores;


    /* este nunca debe llamar al service ProductoNUevoService debo de crear un orquestador que use stock y producto!! por temas de diseño correcto.
    public Producto nuevoProducto(Producto producto, Long sucursal) {
        // se crea el nuevo stock, ya que al ser un nuevo producto no existe para el mismo ese Stock  pero por las dudas consultamos que el mismo no exista.
        Stock existe = stocks.  buscarPorCodigoySucursal(producto.getCodigo(), sucursal);

        Stock nuevo = new Stock(null, producto.getCodigo(),sucursal,0.00,producto.getStockMinimo());
        Stock respuesta = stocks.crearStockNuevo(nuevo);
        if (respuesta.getId() == null){
            return null;
        }
        return productoRepository.save(producto);
    } */

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorCodigo(Long codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /*
    public ProductoCompletoDTO productoCompletoPorCodigo(Long codigo, Long sucursal){
        System.out.println("CONSULTANDO SI EXISTE EL PRODUCTO CON CODIGO " + codigo);
        Optional<Producto> prod = productoRepository.findByCodigo(codigo);
        if (prod.isEmpty()){
            Producto producto = prod.get();
            System.out.println("SE ENCONTRO EL CODIGO " + producto);
            Proveedor proveedor = proveedores.buscarPorCodigo(producto.getProveedor());
            System.out.println("SE ENCONTRO EL PROVEEDOR " + proveedor);
            Stock stock = stocks.buscarPorCodigoySucursal(producto.getCodigo(), sucursal);
            System.out.println("SE ENCONTRO EL STOCK " + stock);
            Categoria categoria = categorias.buscarPorCodigo(producto.getCategoria());
            System.out.println("SE ENCONTROL CATEGORIA " + categoria);
            System.out.println("REGRESANDO EL DTO COMPLETO");
            return new ProductoCompletoDTO(producto, categoria, proveedor, stock);

        }
        System.out.println("NO SE ENCONTRO PRODUCTO REGRESANDO NULL");
        return null;
    } */
}
