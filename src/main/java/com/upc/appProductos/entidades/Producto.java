package com.upc.appProductos.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_PRODUCTO")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String descripcion;
    private double precio;
    private int stock;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imagen;
    private transient double venta;//precio de venta, como no persistente (no se va a crear en la BD)



    public Producto(Long codigo, String descripcion, double precio, String imagen, int stock) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
    }

    public Producto() {
    }
}
