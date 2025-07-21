
package Controlador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;

public class EstadisticaPorCategoria extends EstadisticaVentas {
    public static class VentaPorCategoria {
        public String categoria;
        public int cantidadCategoriaVendida;
        public double montoTotal;

        public VentaPorCategoria(String categoria) {
            this.categoria = categoria;
            this.cantidadCategoriaVendida = 0;
            this.montoTotal = 0.0;
        }
    }

    public ArrayList<VentaPorCategoria> ventasCategorias;

    public EstadisticaPorCategoria() {
        super();
        this.ventasCategorias = new ArrayList<>();
    }

    @Override
    public void calcularEstadistica(LineaFactura lineaFactura) {
        VentaPorCategoria vc = buscarVentaCategoria(lineaFactura.producto.categoria);
        if (vc == null) {
            vc = new VentaPorCategoria(lineaFactura.producto.categoria);
            ventasCategorias.add(vc);
        }
        vc.cantidadCategoriaVendida += lineaFactura.cantidad;
        vc.montoTotal += lineaFactura.subtotalLinea;
    }

    private VentaPorCategoria buscarVentaCategoria(String cat) {
        for (VentaPorCategoria vc : ventasCategorias) {
            if (vc.categoria.equalsIgnoreCase(cat)) {
                return vc;
            }
        }
        return null;
    }

    @Override
    public void mostrarMasMenosVendidos() {
        if (ventasCategorias.isEmpty()) {
            System.out.println("No hay ventas por categoría registradas.");
            return;
        }

        // Ordenar por cantidad vendida
        ventasCategorias.sort(new Comparator<VentaPorCategoria>() {
            public int compare(VentaPorCategoria a, VentaPorCategoria b) {
                return b.cantidadCategoriaVendida - a.cantidadCategoriaVendida;
            }
        });

        VentaPorCategoria masVendida = ventasCategorias.get(0);
        System.out.println("=== CATEGORÍA MÁS VENDIDA ===");
        System.out.printf("%s: %d unidades, $%.2f%n",
            masVendida.categoria, masVendida.cantidadCategoriaVendida, masVendida.montoTotal);
    }

    @Override
    public void guardarEstadisticasCsv() {
        try (Formatter f = new Formatter("estadisticas_categorias.csv")) {
            f.format("categoria;cantidadVendida;montoTotal%n");
            for (VentaPorCategoria vc : ventasCategorias) {
                f.format("%s;%d;%.2f%n",
                    vc.categoria, vc.cantidadCategoriaVendida, vc.montoTotal);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estadísticas por categoría: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS POR CATEGORÍA ===\n");
        for (VentaPorCategoria vc : ventasCategorias) {
            sb.append(String.format("%s: %d unidades, $%.2f%n", 
                vc.categoria, vc.cantidadCategoriaVendida, vc.montoTotal));
        }
        return sb.toString();
    }
}