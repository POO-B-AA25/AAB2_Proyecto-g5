package vista;

import controlador.SistemaController;
import java.util.Scanner;

public class SistemaDeFacturacion {
    public static void main(String[] args) {
        SistemaController ctrl = new SistemaController();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=============================");
            System.out.println("    SISTEMA MVC FACTURAS     ");
            System.out.println("=============================");
            System.out.println("1. Ver inventario");
            System.out.println("2. Agregar producto");
            System.out.println("3. Hacer factura");
            System.out.println("4. Ver estadísticas de ventas");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");

            String op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    ctrl.mostrarInventario();
                    break;
                case "2":
                    ctrl.agregarProductoFlujo(sc);
                    break;
                case "3":
                    ctrl.crearFacturaFlujo(sc);
                    break;
                case "4":
                    ctrl.mostrarEstadisticas();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("[Error] Opción inválida.");
            }
        }
        System.out.println("Saliendo... ¡Gracias!");
        sc.close();
    }
}
