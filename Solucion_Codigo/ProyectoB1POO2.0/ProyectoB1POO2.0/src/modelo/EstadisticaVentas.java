package modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;

/**
 * Lleva estadísticas de ventas acumuladas:
 *  - ArrayList<VentaPorProducto> ventasProductos
 *  - ArrayList<VentaPorCategoria> ventasCategorias
 *
 * Cuando se registre cada línea vendida, se actualizan los contadores.
 * Luego se guardan ambas tablas en un CSV "datosestadisticas.csv".
 */
public class EstadisticaVentas {
    /** Clase interna para acumular ventas por producto */
    public static class VentaPorProducto {
        public String idProducto;
        public String nombreProducto;
        public int cantidadVendida;
        public double montoTotal;

        public VentaPorProducto(String id, String nombre) {
            this.idProducto       = id;
            this.nombreProducto   = nombre;
            this.cantidadVendida  = 0;
            this.montoTotal       = 0.0;
        }
    }

    /** Clase interna para acumular ventas por categoría */
    public static class VentaPorCategoria {
        public String categoria;
        public int cantidadVendida;
        public double montoTotal;

        public VentaPorCategoria(String categoria) {
            this.categoria       = categoria;
            this.cantidadVendida = 0;
            this.montoTotal      = 0.0;
        }
    }

    public ArrayList<VentaPorProducto> ventasProductos;
    public ArrayList<VentaPorCategoria> ventasCategorias;

    public EstadisticaVentas() {
        ventasProductos   = new ArrayList<>();
        ventasCategorias  = new ArrayList<>();
    }

    /**
     * Registra la venta de una línea de factura:
     *  - Actualiza el contador y monto en ventasProductos
     *  - Actualiza el contador y monto en ventasCategorias
     */
    public void registrarVenta(LineaFactura lf) {
        // Por producto
        VentaPorProducto vp = buscarVentaProducto(lf.producto.idProducto);
        if (vp == null) {
            vp = new VentaPorProducto(lf.producto.idProducto, lf.producto.nombre);
            ventasProductos.add(vp);
        }
        vp.cantidadVendida += lf.cantidad;
        vp.montoTotal      += lf.subtotalLinea;

        // Por categoría
        VentaPorCategoria vc = buscarVentaCategoria(lf.producto.categoria);
        if (vc == null) {
            vc = new VentaPorCategoria(lf.producto.categoria);
            ventasCategorias.add(vc);
        }
        vc.cantidadVendida += lf.cantidad;
        vc.montoTotal      += lf.subtotalLinea;
    }

    private VentaPorProducto buscarVentaProducto(String idProd) {
        for (VentaPorProducto vp : ventasProductos) {
            if (vp.idProducto.equals(idProd)) {
                return vp;
            }
        }
        return null;
    }

    private VentaPorCategoria buscarVentaCategoria(String cat) {
        for (VentaPorCategoria vc : ventasCategorias) {
            if (vc.categoria.equalsIgnoreCase(cat)) {
                return vc;
            }
        }
        return null;
    }

    /**
     * Guarda completamente ambas listas en "datosestadisticas.csv".
     * Formato:
     *
     * Ventas por Producto
     * idProducto;nombreProducto;cantidadVendida;montoTotal
     * ...
     *
     * Ventas por Categoría
     * categoria;cantidadVendida;montoTotal
     * ...
     */
    public void guardarEstadisticasCSV() {
        try (Formatter f = new Formatter("datosestadisticas.csv")) {
            // Sección productos
            f.format("Ventas por Producto%n");
            f.format("idProducto;nombreProducto;cantidadVendida;montoTotal%n");
            for (VentaPorProducto vp : ventasProductos) {
                f.format("%s;%s;%d;%.2f%n",
                    vp.idProducto, vp.nombreProducto, vp.cantidadVendida, vp.montoTotal);
            }
            f.format("%nVentas por Categoría%n");
            f.format("categoria;cantidadVendida;montoTotal%n");
            for (VentaPorCategoria vc : ventasCategorias) {
                f.format("%s;%d;%.2f%n",
                    vc.categoria, vc.cantidadVendida, vc.montoTotal);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Muestra en consola:
     *  - Producto más vendido (por cantidad)
     *  - Producto menos vendido
     *  - Categoría más vendida
     */
    public void mostrarMasMenosVendidos() {
        if (ventasProductos.isEmpty()) {
            System.out.println("No hay ventas registradas aún.");
            return;
        }
        // Ordenar productos por cantidadVendida descendente
        ventasProductos.sort(new Comparator<VentaPorProducto>() {
            public int compare(VentaPorProducto a, VentaPorProducto b) {
                return b.cantidadVendida - a.cantidadVendida;
            }
        });
        VentaPorProducto topProd = ventasProductos.get(0);
        VentaPorProducto lowProd = ventasProductos.get(ventasProductos.size() - 1);

        System.out.println("=== PRODUCTO MÁS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n",
            topProd.nombreProducto, topProd.idProducto, topProd.cantidadVendida);

        System.out.println("=== PRODUCTO MENOS VENDIDO ===");
        System.out.printf("%s (%s): %d unidades%n",
            lowProd.nombreProducto, lowProd.idProducto, lowProd.cantidadVendida);

        // Categorías
        ventasCategorias.sort(new Comparator<VentaPorCategoria>() {
            public int compare(VentaPorCategoria a, VentaPorCategoria b) {
                return b.cantidadVendida - a.cantidadVendida;
            }
        });
        VentaPorCategoria topCat = ventasCategorias.get(0);
        System.out.println("=== CATEGORÍA MÁS VENDIDA ===");
        System.out.printf("%s: %d unidades%n",
            topCat.categoria, topCat.cantidadVendida);
    }
}
