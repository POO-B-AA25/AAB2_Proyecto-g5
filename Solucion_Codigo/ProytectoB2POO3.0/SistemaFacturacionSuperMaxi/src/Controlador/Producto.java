package Controlador;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Random;

public abstract class Producto implements Serializable {
    public String idProducto;
    public String nombre;
    public String categoria;
    public int cantidadStock;
    public LocalDate fechaCaducidad;

    public Producto() {}

    public Producto(String nombre, String categoria, int cantidadStock) {
        this.idProducto = UUID.randomUUID().toString().substring(0, 8);
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidadStock = cantidadStock;

        Random rnd = new Random();
        int dias = rnd.nextInt(30) + 1;
        this.fechaCaducidad = LocalDate.now().plusDays(dias);
    }

    // Métodos concretos
    public String getIdProducto() { return idProducto; }
    public int getCantidad() { return cantidadStock; }
    public String getCategoria() { return categoria; }

    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    public void setCantidad(int cantidad) { this.cantidadStock = cantidad; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean disminuirStock(int cantidad) {
        if (cantidad <= this.cantidadStock) {
            this.cantidadStock -= cantidad;
            return true;
        }
        return false;
    }

    public void aumentarStock(int cantidad) {
        this.cantidadStock += cantidad;
    }


    public abstract double determinarPrecio();

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %-12s | %8.2f | %4d | %10s",
            idProducto, nombre, categoria, determinarPrecio(), cantidadStock, fechaCaducidad.toString()
        );
    }
}