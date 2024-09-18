package com.upc.appProductos.rest;

import com.upc.appProductos.entidades.Producto;
import com.upc.appProductos.negocio.IProductoNegocio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api") //http://localhost:8080/api
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class ProductoRest {
    @Autowired
    private IProductoNegocio productoNegocio;
    //Uso de logback
    Logger logger = LoggerFactory.getLogger(ProductoRest.class);

    @GetMapping("/productos") //http://localhost:8080/api/productos
    public List<Producto> lista(){
        logger.error("Lista de productos");
        return productoNegocio.listado();
    }
    //¿Como invocamos al método que retorna el listado de productos con su precio de venta?
    @GetMapping("/productosTotal")
    public List<Producto> listadoTotal(){
        logger.error("Lista de productos total");
        return productoNegocio.listadoTotal();
    }

    @GetMapping("productosDescripcion/{descripcion}")
    public List<Producto> listadoPorDescripcion(@PathVariable(value = "descripcion")
                                                String descripcion){
        return productoNegocio.listadoProductosPorDescripcion(descripcion);
    }

    @PostMapping("/registrarProducto")
    public Producto registrar(@RequestBody Producto producto){
        try {
            return productoNegocio.registrar(producto);
        } catch (Exception e) {
            logger.error("Error al registrar el producto", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    //Hemos agregado el manejor de excepciones y ocultado el Trace de la excepción (application.properties)
    @GetMapping("/productos/{id}")
    public Producto buscarPorId(@PathVariable(value = "id") Long id){
        try {
            logger.error("Estamos en el try de buscarPorId");
            return productoNegocio.buscar(id);
        } catch (Exception e) {
            logger.error("No existe el producto con el codigo ingresado", e);
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto buscado", e);
        }
    }

    @DeleteMapping("/borrarProducto/{codigo}")
    public void eliminar(@PathVariable Long codigo) {
        try {
            productoNegocio.eliminar(codigo);

        } catch (Exception e) {
            logger.error("Error al eliminar el producto", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/actualizarProducto/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        try {
            Producto productoExistente = productoNegocio.buscar(id);
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoExistente.setPrecio(productoActualizado.getPrecio());
            productoExistente.setStock(productoActualizado.getStock());

            return productoNegocio.actualizar(productoExistente);
        } catch (Exception e) {
            logger.error("Error al actualizar el producto", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/productos/menor-precio")
    public List<Producto> buscarPorPrecioMenorA(@RequestParam double precio) {
        return productoNegocio.buscarPorPrecioMenorA(precio);
    }
    @GetMapping("/productos/precio-en-rango")
    public List<Producto> buscarPorPrecioEnRango(@RequestParam double precioMin, @RequestParam double precioMax) {
        return productoNegocio.buscarPorPrecioEnRango(precioMin, precioMax);
    }


    //¿Métodos para actualizar, eliminar, buscar por precio,stock, rango de precios, mayor a stock?

}
