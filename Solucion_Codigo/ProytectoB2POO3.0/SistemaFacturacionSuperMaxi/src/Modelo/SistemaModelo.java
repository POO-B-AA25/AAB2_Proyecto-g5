
package Modelo;

import Controlador.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;


public class SistemaModelo {

    private Inventario inventario;
    private ImpuestoDeducible deducible;
    private EstadisticaVentas estadisticas;

    private final String nombreArchivoInventario = "datosinventario.csv";
    private final String nombreArchivoFacturas = "datosfacturas.csv";

    private final NumberFormat numberFormat;

    public SistemaModelo() {

        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

        crearDirectorios();

        inventario = new Inventario();
        cargarInventarioCSV();


        deducible = new ImpuestoDeducible();
        estadisticas = new EstadisticaPorProducto(); 
    }

    private void crearDirectorios() {
        File dirFacturas = new File("facturas");
        if (!dirFacturas.exists()) {
            boolean created = dirFacturas.mkdirs();
            if (created) {
                System.out.println("[INFO] Directorio 'facturas' creado");
            }
        }
    }

    private double parseDouble(String numeroStr) throws ParseException {
        try {
            return numberFormat.parse(numeroStr.trim()).doubleValue();
        } catch (ParseException e) {
            try {
                String numeroConPunto = numeroStr.trim().replace(',', '.');
                return Double.parseDouble(numeroConPunto);
            } catch (NumberFormatException nfe) {
                throw new ParseException("No se puede convertir '" + numeroStr + "' a número", 0);
            }
        }
    }

