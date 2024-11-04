package com.upc.appProductos.negocio;

import com.upc.appProductos.entidades.Producto;
import com.upc.appProductos.repositorio.IProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

@Service
public class ProductoNegocio implements IProductoNegocio{

    @Autowired
    IProductoRepositorio iProductoRepositorio;

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
        // Verificar si el producto existe
        Optional<Producto> productoExistente = iProductoRepositorio.findById(producto.getCodigo());
        if (productoExistente.isEmpty()) {
            throw new Exception("El producto que intenta actualizar no existe.");
        }

        // Verificar si existe otro producto con la misma descripción
        Optional<Producto> productoConDescripcionExistente = iProductoRepositorio.findByDescripcionIgnoreCase(producto.getDescripcion());
        if (productoConDescripcionExistente.isPresent() && !productoConDescripcionExistente.get().getCodigo().equals(producto.getCodigo())) {
            throw new Exception("Ya existe un producto con ese nombre.");
        }

        // Convertir la imagen a Base64 si no está vacía
        if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
            producto.setImagen(encodeImagen(producto.getImagen())); // Usa el método encodeImagen
        }

        return iProductoRepositorio.save(producto);
    }



    @Override
    public Producto registrar(Producto producto) throws Exception {
        // Verificar si ya existe un producto con la misma descripción
        Optional<Producto> productoExistente = iProductoRepositorio.findByDescripcionIgnoreCase(producto.getDescripcion());
        if (productoExistente.isPresent()) {
            throw new Exception("Ya existe un producto con ese nombre.");
        }

        // Convertir la imagen a Base64 si no está vacía
        if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
            producto.setImagen(encodeImagen(producto.getImagen())); // Usa el método encodeImagen
        }

        return iProductoRepositorio.save(producto); // Guardar en la base de datos
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
        List<Producto> listado = iProductoRepositorio.findAll();
        for (Producto producto : listado) {
            producto.setVenta(calcularPrecioVenta(producto));
            // Decodificar la imagen en Base64 antes de enviarla al frontend
            if (producto.getImagen() != null) {
                producto.setImagen(decodeImagen(producto.getImagen())); // Decodificar la imagen
            }
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

    public static String encodeImagen(String imagen) {
        // Codificar la imagen a Base64
        return Base64.getEncoder().encodeToString(imagen.getBytes());
    }

    public static String decodeImagen(String imagenBase64) {
        // Decodificar desde Base64 a texto
        return new String(Base64.getDecoder().decode(imagenBase64));
    }
}
