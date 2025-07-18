package modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import vista.*;

public class Factura implements Serializable {
    
    private String idFactura;
    private Date fechaFactura;
    private Object cliente;
    private String metodoPago;
    private Object sistemaDeFacturacion;
    private double subtotal;
    private double total;
    private double impuesto;
    

    public LocalDate fechaEmision;
    public Cliente clienteObj;
    public ArrayList<LineaFactura> lineas;
    public double iva;
    
    private static int contadorFacturas = 1;
    
    public Factura() {
        this.idFactura = "FAC-" + String.format("%04d", contadorFacturas++);
        this.fechaFactura = new Date();
        this.cliente = new Object();
        this.metodoPago = "EFECTIVO";
        this.sistemaDeFacturacion = new SistemaDeFacturacion();
        this.subtotal = 0.0;
        this.total = 0.0;
        this.impuesto = 0.0;
        
        // Mantener compatibilidad
        this.fechaEmision = LocalDate.now();
        this.lineas = new ArrayList<>();
        this.iva = 0.0;
    }
    
    public void generarFactura(Date fecha, Object cliente, String metodoPago, Object sistemaDeFacturacion) {
        this.fechaFactura = fecha;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.sistemaDeFacturacion = sistemaDeFacturacion;
        
        // Mantener compatibilidad
        if (cliente instanceof Cliente) {
            this.clienteObj = (Cliente) cliente;
        }
    }

    public void setCliente(Cliente cliente) {
        this.clienteObj = cliente;
        this.cliente = cliente;
    }
    
    public void agregarLinea(LineaFactura linea) {
        this.lineas.add(linea);
    }
    
    public double calcularSubtotal() {
        this.subtotal = 0.0;
        for (LineaFactura linea : lineas) {
            this.subtotal += linea.getSubtotalLinea();
        }
        return this.subtotal;
    }
    
    public double calcularImpuesto() {
        this.impuesto = this.subtotal * 0.15; // 15% IVA
        this.iva = this.impuesto; // Mantener compatibilidad
        return this.impuesto;
    }
    
    public void calcularTotal() {
        this.total = this.subtotal + this.impuesto;
    }
    
    // Método original mantenido para compatibilidad
    public void calcularIva(double porcentajeIva) {
        this.iva = this.subtotal * porcentajeIva;
        this.impuesto = this.iva;
    }
    
    // Método original mantenido para compatibilidad
    public void calcularTotal(Deducible deducible) {
        double totalBruto = this.subtotal + this.iva;
        // Aplicar deducción si es necesario
        this.total = totalBruto;
    }
    
    public void obtenerFactura() {
        System.out.println(this.toString());
    }

    public void guardarEnArchivoBinario(String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(nombreArchivo + ".ser"))) {
            oos.writeObject(this);
            System.out.println("Factura serializada en: " + nombreArchivo + ".ser");
        } catch (IOException e) {
            System.err.println("Error al serializar factura: " + e.getMessage());
        }
    }
    
    // MÉTODO ESTÁTICO PARA DESERIALIZAR DESDE .ser
    public static Factura cargarDesdeArchivoBinario(String nombreArchivo) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(nombreArchivo + ".ser"))) {
            Factura factura = (Factura) ois.readObject();
            System.out.println("Factura deserializada desde: " + nombreArchivo + ".ser");
            return factura;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar factura: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================\n");
        sb.append("              FACTURA\n");
        sb.append("==========================================\n");
        sb.append("ID Factura: ").append(idFactura).append("\n");
        sb.append("Fecha: ").append(fechaEmision != null ? fechaEmision.toString() : fechaFactura.toString()).append("\n");
        sb.append("Método de Pago: ").append(metodoPago).append("\n");
        
        if (clienteObj != null) {
            sb.append("\n--- DATOS DEL CLIENTE ---\n");
            sb.append("ID: ").append(clienteObj.getId()).append("\n");
            sb.append("Nombre: ").append(clienteObj.getNombre()).append("\n");
            sb.append("Dirección: ").append(clienteObj.getDireccion()).append("\n");
        }
        
        sb.append("\n--- DETALLE DE PRODUCTOS ---\n");
        sb.append("PRODUCTO                CANT   P.UNIT   SUBTOTAL\n");
        sb.append("------------------------------------------\n");
        
        for (LineaFactura linea : lineas) {
            sb.append(String.format("%-20s %4d   %6.2f   %8.2f\n",
                linea.getProducto().getNombre(),
                linea.getCantidad(),
                linea.getPrecioUnitario(),
                linea.getSubtotalLinea()));
        }
        
        sb.append("------------------------------------------\n");
        sb.append(String.format("SUBTOTAL:                        %8.2f\n", subtotal));
        sb.append(String.format("IVA (15%%):                       %8.2f\n", impuesto));
        sb.append(String.format("TOTAL:                           %8.2f\n", total));
        sb.append("==========================================\n");
        
        return sb.toString();
    }
    
    // Getters y Setters
    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Object getCliente() {
        return cliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Object getSistemaDeFacturacion() {
        return sistemaDeFacturacion;
    }

    public void setSistemaDeFacturacion(Object sistemaDeFacturacion) {
        this.sistemaDeFacturacion = sistemaDeFacturacion;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }
}