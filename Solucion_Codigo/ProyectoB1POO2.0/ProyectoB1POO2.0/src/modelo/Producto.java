package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Producto implements Serializable {
    public String idProducto;
    public String nombre;
    public int cantidadStock;      // Cambiado de cantidadDisponible a cantidadStock
    public String categoria;
    public LocalDate fechaCaducidad;
    public double precioNormal;    // Cambiado de precio a precioNormal
    public String descripcion;

    public Producto() {
        this.idProducto = UUID.randomUUID().toString().substring(0, 8);
        this.fechaCaducidad = LocalDate.now().plusMonths(12);
        this.cantidadStock = 0;
        this.precioNormal = 0.0;
        this.descripcion = "";
    }

    public Producto(String nombre, String categoria, double precioNormal, String descripcion) {
        this();
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioNormal = precioNormal;
        this.descripcion = descripcion;
    }

    public Producto(String nombre, String categoria, double precioNormal, int cantidadStock) {
        this();
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioNormal = precioNormal;
        this.cantidadStock = cantidadStock;
    }

    // Método getDatos según UML
    public void getDatos() {
        System.out.println("ID: " + idProducto);
        System.out.println("Nombre: " + nombre);
        System.out.println("Cantidad: " + cantidadStock);
        System.out.println("Categoría: " + categoria);
        System.out.println("Fecha Caducidad: " + fechaCaducidad);
        System.out.println("Precio: " + precioNormal);
        System.out.println("Descripción: " + descripcion);
    }

    // Setters
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public void setPrecioNormal(double precioNormal) {
        this.precioNormal = precioNormal;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters
    public String getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public double getPrecioNormal() {
        return precioNormal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Método eliminarProducto según UML
    public boolean eliminarProducto(String idProducto) {
        return this.idProducto.equals(idProducto);
    }

    // Método actualizarProducto según UML
    public String actualizarProducto() {
        return "Producto " + idProducto + " actualizado";
    }

    // Métodos de negocio
    public boolean hayStock(int cantidad) {
        return this.cantidadStock >= cantidad;
    }

    public void disminuirStock(int cantidad) {
        if (hayStock(cantidad)) {
            this.cantidadStock -= cantidad;
        }
    }

    public void aumentarStock(int cantidad) {
        this.cantidadStock += cantidad;
    }

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %-12s | %8.2f | %4d | %s",
            idProducto, nombre, categoria, precioNormal, cantidadStock, fechaCaducidad
        );
    }
}