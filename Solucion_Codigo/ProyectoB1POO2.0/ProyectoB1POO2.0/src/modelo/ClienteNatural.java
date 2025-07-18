package modelo;

public class ClienteNatural extends Cliente {
    private int puntosPorCompras;
    
    public ClienteNatural() {
        super();
        this.puntosPorCompras = 0;
    }

    public ClienteNatural(String id, String nombre, String apellido, String cedula, String telefono, String direccion) {
        super(id, nombre, apellido, cedula, telefono, direccion);
        this.puntosPorCompras = 0;
    }

    public void ganarPuntos() {
        this.puntosPorCompras += 15; // Ejemplo: Cliente natural gana más puntos
    }

    public int getPuntosPorCompras() {
        return puntosPorCompras;
    }

    public void setPuntosPorCompras(int puntosPorCompras) {
        this.puntosPorCompras = puntosPorCompras;
    }

    @Override
    public String toString() {
        return String.format("%s | Puntos: %d | Tipo: NATURAL", 
                           super.toString(), puntosPorCompras);
    }
}