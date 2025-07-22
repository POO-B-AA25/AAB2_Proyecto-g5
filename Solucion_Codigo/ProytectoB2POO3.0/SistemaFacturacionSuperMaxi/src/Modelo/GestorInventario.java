package Modelo;

import Controlador.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;


public class GestorInventario {
    
    private Inventario inventario;
    private String nombreArchivoInventario = "datosinventario.csv";
    private NumberFormat numberFormat;
    
    public GestorInventario() {
        this.numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        this.inventario = new Inventario();
        cargarInventarioCSV();
    }
    
    public void mostrarInventario() {
        inventario.listarInventario();
    }
    
    public Producto buscarProducto(String idProducto) {
        return inventario.buscarProducto(idProducto);
    }
    
    public void actualizarStockProducto(String idProducto, int cantidadVendida) {
        Producto producto = inventario.buscarProducto(idProducto);
        if (producto != null) {
            producto.disminuirStock(cantidadVendida);
            guardarInventarioCSV();
        }
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
}