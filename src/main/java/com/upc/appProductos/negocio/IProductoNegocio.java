package com.upc.appProductos.negocio;

import com.upc.appProductos.entidades.Producto;

import java.util.List;

public interface IProductoNegocio {
    Producto registrar(Producto producto) throws Exception;
    public Producto buscar(Long codigo) throws Exception;
    public List<Producto> listado();
    public Producto actualizar(Producto producto) throws Exception;
    public void eliminar(Long codigo) throws Exception;

    public double calcularIGV(Producto producto);
    public double calcularDescuento(Producto producto);
    public double calcularPrecioVenta(Producto producto);
    public double calcularPrecioVenta(Long codigo) throws Exception;

    public List<Producto> listadoProductosPorDescripcion(String prefijo);
    public List<Producto> listadoTotal();
    List<Producto> buscarPorPrecioMenorA(double precio);
    List<Producto> buscarPorPrecioEnRango(double precioMin, double precioMax);
}

