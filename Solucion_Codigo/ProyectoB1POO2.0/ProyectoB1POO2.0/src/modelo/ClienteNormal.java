package modelo;

public class ClienteNormal extends Cliente {
    private int puntosPorCompras;
    
    public ClienteNormal() {
        super();
        this.puntosPorCompras = 0;
    }

    public ClienteNormal(String id, String nombre, String apellido, String cedula, String telefono, String direccion) {
        super(id, nombre, apellido, cedula, telefono, direccion);
        this.puntosPorCompras = 0;
    }

    public void ganarPuntos() {
        this.puntosPorCompras += 10; // Ejemplo: ganar 10 puntos por compra
    }

    public int getPuntosPorCompras() {
        return puntosPorCompras;
    }

    public void setPuntosPorCompras(int puntosPorCompras) {
        this.puntosPorCompras = puntosPorCompras;
    }

    @Override
    public String toString() {
        return String.format("%s | Puntos: %d | Tipo: NORMAL", 
                           super.toString(), puntosPorCompras);
    }
}