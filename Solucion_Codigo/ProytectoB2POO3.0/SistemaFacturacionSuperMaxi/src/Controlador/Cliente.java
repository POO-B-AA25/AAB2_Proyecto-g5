package Controlador;
import java.io.Serializable;

public abstract class Cliente implements Serializable {
    public String idCliente;
    public String nombre;
    public String correo;
    public String telefono;
    public String direccion;

    public Cliente() {}

    public Cliente(String idCliente, String nombre, String correo, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Método abstracto que deben implementar las clases hijas
    public abstract double determinarDescuento(double total);

    @Override
    public String toString() {
        return String.format("ID: %s | Nombre: %s | Correo: %s | Telefono: %s | Direccion: %s", 
                           idCliente, nombre, correo, telefono, direccion);
    }
}
