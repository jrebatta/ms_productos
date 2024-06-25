package com.upc.appProductos;

import com.upc.appProductos.entidades.Producto;
import com.upc.appProductos.negocio.IProductoNegocio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AppProductosApplicationTests {

	@Autowired
	private IProductoNegocio productoNegocio;
	@Test
	void contextLoads() {
	}

	//(A)
	@Test
	void testRegistro() throws Exception {
		Producto producto = new Producto();
		producto.setDescripcion("Cool");
		producto.setPrecio(4);
		producto.setStock(10);
		productoNegocio.registrar(producto);

		Producto producto2 = new Producto();
		producto2.setDescripcion("Agua Mineral");
		producto2.setPrecio(4);
		producto2.setStock(10);
		productoNegocio.registrar(producto2);
	}

	//(B)
	@Test
	void testBuscar()
	{
		Producto producto = new Producto();
        try {
            producto = productoNegocio.buscar(20L);
			System.out.println("El producto es: " + producto);
        } catch (Exception e) {
			System.out.println(e.getMessage());
        }
    }
	//(D)
	@Test
	void testListado(){
		List<Producto> listado;
		listado = productoNegocio.listado();
		//For para colecciones
		System.out.println("Listado de productos registrados: ");
		for(Producto producto:listado){
			System.out.println(producto);
		}
	}

	//(B)
	@Test
	void testPrecioVenta(){
		double venta = 0;
        try {
            venta = productoNegocio.calcularPrecioVenta(1L);
			System.out.println("El precio de venta es: " + venta);
        } catch (Exception e) {
			System.out.println(e.getMessage());
        }
    }

	//(C)
	@Test
	void testActualizar(){
		Producto producto = new Producto();
		producto.setCodigo(20L);
		producto.setDescripcion("Fanta Naranja");
		producto.setPrecio(5);
		producto.setStock(50);
        try {
            productoNegocio.actualizar(producto);
        } catch (Exception e) {
			System.out.println(e.getMessage());
        }
    }

	//(F)
	@Test
	void testListadoPorDescripcion(){
		List<Producto> listado;
		listado = productoNegocio.listadoProductosPorDescripcion("Pep");
		System.out.println("Listado de productos por nombre");
		for(Producto producto:listado){
			System.out.println(producto);
		}
	}

	//(G)
	@Test
	void testListadoTotal(){
		List<Producto> listado;
		listado = productoNegocio.listadoTotal();
		System.out.println("Listado total de productos con su precio de venta");
		for (Producto producto:listado){
			System.out.println(producto);
		}
	}
}
