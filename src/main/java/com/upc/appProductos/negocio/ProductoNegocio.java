package com.upc.appProductos.negocio;

import com.upc.appProductos.entidades.Producto;
import com.upc.appProductos.repositorio.IProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoNegocio implements IProductoNegocio{

    @Autowired
    IProductoRepositorio iProductoRepositorio;

    public static String convertirTexto(String texto) {
        // Convertir a minúsculas y eliminar espacios
        return texto.toLowerCase().replaceAll("\\s+", "");
    }


    @Override
    public Producto buscar(Long codigo) throws Exception {
        return iProductoRepositorio.findById(codigo).orElseThrow(
                () -> new Exception("No se encontró el producto con el código buscado.")
        );
    }

    @Override
    public List<Producto> listado() {
        return iProductoRepositorio.findAll();//select * from tbl_producto
    }

    @Override
    public Producto actualizar(Producto producto) throws Exception {
        // Verificar si el producto a actualizar ya existe en la base de datos
        Optional<Producto> productoExistente = iProductoRepositorio.findById(producto.getCodigo());

        if (productoExistente.isEmpty()) {
            throw new Exception("El producto que intenta actualizar no existe.");
        }

        // Verificar si ya existe un producto con la misma descripción, excluyendo el actual
        Optional<Producto> productoConDescripcionExistente = iProductoRepositorio.findByDescripcionIgnoreCase(producto.getDescripcion());
        if (productoConDescripcionExistente.isPresent() && !productoConDescripcionExistente.get().getCodigo().equals(producto.getCodigo())) {
            throw new Exception("Ya existe un producto con ese nombre.");
        }

        return iProductoRepositorio.save(producto);
    }


    @Override
    public Producto registrar(Producto producto) throws Exception {
        // Verificar si ya existe un producto con la misma descripción, ignorando mayúsculas y minúsculas
        Optional<Producto> productoExistente = iProductoRepositorio.findByDescripcionIgnoreCase(producto.getDescripcion());
        if (productoExistente.isPresent()) {
            throw new Exception("Ya existe un producto con ese nombre.");
        }
        return iProductoRepositorio.save(producto); // Insertar en la base de datos
    }


    @Override
    public void eliminar(Long codigo) throws Exception {
        Producto producto = buscar(codigo); // Verificar que el producto existe
        iProductoRepositorio.delete(producto);
    }

    @Override
    public double calcularIGV(Producto producto) {
        double igv = 0;
        if(producto != null){
            igv = 0.18 * producto.getPrecio();
        }
        return igv;
    }

    @Override
    public double calcularDescuento(Producto producto) {
        double descuento = 0;
        if (producto != null && producto.getStock() > 20)
        {
            descuento = 0.10 * producto.getPrecio();

        }
        return descuento;
    }

    @Override
    public double calcularPrecioVenta(Producto producto) {
        double precioVenta = producto.getPrecio() + calcularIGV(producto) - calcularDescuento(producto);
        BigDecimal precioRedondeado = new BigDecimal(precioVenta).setScale(2, RoundingMode.HALF_UP);
        return precioRedondeado.doubleValue();
    }

    @Override
    public double calcularPrecioVenta(Long codigo) throws Exception{
        return calcularPrecioVenta(buscar(codigo));
    }

    @Override
    public List<Producto> listadoProductosPorDescripcion(String prefijo) {
        return iProductoRepositorio.obtenerReportePorDescripcion(prefijo);
    }

    @Override
    public List<Producto> listadoTotal() {
        List<Producto> listado;
        listado = iProductoRepositorio.findAll();
        for(Producto producto:listado){
            producto.setVenta(calcularPrecioVenta(producto));
        }
        return listado;
    }

    @Override
    public List<Producto> buscarPorPrecioMenorA(double precio) {
        return iProductoRepositorio.findByPrecioLessThan(precio);
    }

    @Override
    public List<Producto> buscarPorPrecioEnRango(double precioMin, double precioMax) {
        return iProductoRepositorio.findByPrecioBetween(precioMin, precioMax);
    }
}
