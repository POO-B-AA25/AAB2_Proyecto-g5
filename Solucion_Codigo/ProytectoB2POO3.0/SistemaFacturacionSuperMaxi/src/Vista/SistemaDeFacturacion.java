package Vista;

import Modelo.*;
import Controlador.*;
import java.util.Scanner;


public class SistemaDeFacturacion {
    
    // Gestores especializados siguiendo principios SOLID
    private static GestorInventario gestorInventario;
    private static GestorFacturacion gestorFacturacion;
    private static GestorEstadisticas gestorEstadisticas;
    private static GestorArchivos gestorArchivos;
    
    public static void main(String[] args) {
        // Inicializar gestores SOLID
        inicializarSistema();
        
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        // Mensaje de bienvenida
        System.out.println("¡Bienvenido al Sistema MVC de Facturación SOLID!");
        System.out.println("Sistema inicializado con gestores especializados.");

        // Bucle principal del sistema
        while (!salir) {
            try {
                mostrarMenu();
                String op = sc.nextLine().trim();
                
                switch (op) {
                    case "1":
                        // Usar directamente el GestorInventario
                        gestorInventario.mostrarInventario();
                        pausa(sc);
                        break;
                        
                    case "2":
                        // Agregar producto usando GestorInventario
                        try {
                            gestorInventario.agregarProductoFlujo(sc);
                        } catch (NumberFormatException e) {
                            System.out.println("[Error] Formato numérico inválido: " + e.getMessage());
                        }
                        pausa(sc);
                        break;
                        
                    case "3":
                        // Crear factura usando GestorFacturacion y registrar en GestorEstadisticas
                        try {
                            Factura facturaCreada = gestorFacturacion.crearFacturaFlujo(sc);
                            if (facturaCreada != null) {
                                gestorEstadisticas.registrarVentasFactura(facturaCreada);
                            }
                        } catch (Exception e) {
                            System.out.println("[Error] Error en la facturación: " + e.getMessage());
                        }
                        pausa(sc);
                        break;
                        
                    case "4":
                        // Ver estadísticas usando GestorEstadisticas
                        gestorEstadisticas.mostrarEstadisticas();
                        pausa(sc);
                        break;
                        
                    case "5":
                        // Cambiar tipo de estadística usando GestorEstadisticas
                        gestorEstadisticas.cambiarTipoEstadistica(sc);
                        pausa(sc);
                        break;
                        
                    case "6":
                        // Información del sistema
                        mostrarInformacionSistema();
                        pausa(sc);
                        break;
                        
                    case "7":
                        // Opción adicional: Ver estado de gestores
                        mostrarEstadoGestores();
                        pausa(sc);
                        break;
                        
                    case "0":
                        // Salir del sistema
                        System.out.print("¿Está seguro que desea salir? (S/N): ");
                        String confirmacion = sc.nextLine().trim().toUpperCase();
                        if (confirmacion.equals("S")) {
                            salir = true;
                        }
                        break;
                        
                    default:
                        System.out.println("[Error] Opción inválida. Intente nuevamente.");
                        pausa(sc);
                }
            } catch (Exception e) {
                System.err.println("[Error del Sistema] " + e.getMessage());
                System.out.println("Presione Enter para continuar...");
                sc.nextLine();
            }
        }
        
        // Mensaje de despedida
        mostrarDespedida();
        sc.close();
    }
    
    /**
     * Inicializa todos los gestores SOLID del sistema
     */
    private static void inicializarSistema() {
        System.out.println("Inicializando gestores SOLID...");
        
        // 1. Gestor de archivos (configuración inicial)
        gestorArchivos = new GestorArchivos();
        gestorArchivos.crearDirectorios();
        System.out.println("GestorArchivos inicializado");
        
        // 2. Gestor de inventario (carga datos CSV)
        gestorInventario = new GestorInventario();
        System.out.println("GestorInventario inicializado");
        
        // 3. Gestor de facturación (depende del inventario)
        gestorFacturacion = new GestorFacturacion(gestorInventario);
        System.out.println("GestorFacturacion inicializado");
        
        // 4. Gestor de estadísticas
        gestorEstadisticas = new GestorEstadisticas();
        System.out.println("GestorEstadisticas inicializado");
        
        System.out.println("Todos los gestores SOLID están listos!\n");
    }
    
