package Controlador;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;


public abstract class EstadisticaVentas {
    public double montoTotal;

    public EstadisticaVentas() {
        this.montoTotal = 0.0;
    }

    public void registrarVenta(LineaFactura lineaFactura) {
        this.montoTotal += lineaFactura.subtotalLinea;
        calcularEstadistica(lineaFactura);
    }

    // Método abstracto que implementan las clases hijas
    public abstract void calcularEstadistica(LineaFactura lineaFactura);
    
    public abstract void mostrarMasMenosVendidos();
    
    public abstract void guardarEstadisticasCsv();

    @Override
    public abstract String toString();
}