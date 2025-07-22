package Modelo;

import Controlador.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Formatter;
import java.util.Scanner;


public class GestorFacturacion {
    
    private String nombreArchivoFacturas = "datosfacturas.csv";
    private GestorInventario gestorInventario;
    private ImpuestoDeducible deducible;
    
    public GestorFacturacion(GestorInventario gestorInventario) {
        this.gestorInventario = gestorInventario;
        this.deducible = new ImpuestoDeducible();
    }
    
    public Factura crearFacturaFlujo(Scanner sc) {
        Factura fac = new Factura();
        System.out.println("=== DATOS DE CLIENTE ===");
        System.out.print("ID Cliente: ");
        String idCli = sc.nextLine().trim();
        System.out.print("Nombre: ");
        String nomCli = sc.nextLine().trim();
        System.out.print("Correo: ");
        String correo = sc.nextLine().trim();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine().trim();
        System.out.print("Dirección: ");
        String dir = sc.nextLine().trim();
        
        System.out.print("¿Es cliente natural? (S/N): ");
        String esNatural = sc.nextLine().trim().toUpperCase();
        
        Cliente cliente;
        if (esNatural.equals("S")) {
            cliente = new ClienteNatural(idCli, nomCli, correo, tel, dir);
        } else {
            System.out.print("Puntos de cupón descuento: ");
            int puntos = Integer.parseInt(sc.nextLine().trim());
            cliente = new ClienteNormal(idCli, nomCli, correo, tel, dir, puntos);
        }
        
        fac.setCliente(cliente);

        boolean cont = true;
        while (cont) {
            gestorInventario.mostrarInventario();
            System.out.print("ID producto a vender (o 0 para terminar): ");
            String idp = sc.nextLine().trim();
            if (idp.equals("0")) {
                break;
            }
            Producto prod = gestorInventario.buscarProducto(idp);
            if (prod == null) {
                System.out.println("[Error] Producto no existe.");
                continue;
            }
            System.out.print("Cantidad a vender: ");
            int cant = Integer.parseInt(sc.nextLine().trim());
            if (cant <= 0 || cant > prod.cantidadStock) {
                System.out.println("[Error] Cantidad inválida o fuera de stock.");
                continue;
            }
            
            // Usar el precio determinado por polimorfismo
            double pu = prod.determinarPrecio();
            LineaFactura lf = new LineaFactura(prod, cant, pu);
            fac.agregarLinea(lf);

            // Descontar stock a través del gestor de inventario
            gestorInventario.actualizarStockProducto(idp, cant);
            System.out.println("[OK] " + cant + " x " + prod.nombre + " agregado a $" + String.format("%.2f", pu) + " c/u");

            System.out.print("¿Agregar otro producto? (S/N): ");
            String r = sc.nextLine().trim().toUpperCase();
            if (!r.equals("S")) {
                cont = false;
            }
        }

        if (fac.lineas.isEmpty()) {
            System.out.println("No se agregaron productos. Factura cancelada.");
            return null;
        }

        fac.calcularSubtotal();
        fac.calcularIva(0.15);
        fac.calcularTotal(deducible);

        System.out.println(fac.toString());
        
        guardarFacturaCSV(fac);
        
        return fac;
    }
    
    private void guardarFacturaCSV(Factura fac) {
        File file = new File(nombreArchivoFacturas);
        try (Formatter f = new Formatter(new FileOutputStream(file, true))) {
            if (file.length() == 0) {
                f.format("# Facturas%n");
                f.format("# Encabezado: idFactura;fecha;clienteId;clienteNombre;correo;telefono;direccion;tipoCliente;subtotal;iva;total%n");
            }

            String fecha = fac.fechaEmision.toString();
            String tipoCliente = (fac.cliente instanceof ClienteNatural) ? "NATURAL" : "NORMAL";

            f.format("%s;%s;%s;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f%n",
                    fac.idFactura, fecha,
                    fac.cliente.idCliente, fac.cliente.nombre,
                    fac.cliente.correo, fac.cliente.telefono,
                    fac.cliente.direccion, tipoCliente,
                    fac.subtotal, fac.iva, fac.total
            );

            f.format("# Detalle: idFactura;idProducto;nombreProducto;categoria;cantidad;precioUnitario;subtotalLinea;tipoProducto%n");
            for (LineaFactura lf : fac.lineas) {
                String tipoProducto = (lf.producto instanceof ProductoPromocional) ? "PROMOCIONAL" : "NORMAL";
                f.format("%s;%s;%s;%s;%d;%.2f;%.2f;%s%n",
                        fac.idFactura,
                        lf.producto.idProducto, lf.producto.nombre,
                        lf.producto.categoria,
                        lf.cantidad, lf.precioUnitario, lf.subtotalLinea,
                        tipoProducto
                );
            }

            f.format("%n");
            serializarFactura(fac);

        } catch (Exception e) {
            System.err.println("Error guardar factura: " + e.getMessage());
        }
    }

    private void serializarFactura(Factura factura) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("facturas/factura_" + factura.idFactura + ".ser"))) {
            oos.writeObject(factura);
            System.out.println("[SERIALIZADO] Factura guardada en: facturas/factura_" + factura.idFactura + ".ser");
        } catch (IOException e) {
            System.err.println("Error serializar factura: " + e.getMessage());
        }
    }
}