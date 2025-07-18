package modelo;

import java.io.Serializable;


public class LineaFactura implements Serializable {
    public Producto producto;
    public int cantidad;
    public double precioUnitario;
    public double subtotalLinea;

    public LineaFactura() {}

    public LineaFactura(Producto producto, int cantidad, double precioUnitario) {
        this.producto       = producto;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotalLinea() {
        return subtotalLinea;
    }

    
    public void calcularSubtotal() {
        this.subtotalLinea = this.cantidad * this.precioUnitario;
    }

    @Override
    public String toString() {
        return String.format(
            "%-30s | %-30s | %4d | %8.2f | %8.2f",
            producto.idProducto, producto.nombre,
            cantidad, precioUnitario, subtotalLinea
        );
    }
}
