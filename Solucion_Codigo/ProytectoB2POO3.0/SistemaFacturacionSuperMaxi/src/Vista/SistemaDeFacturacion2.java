package vista;

import Modelo.*;
import java.util.Scanner;

public class SistemaDeFacturacion2 {
    public static void main(String[] args) {
        SistemaModelo ctrl = new SistemaModelo();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        System.out.println("¡Bienvenido al Sistema MVC de Facturación!");
        System.out.println("Sistema inicializado correctamente.");

        while (!salir) {
            try {
                mostrarMenu();
                String op = sc.nextLine().trim();
                
                switch (op) {
                    case "1":
                        ctrl.mostrarInventario();
                        pausa(sc);
                        break;
                    case "2":
                        try {
                            ctrl.agregarProductoFlujo(sc);
                        } catch (NumberFormatException e) {
                            System.out.println("[Error] Formato numérico inválido: " + e.getMessage());
                        }
                        pausa(sc);
                        break;
                    case "3":
                        try {
                            ctrl.crearFacturaFlujo(sc);
                        } catch (Exception e) {
                            System.out.println("[Error] Error en la facturación: " + e.getMessage());
                        }
                        pausa(sc);
                        break;
                    case "4":
                        ctrl.mostrarEstadisticas();
                        pausa(sc);
                        break;
                    case "5":
                        ctrl.cambiarTipoEstadistica(sc);
                        pausa(sc);
                        break;
                    case "6":
                        mostrarInformacionSistema();
                        pausa(sc);
                        break;
                    case "0":
                        System.out.println("¿Está seguro que desea salir? (S/N): ");
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
        
        System.out.println("=============================================");
        System.out.println("    Gracias por usar el sistema MVC");
        System.out.println("         ¡Hasta luego!");
        System.out.println("=============================================");
        sc.close();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n=============================================");
        System.out.println("       SISTEMA MVC DE FACTURACIÓN          ");
        System.out.println("=============================================");
        System.out.println("1. Ver inventario completo");
        System.out.println("2. Agregar nuevo producto");
        System.out.println("3. Crear nueva factura");
        System.out.println("4. Ver estadísticas de ventas");
        System.out.println("5. Cambiar tipo de estadística");
        System.out.println("6. Información del sistema");
        System.out.println("0. Salir del sistema");
        System.out.println("=============================================");
        System.out.print("Seleccione una opción: ");
    }
    
    private static void pausa(Scanner sc) {
        System.out.println("\nPresione Enter para continuar...");
        sc.nextLine();
    }
    
    private static void mostrarInformacionSistema() {
        System.out.println("\n=== INFORMACIÓN DEL SISTEMA ===");
        System.out.println("Sistema: MVC Facturación con Polimorfismo");
        System.out.println("Versión: 2.0");
        System.out.println("Funcionalidades:");
        System.out.println("  • Gestión de inventario con productos normales y promocionales");
        System.out.println("  • Manejo de clientes naturales y normales");
        System.out.println("  • Facturación con cálculos automáticos de IVA y descuentos");
        System.out.println("  • Estadísticas de ventas por producto y categoría");
        System.out.println("  • Persistencia en archivos CSV y serialización");
        System.out.println("  • Aplicación de deducciones fiscales");
        System.out.println("\nArchivos generados:");
        System.out.println("  • datosinventario.csv - Base de datos del inventario");
        System.out.println("  • datosfacturas.csv - Registro de todas las facturas");
        System.out.println("  • facturas/*.ser - Facturas serializadas");
        System.out.println("  • estadisticas_productos.csv - Estadísticas por producto");
        System.out.println("  • estadisticas_categorias.csv - Estadísticas por categoría");
        System.out.println("\nPatrones implementados:");
        System.out.println("  • MVC (Modelo-Vista-Controlador)");
        System.out.println("  • Polimorfismo (Productos y Clientes)");
        System.out.println("  • Herencia (Clases abstractas)");
        System.out.println("  • Encapsulamiento y abstracción");
    }
}