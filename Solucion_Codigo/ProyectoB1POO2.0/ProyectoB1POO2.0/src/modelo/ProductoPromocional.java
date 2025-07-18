package modelo;

public class ProductoPromocional extends Producto {
    private int tipoProducto;
    private double descuento;

    public ProductoPromocional() {
        super();
        this.tipoProducto = 2; 
        this.descuento = 0.15; 
    }

    public ProductoPromocional(String nombre, String categoria, double precio, String descripcion) {
        super(nombre, categoria, precio, descripcion);
        this.tipoProducto = 2;
        this.descuento = 0.15;
    }

    public void aplicarDescuento() {
  
        if (this.descuento == 0.0) {
            this.descuento = 0.15;
        }
    }

    // Getters y Setters
    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    
    public double getPrecio() {
        return precioNormal * (1 - descuento);
    }

    public double getPrecioOriginal() {
        return precioNormal;
    }

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %-12s | %8.2f | %4d | %s | PROMOCIONAL (-%.0f%%)",
            idProducto, nombre, categoria, getPrecio(), cantidadStock, fechaCaducidad, descuento * 100
        );
    }
}