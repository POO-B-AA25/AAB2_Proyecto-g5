package Controlador;

public class ClienteNormal extends Cliente {

    public int puntosCuponDescuento;

    public ClienteNormal() {
        super();
        this.puntosCuponDescuento = 0;
    }

    public ClienteNormal(String idCliente, String nombre, String correo, String telefono, String direccion, int puntosDescuento) {
        super(idCliente, nombre, correo, telefono, direccion);
        this.puntosCuponDescuento = puntosDescuento;
    }

    @Override
    public double determinarDescuento(double total) {
        // Cada 100 puntos = 5% descuento (máximo 15%)
        double porcentajeDescuento = Math.min((puntosCuponDescuento / 100) * 0.05, 0.15);
        return total * porcentajeDescuento;
    }
}