    private void cargarInventarioCSV() {
        File file = new File(nombreArchivoInventario);
        if (!file.exists()) {
            try (Formatter f = new Formatter(file)) {
                f.format("idProducto;nombre;categoria;precioBase;cantidadStock;fechaCaducidad;tipoProducto%n");
            } catch (Exception e) {
                System.err.println("Error crear inventario: " + e.getMessage());
            }
            return;
        }
        try (Scanner sc = new Scanner(file)) {
            sc.useDelimiter("\\r?\\n");
            int lineaActual = 0;
            while (sc.hasNextLine()) {
                lineaActual++;
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                if (linea.startsWith("idProducto;")) {
                    continue; // saltar cabecera
                }
                try {
                    String[] partes = linea.split(";");
                    if (partes.length >= 6) {
                        Producto p = null;
                        String idProducto = partes[0];
                        String nombre = partes[1];
                        String categoria = partes[2];
                        double precio = parseDouble(partes[3]);
                        int cantidad = Integer.parseInt(partes[4]);
                        String fechaCaducidad = partes[5];
                        String tipoProducto = partes.length > 6 ? partes[6] : "NORMAL";

                        // Crear producto según el tipo
                        if ("PROMOCIONAL".equalsIgnoreCase(tipoProducto)) {
                            p = new ProductoPromocional(nombre, categoria, cantidad, precio);
                        } else {
                            p = new ProductoNormal(nombre, categoria, cantidad, precio);
                        }
                        
                        // Asignar ID y fecha específicos del archivo
                        p.idProducto = idProducto;
                        p.fechaCaducidad = java.time.LocalDate.parse(fechaCaducidad);
                        
                        inventario.agregarProducto(p);
                    }
                } catch (Exception e) {
                    System.err.println("Error en línea " + lineaActual + " del inventario: " + e.getMessage());
                    System.err.println("Contenido de la línea: " + linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error leer inventario: " + e.getMessage());
        }
    }

    private void guardarInventarioCSV() {
        try (Formatter f = new Formatter(nombreArchivoInventario)) {
            f.format("idProducto;nombre;categoria;precioBase;cantidadStock;fechaCaducidad;tipoProducto%n");
            for (Producto p : inventario.productos) {
                String tipo = (p instanceof ProductoPromocional) ? "PROMOCIONAL" : "NORMAL";
                double precioBase = (p instanceof ProductoPromocional) ? 
                    ((ProductoPromocional) p).precioBase : ((ProductoNormal) p).precioNormal;
                
                f.format("%s;%s;%s;%.2f;%d;%s;%s%n",
                        p.idProducto, p.nombre, p.categoria, precioBase, p.cantidadStock,
                        p.fechaCaducidad.toString(), tipo);
            }
        } catch (Exception e) {
            System.err.println("Error guardar inventario: " + e.getMessage());
        }
    }

    public void mostrarInventario() {
        inventario.listarInventario();
    }

    public void agregarProductoFlujo(Scanner sc) {
        System.out.println("=== AGREGAR PRODUCTO ===");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Categoría: ");
        String categoria = sc.nextLine().trim();

        double precio = 0;
        boolean precioValido = false;
        while (!precioValido) {
            try {
                System.out.print("Precio unitario (puede usar , o . como decimal): ");
                String precioStr = sc.nextLine().trim();
                precio = parseDouble(precioStr);
                precioValido = true;
            } catch (ParseException e) {
                System.out.println("Error: " + e.getMessage() + ". Intente nuevamente.");
            }
        }

        System.out.print("Cantidad inicial: ");
        int cantidad = Integer.parseInt(sc.nextLine().trim());
        
        System.out.print("¿Es producto promocional? (S/N): ");
        String esPromo = sc.nextLine().trim().toUpperCase();
        
        Producto p;
        if (esPromo.equals("S")) {
            p = new ProductoPromocional(nombre, categoria, cantidad, precio);
        } else {
            p = new ProductoNormal(nombre, categoria, cantidad, precio);
        }

        inventario.agregarProducto(p);
        guardarInventarioCSV();
        System.out.println("[OK] Producto agregado, ID=" + p.idProducto + " | Caduca=" + p.fechaCaducidad.toString());
        System.out.println("Precio final: $" + String.format("%.2f", p.determinarPrecio()));
    }

    public void crearFacturaFlujo(Scanner sc) {
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
            mostrarInventario();
            System.out.print("ID producto a vender (o 0 para terminar): ");
            String idp = sc.nextLine().trim();
            if (idp.equals("0")) {
                break;
            }
            Producto prod = inventario.buscarProducto(idp);
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

            // Descontar stock y guardar inventario
            prod.disminuirStock(cant);
            guardarInventarioCSV();
            System.out.println("[OK] " + cant + " x " + prod.nombre + " agregado a $" + String.format("%.2f", pu) + " c/u");

            System.out.print("¿Agregar otro producto? (S/N): ");
            String r = sc.nextLine().trim().toUpperCase();
            if (!r.equals("S")) {
                cont = false;
            }
        }

        if (fac.lineas.isEmpty()) {
            System.out.println("No se agregaron productos. Factura cancelada.");
            return;
        }

        fac.calcularSubtotal();
        fac.calcularIva(0.15);
        fac.calcularTotal(deducible);


        System.out.println(fac.toString());

        guardarFacturaCSV(fac);

        for (LineaFactura lf : fac.lineas) {
            estadisticas.registrarVenta(lf);
        }
        estadisticas.guardarEstadisticasCsv();
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

    public void mostrarEstadisticas() {
        System.out.println("=== ESTADÍSTICAS DE VENTAS ===");
        System.out.println("Monto total vendido: $" + String.format("%.2f", estadisticas.montoTotal));
        estadisticas.mostrarMasMenosVendidos();
    }
    
    public void cambiarTipoEstadistica(Scanner sc) {
        System.out.println("=== CAMBIAR TIPO DE ESTADÍSTICA ===");
        System.out.println("1. Estadísticas por producto");
        System.out.println("2. Estadísticas por categoría");
        System.out.print("Seleccione opción: ");
        
        String opcion = sc.nextLine().trim();
        switch (opcion) {
            case "1":
                estadisticas = new EstadisticaPorProducto();
                System.out.println("[OK] Cambiado a estadísticas por producto");
                break;
            case "2":
                estadisticas = new EstadisticaPorCategoria();
                System.out.println("[OK] Cambiado a estadísticas por categoría");
                break;
            default:
                System.out.println("[Error] Opción inválida");
        }
    }
}
