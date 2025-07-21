
package Controlador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;

public class EstadisticaPorProducto extends EstadisticaVentas {
    public static class VentaPorProducto {
        public String idProducto;
        public String nombreProducto;
        public int cantidadProductoVendida;
        public double montoTotal;

        public VentaPorProducto(String id, String nombre) {
            this.idProducto = id;
            this.nombreProducto = nombre;
            this.cantidadProductoVendida = 0;
            this.montoTotal = 0.0;
        }
    }

    public ArrayList<VentaPorProducto> ventasProductos;

    public EstadisticaPorProducto() {
        super();
        this.ventasProductos = new ArrayList<>();
    }

    @Override
    public void calcularEstadistica(LineaFactura lineaFactura) {
        VentaPorProducto vp = buscarVentaProducto(lineaFactura.producto.idProducto);
        if (vp == null) {
            vp = new VentaPorProducto(lineaFactura.producto.idProducto, lineaFactura.producto.nombre);
            ventasProductos.add(vp);
        }
        vp.cantidadProductoVendida += lineaFactura.cantidad;
        vp.montoTotal += lineaFactura.subtotalLinea;
    }

    private VentaPorProducto buscarVentaProducto(String idProd) {
        for (VentaPorProducto vp : ventasProductos) {
            if (vp.idProducto.equals(idProd)) {
                return vp;
            }
        }
        return null;
    }

    @Override
    public void mostrarMasMenosVendidos() {
        if (ventasProductos.isEmpty()) {
            System.out.println("No hay ventas de productos registradas.");
            return;
        }

        // Ordenar por cantidad vendida
        ventasProductos.sort(new Comparator<VentaPorProducto>() {
            public int compare(VentaPorProducto a, VentaPorProducto b) {
                return b.cantidadProductoVendida - a.cantidadProductoVendida;
            }
        });

        VentaPorProducto masVendido = ventasProductos.get(0);
        VentaPorProducto menosVendido = ventasProductos.get(ventasProductos.size() - 1);

        System.out.println("=== PRODUCTO MÁS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades, $%.2f%n",
            masVendido.nombreProducto, masVendido.idProducto, 
            masVendido.cantidadProductoVendida, masVendido.montoTotal);

        System.out.println("=== PRODUCTO MENOS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades, $%.2f%n",
            menosVendido.nombreProducto, menosVendido.idProducto, 
            menosVendido.cantidadProductoVendida, menosVendido.montoTotal);
    }

    @Override
    public void guardarEstadisticasCsv() {
        try (Formatter f = new Formatter("estadisticas_productos.csv")) {
            f.format("idProducto;nombreProducto;cantidadVendida;montoTotal%n");
            for (VentaPorProducto vp : ventasProductos) {
                f.format("%s;%s;%d;%.2f%n",
                    vp.idProducto, vp.nombreProducto, vp.cantidadProductoVendida, vp.montoTotal);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estadísticas por producto: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS POR PRODUCTO ===\n");
        for (VentaPorProducto vp : ventasProductos) {
            sb.append(String.format("%s: %d unidades, $%.2f%n", 
                vp.nombreProducto, vp.cantidadProductoVendida, vp.montoTotal));
        }
        return sb.toString();
    }
}