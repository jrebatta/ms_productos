package com.upc.appProductos.repositorio;

import com.upc.appProductos.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProductoRepositorio extends JpaRepository<Producto, Long> {

    public List<Producto> findByDescripcionStartingWith(String prefix); //Spring Data

    //Hibernate
    @Query("SELECT p FROM Producto p WHERE p.descripcion like %:prefijo%")
    public List<Producto> obtenerReportePorDescripcion(@Param("prefijo") String prefijo);

    // Nuevo método para encontrar productos por descripción exacta ignorando mayúsculas y minúsculas
    @Query("SELECT p FROM Producto p WHERE LOWER(p.descripcion) = LOWER(:descripcion)")
    Optional<Producto> findByDescripcionIgnoreCase(@Param("descripcion") String descripcion);

    List<Producto> findByPrecioLessThan(double precio);

    List<Producto> findByPrecioBetween(double precioMin, double precioMax);
}
