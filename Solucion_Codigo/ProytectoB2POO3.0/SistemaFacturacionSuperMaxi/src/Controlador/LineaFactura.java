package Controlador;

import java.io.Serializable;

public class LineaFactura implements Serializable {
    public Producto producto;
    public int cantidad;
    public double precioUnitario;
    public double subtotalLinea;

    public LineaFactura() {}

    public LineaFactura(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.subtotalLinea = this.cantidad * this.precioUnitario;
    }

    @Override
    public String toString() {
        String tipoProducto = (producto instanceof ProductoPromocional) ? "PROMOCIONAL" : "NORMAL";
        return String.format(
            "%-30s | %-30s | %4d | %8.2f | %8.2f | %s",
            producto.idProducto, producto.nombre,
            cantidad, precioUnitario, subtotalLinea, tipoProducto
        );
    }
}