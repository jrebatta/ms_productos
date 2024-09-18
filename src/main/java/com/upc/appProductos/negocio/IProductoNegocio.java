package com.upc.appProductos.negocio;

import com.upc.appProductos.entidades.Producto;

import java.util.List;

public interface IProductoNegocio {
    Producto registrar(Producto producto) throws Exception;
    Producto buscar(Long codigo) throws Exception;
    List<Producto> listado();
    Producto actualizar(Producto producto) throws Exception;
    void eliminar(Long codigo) throws Exception;

    double calcularIGV(Producto producto);
    double calcularDescuento(Producto producto);
    double calcularPrecioVenta(Producto producto);
    double calcularPrecioVenta(Long codigo) throws Exception;

    List<Producto> listadoProductosPorDescripcion(String prefijo);
    List<Producto> listadoTotal();
    List<Producto> buscarPorPrecioMenorA(double precio);
    List<Producto> buscarPorPrecioEnRango(double precioMin, double precioMax);
}

