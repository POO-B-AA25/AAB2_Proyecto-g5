package Modelo;

import java.util.Scanner;

import Controlador.*;

public class GestorEstadisticas {
    
    private EstadisticaVentas estadisticas;
    
    public GestorEstadisticas() {
        this.estadisticas = new EstadisticaPorProducto();
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
    
    public void registrarVentasFactura(Factura factura) {
        if (factura != null && !factura.lineas.isEmpty()) {
            for (LineaFactura lf : factura.lineas) {
                estadisticas.registrarVenta(lf);
            }
            estadisticas.guardarEstadisticasCsv();
        }
    }
}