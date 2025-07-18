package modelo;

import java.io.Serializable;

public class Cliente implements Serializable {
    public String idCliente;  // Cambiado de 'id' a 'idCliente' para consistencia
    public String nombre;
    public String apellido;
    public String cedula;
    public String telefono;
    public String direccion;
    public String correo;     // Agregado para consistencia con SistemaController

    public Cliente() {}

    // Constructor con 5 parámetros (sin apellido) para compatibilidad con SistemaController
    public Cliente(String idCliente, String nombre, String correo, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.apellido = "";
        this.cedula = "";
    }

    // Constructor completo con 6 parámetros
    public Cliente(String idCliente, String nombre, String apellido, String cedula, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = "";
    }

    // Getters
    public String getId() {
        return idCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    // Setters
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Nombre: %s %s | Cédula: %s | Teléfono: %s | Dirección: %s | Correo: %s", 
                           idCliente, nombre, apellido, cedula, telefono, direccion, correo);
    }
}