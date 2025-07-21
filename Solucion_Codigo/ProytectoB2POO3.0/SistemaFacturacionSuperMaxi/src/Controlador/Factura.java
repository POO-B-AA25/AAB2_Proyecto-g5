
package Controlador;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Factura implements Serializable {
    
    public String idFactura;
    public LocalDate fechaEmision;
    public Cliente cliente;
    public ArrayList<LineaFactura> lineas;
    public double subtotal;
    public double iva;
    public double total;
    
    private static int contadorFacturas = 1;
    
    public Factura() {
        this.idFactura = "FAC-" + String.format("%04d", contadorFacturas++);
        this.fechaEmision = LocalDate.now();
        this.lineas = new ArrayList<>();
        this.subtotal = 0.0;
        this.iva = 0.0;
        this.total = 0.0;
    }

    public Factura(LocalDate fechaEmision, String idFactura, ArrayList<LineaFactura> lineas, 
                   double subtotal, double total, double iva) {
        this.fechaEmision = fechaEmision;
        this.idFactura = idFactura;
        this.lineas = lineas;
        this.subtotal = subtotal;
        this.total = total;
        this.iva = iva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void agregarLinea(LineaFactura linea) {
        this.lineas.add(linea);
    }
    
    public void calcularSubtotal() {
        this.subtotal = 0.0;
        for (LineaFactura linea : lineas) {
            this.subtotal += linea.subtotalLinea;
        }
    }
    
    public void calcularIva(double porcentajeIva) {
        this.iva = this.subtotal * porcentajeIva;
    }
    
    public double calcularTotal() {
        return this.subtotal + this.iva;
    }
    
    public void calcularTotal(ImpuestoDeducible deducible) {
        double totalBruto = this.subtotal + this.iva;
        
        // Aplicar descuento del cliente usando polimorfismo
        double descuentoCliente = cliente.determinarDescuento(totalBruto);
        
        // Aplicar deducción de impuestos
        double totalConDeducible = deducible.aplicarDeduccion(totalBruto);
        
        this.total = totalConDeducible - descuentoCliente;
        
        // Acumular puntos si es cliente natural
        if (cliente instanceof ClienteNatural) {
            ((ClienteNatural) cliente).acumularPuntos(this.total);
        }
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
        sb.append("Fecha: ").append(fechaEmision.toString()).append("\n");
        sb.append("\n--- DATOS DEL CLIENTE ---\n");
        sb.append("ID: ").append(cliente.idCliente).append("\n");
        sb.append("Nombre: ").append(cliente.nombre).append("\n");
        sb.append("Correo: ").append(cliente.correo).append("\n");
        sb.append("Teléfono: ").append(cliente.telefono).append("\n");
        sb.append("Dirección: ").append(cliente.direccion).append("\n");
        
        // Mostrar tipo de cliente usando polimorfismo
        String tipoCliente = (cliente instanceof ClienteNatural) ? "NATURAL" : "NORMAL";
        sb.append("Tipo: ").append(tipoCliente).append("\n");
        
        sb.append("\n--- DETALLE DE PRODUCTOS ---\n");
        sb.append("PRODUCTO                CANT   P.UNIT   SUBTOTAL   TIPO\n");
        sb.append("--------------------------------------------------------\n");
        
        for (LineaFactura linea : lineas) {
            String tipoProducto = (linea.producto instanceof ProductoPromocional) ? "PROMO" : "NORMAL";
            sb.append(String.format("%-20s %4d   %6.2f   %8.2f   %s\n",
                linea.producto.nombre,
                linea.cantidad,
                linea.precioUnitario,
                linea.subtotalLinea,
                tipoProducto));
        }
        
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("SUBTOTAL:                            %8.2f\n", subtotal));
        sb.append(String.format("IVA (15%%):                           %8.2f\n", iva));
        
        // Mostrar descuentos aplicados
        double totalSinDescuentos = subtotal + iva;
        double descuentoTotal = totalSinDescuentos - total;
        if (descuentoTotal > 0) {
            sb.append(String.format("DESCUENTOS:                         -%8.2f\n", descuentoTotal));
        }
        
        sb.append(String.format("TOTAL:                               %8.2f\n", total));
        sb.append("==========================================\n");
        
        return sb.toString();
    }
}
