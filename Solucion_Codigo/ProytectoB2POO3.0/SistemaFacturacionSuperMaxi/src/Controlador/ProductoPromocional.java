package Controlador;

public class ProductoPromocional extends Producto {
    public double precioBase;
    public double precioPromocional;

    public ProductoPromocional() {
        super();
    }

    public ProductoPromocional(String nombre, String categoria, int cantidadStock, double precioBase) {
        super(nombre, categoria, cantidadStock);
        this.precioBase = precioBase;
        determinarPrecio(); 
    }

    @Override
    public double determinarPrecio() {
        this.precioPromocional = this.precioBase * 0.8; // 20% descuento
        return this.precioPromocional;
    }
}