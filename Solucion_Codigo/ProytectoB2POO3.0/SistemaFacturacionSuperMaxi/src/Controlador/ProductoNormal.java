package Controlador;
import java.io.Serializable;
import java.time.LocalDate;

public class ProductoNormal extends Producto {
    public double precioNormal;

    public ProductoNormal() {
        super();
    }

    public ProductoNormal(String nombre, String categoria, int cantidadStock, double precioNormal) {
        super(nombre, categoria, cantidadStock);
        this.precioNormal = precioNormal;
    }

    @Override
    public double determinarPrecio() {
        return this.precioNormal;
    }
}