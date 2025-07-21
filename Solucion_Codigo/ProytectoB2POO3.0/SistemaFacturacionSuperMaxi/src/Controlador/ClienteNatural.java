package Controlador;

public class ClienteNatural extends Cliente {
    public int puntosCompra;

    public ClienteNatural() {
        super();
    }

    public ClienteNatural(String idCliente, String nombre, String correo, String telefono, String direccion) {
        super(idCliente, nombre, correo, telefono, direccion);
    }

    @Override
    public double determinarDescuento(double total) {

        double descuentoBase = total * 0.03;
        double descuentoPuntos = Math.min((puntosCompra / 50) * 0.01 * total, total * 0.05);
        return descuentoBase + descuentoPuntos;
    }

    public void acumularPuntos(double montoCompra) {
        // 1 punto por cada $10 de compra
        this.puntosCompra += (int)(montoCompra / 10);
    }
}