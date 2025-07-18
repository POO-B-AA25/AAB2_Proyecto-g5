package modelo;

public class ProductoNormal extends Producto {
    private int tipoProducto;
    private double descuento;

    public ProductoNormal() {
        super();
        this.tipoProducto = 1; // Tipo 1 = Normal
        this.descuento = 0.0;
    }

    public ProductoNormal(String nombre, String categoria, double precioNormal, String descripcion) {
        super(nombre, categoria, precioNormal, descripcion);
        this.tipoProducto = 1;
        this.descuento = 0.0;
    }

    public void aplicarDescuento() {
        // Productos normales no tienen descuento por defecto
        this.descuento = 0.0;
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
            "%-8s | %-25s | %-12s | %8.2f | %4d | %s | NORMAL",
            idProducto, nombre, categoria, getPrecio(), cantidadStock, fechaCaducidad
        );
    }
}