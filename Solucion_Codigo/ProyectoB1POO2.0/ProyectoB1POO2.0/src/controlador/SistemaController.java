package controlador;

import modelo.*;
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

public class SistemaController {

    private Inventario inventario;
    private Deducible deducible;
    private EstadisticaVentas estadisticas;

    private final String nombreArchivoInventario = "datosinventario.csv";
    private final String nombreArchivoFacturas = "datosfacturas.csv";

    private final NumberFormat numberFormat;

    public SistemaController() {
        // Configurar el formatter para manejar números con coma
        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

        // Crear directorio para facturas serializadas
        crearDirectorios();

        // 1) Cargar inventario desde CSV
        inventario = new Inventario();
        cargarInventarioCSV();

        // 2) Inicializar objetos de deducción y estadísticas
        deducible = new Deducible();
        estadisticas = new EstadisticaVentas();
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
        // Intentar primero con el formato local (coma)
        try {
            return numberFormat.parse(numeroStr.trim()).doubleValue();
        } catch (ParseException e) {
            // Si falla, intentar con formato inglés (punto)
            try {
                // Reemplazar coma por punto para formato inglés
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
            // Si no existe, crear con cabecera
            try (Formatter f = new Formatter(file)) {
                f.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
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
                        Producto p = new Producto();
                        p.idProducto = partes[0];
                        p.nombre = partes[1];
                        p.categoria = partes[2];
                        p.precioNormal = parseDouble(partes[3]);
                        p.cantidadStock = Integer.parseInt(partes[4]);
                        p.fechaCaducidad = java.time.LocalDate.parse(partes[5]);
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
            f.format("idProducto;nombre;categoria;precioNormal;cantidadStock;fechaCaducidad%n");
            for (Producto p : inventario.productos) {
                f.format("%s;%s;%s;%.2f;%d;%s%n",
                        p.idProducto, p.nombre, p.categoria, p.precioNormal, p.cantidadStock,
                        p.fechaCaducidad.toString());
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

        Producto p = new Producto(nombre, categoria, precio, cantidad);
        inventario.agregarProducto(p);
        guardarInventarioCSV();
        System.out.println("[OK] Producto agregado, ID=" + p.idProducto + " | Caduca=" + p.fechaCaducidad.toString());
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
        
        // Usar constructor con 5 parámetros
        Cliente cliente = new Cliente(idCli, nomCli, correo, tel, dir);
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
            double pu = prod.precioNormal;
            LineaFactura lf = new LineaFactura(prod, cant, pu);
            fac.agregarLinea(lf);

            // Descontar stock y guardar inventario
            prod.disminuirStock(cant);
            guardarInventarioCSV();
            System.out.println("[OK] " + cant + " x " + prod.nombre + " agregado.");

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

        // Calcular totales
        fac.calcularSubtotal();
        fac.calcularIva(0.15);
        fac.calcularTotal(deducible);

        // Mostrar factura
        System.out.println(fac.toString());

        // Guardar factura en CSV
        guardarFacturaCSV(fac);

        // Actualizar estadísticas
        for (LineaFactura lf : fac.lineas) {
            estadisticas.registrarVenta(lf);
        }
        estadisticas.guardarEstadisticasCSV();
    }

    private void guardarFacturaCSV(Factura fac) {
        File file = new File(nombreArchivoFacturas);
        try (Formatter f = new Formatter(new FileOutputStream(file, true))) {
            if (file.length() == 0) {
                f.format("# Facturas%n");
                f.format("# Encabezado: idFactura;fecha;clienteId;clienteNombre;correo;telefono;direccion;subtotal;iva;total%n");
            }

            String fecha = fac.fechaEmision.toString();

            f.format("%s;%s;%s;%s;%s;%s;%s;%.2f;%.2f;%.2f%n",
                    fac.idFactura, fecha,
                    fac.clienteObj.idCliente, fac.clienteObj.nombre,
                    fac.clienteObj.correo, fac.clienteObj.telefono,
                    fac.clienteObj.direccion,
                    fac.subtotal, fac.iva, fac.total
            );

            f.format("# Detalle: idFactura;idProducto;nombreProducto;categoria;cantidad;precioUnitario;subtotalLinea%n");
            for (LineaFactura lf : fac.lineas) {
                f.format("%s;%s;%s;%s;%d;%.2f;%.2f%n",
                        fac.idFactura,
                        lf.producto.idProducto, lf.producto.nombre,
                        lf.producto.categoria,
                        lf.cantidad, lf.precioUnitario, lf.subtotalLinea
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
        estadisticas.mostrarMasMenosVendidos();
    }
}