    /**
     * Muestra el menú principal del sistema
     */
    private static void mostrarMenu() {
        System.out.println("\n=============================================");
        System.out.println("     SISTEMA MVC FACTURACIÓN - SOLID       ");
        System.out.println("=============================================");
        System.out.println("1. Ver inventario completo [GestorInventario]");
        System.out.println("2. Agregar nuevo producto [GestorInventario]");
        System.out.println("3. Crear nueva factura [GestorFacturacion + GestorEstadisticas]");
        System.out.println("4. Ver estadísticas de ventas [GestorEstadisticas]");
        System.out.println("5. Cambiar tipo de estadística [GestorEstadisticas]");
        System.out.println("6. Información del sistema");
        System.out.println("7. Ver estado de gestores SOLID");
        System.out.println("0. Salir del sistema");
        System.out.println("=============================================");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    private static void pausa(Scanner sc) {
        System.out.println("\nPresione Enter para continuar...");
        sc.nextLine();
    }
    
    /**
     * Muestra información detallada del sistema SOLID
     */
    private static void mostrarInformacionSistema() {
        System.out.println("\n=== INFORMACIÓN DEL SISTEMA SOLID ===");
        System.out.println("Sistema: MVC Facturación con Arquitectura SOLID");
        System.out.println("Versión: 3.0 (Gestores Especializados)");
        System.out.println("");
        
        System.out.println("GESTORES IMPLEMENTADOS:");
        System.out.println("  GestorInventario:");
        System.out.println("      - Responsabilidad: Gestión completa del inventario");
        System.out.println("      - Funciones: Cargar/guardar CSV, mostrar, agregar, buscar productos");
        System.out.println("");
        System.out.println("  GestorFacturacion:");
        System.out.println("      - Responsabilidad: Proceso completo de facturación");  
        System.out.println("      - Funciones: Crear facturas, guardar CSV, serializar");
        System.out.println("");
        System.out.println("  GestorEstadisticas:");
        System.out.println("      - Responsabilidad: Manejo de estadísticas de ventas");
        System.out.println("      - Funciones: Mostrar stats, cambiar tipos, registrar ventas");
        System.out.println("");
        System.out.println("  GestorArchivos:");
        System.out.println("      - Responsabilidad: Configuración de archivos del sistema");
        System.out.println("      - Funciones: Crear directorios necesarios");
        System.out.println("");
        
        System.out.println("PRINCIPIOS SOLID APLICADOS:");
        System.out.println("  - SRP: Cada gestor tiene UNA responsabilidad específica");
        System.out.println("  - OCP: Abierto para extensión, cerrado para modificación");
        System.out.println("  - LSP: Subtipos reemplazables (polimorfismo en productos/clientes)");
        System.out.println("  - ISP: Interfaces segregadas según necesidad");
        System.out.println("  - DIP: Dependencias de abstracciones, no concreciones");
        System.out.println("");
        
        System.out.println("ARCHIVOS DEL SISTEMA:");
        System.out.println("  - datosinventario.csv - Inventario persistente");
        System.out.println("  - datosfacturas.csv - Histórico de facturas");
        System.out.println("  - facturas/*.ser - Facturas serializadas");
        System.out.println("  - estadisticas_productos.csv - Stats por producto");
        System.out.println("  - estadisticas_categorias.csv - Stats por categoría");
    }
    
 
    private static void mostrarEstadoGestores() {
        System.out.println("\n=== ESTADO DE GESTORES SOLID ===");
        System.out.println("GestorInventario: " + (gestorInventario != null ? "ACTIVO" : "INACTIVO"));
        System.out.println("GestorFacturacion: " + (gestorFacturacion != null ? "ACTIVO" : "INACTIVO"));
        System.out.println("GestorEstadisticas: " + (gestorEstadisticas != null ? "ACTIVO" : "INACTIVO"));
        System.out.println("GestorArchivos: " + (gestorArchivos != null ? "ACTIVO" : "INACTIVO"));
        System.out.println("");
        System.out.println("DEPENDENCIAS:");
        System.out.println("  - GestorFacturacion -> GestorInventario (inyección de dependencia)");
        System.out.println("  - Main -> Todos los gestores (coordinación directa)");
        System.out.println("");
        System.out.println("FLUJO DE DATOS:");
        System.out.println("  1. GestorArchivos configura el sistema");
        System.out.println("  2. GestorInventario carga datos CSV");
        System.out.println("  3. GestorFacturacion usa inventario para crear facturas");
        System.out.println("  4. GestorEstadisticas registra las ventas realizadas");
    }
    

    private static void mostrarDespedida() {
        System.out.println("\n=============================================");
        System.out.println("   Cerrando Sistema MVC SOLID               ");
        System.out.println("                                             ");
        System.out.println("   GestorInventario: Datos guardados        ");
        System.out.println("   GestorFacturacion: Facturas respaldadas  ");
        System.out.println("   GestorEstadisticas: Stats actualizadas   ");
        System.out.println("   GestorArchivos: Sistema configurado      ");
        System.out.println("                                             ");
        System.out.println("         ¡Gracias por usar SOLID!           ");
        System.out.println("=============================================");
    }
}