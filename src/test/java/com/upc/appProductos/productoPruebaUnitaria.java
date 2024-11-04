package com.upc.appProductos;

import com.upc.appProductos.entidades.Producto;
import com.upc.appProductos.negocio.IProductoNegocio;
import com.upc.appProductos.repositorio.IProductoRepositorio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
public class productoPruebaUnitaria {
    @Autowired
    private IProductoNegocio productoNegocio;
    @MockBean //Para simular el acceso a la base de datos
    private IProductoRepositorio productoRepositorio;

    @Test
    void testCalcularPrecioVenta(){
        //De que manera simulada puedo verificar el precio de venta de un producto
        Producto producto = new Producto(1L, "Pepsi", 3, "", 100);
        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto));
        try {
            Assertions.assertEquals(3.24, productoNegocio.calcularPrecioVenta(1L), 0.01);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testListado(){
        when(productoRepositorio.findAll()).thenReturn(
              Stream.of(
                      new Producto(1L, "Pepsi", 3, "", 100),
                      new Producto(2L, "Sprite", 4, "", 10)
              ).collect(Collectors.toList()));
        Assertions.assertEquals(2, productoNegocio.listado().size());
    }
}
