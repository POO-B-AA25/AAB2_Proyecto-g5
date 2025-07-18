package modelo;

import java.util.Date;
import java.util.List;

public class Deducible {
    private double porcentajeDescuento;
    private Date fechaInicioDescuento;
    private Date fechaFinDescuento;
    private int tipoDescuento;
    private String descripcionDescuento;
    
    public Deducible() {
        this.porcentajeDescuento = 0.0;
        this.fechaInicioDescuento = new Date();
        this.fechaFinDescuento = new Date();
        this.tipoDescuento = 0;
        this.descripcionDescuento = "";
    }
    
    public Deducible(double porcentajeDescuento, Date fechaInicioDescuento, 
                    Date fechaFinDescuento, int tipoDescuento, String descripcionDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaInicioDescuento = fechaInicioDescuento;
        this.fechaFinDescuento = fechaFinDescuento;
        this.tipoDescuento = tipoDescuento;
        this.descripcionDescuento = descripcionDescuento;
    }
    
    public double getPorcentaje(String categoria) {
        String cat = categoria.trim().toUpperCase();
        if (cat.equals("ALIMENTACION")) {
            return 0.35;
        } else if (cat.equals("EDUCACION")) {
            return 0.30;
        } else if (cat.equals("VIVIENDA")) {
            return 0.20;
        } else if (cat.equals("VESTIMENTA")) {
            return 0.25;
        } else if (cat.equals("SALUD")) {
            return 0.40;
        } else {
            return 0.0;
        }
    }

    /** Calcula el monto deducible: porcentaje × monto */
    public double calcularValorDeducible(String categoria, double monto) {
        return getPorcentaje(categoria) * monto;
    }
    
    public double calcularDescuento(String categoria, List<String> categoriaListaCategoria) {
        // Verificar si la categoría está en la lista de categorías válidas
        if (categoriaListaCategoria != null && categoriaListaCategoria.contains(categoria.toUpperCase())) {
            // Verificar si el descuento está vigente
            Date fechaActual = new Date();
            if (fechaActual.after(fechaInicioDescuento) && fechaActual.before(fechaFinDescuento)) {
                return porcentajeDescuento;
            }
        }
        return 0.0;
    }
    
    public void actualizarDescuento(String categoria, List<String> categoriaListaCategoria) {
        if (categoriaListaCategoria != null && categoriaListaCategoria.contains(categoria.toUpperCase())) {
            // Actualizar el descuento según la categoría
            switch (categoria.toUpperCase()) {
                case "ALIMENTACION":
                    this.porcentajeDescuento = 0.10;
                    break;
                case "EDUCACION":
                    this.porcentajeDescuento = 0.15;
                    break;
                case "VIVIENDA":
                    this.porcentajeDescuento = 0.08;
                    break;
                case "VESTIMENTA":
                    this.porcentajeDescuento = 0.12;
                    break;
                case "SALUD":
                    this.porcentajeDescuento = 0.20;
                    break;
                default:
                    this.porcentajeDescuento = 0.05;
            }
        }
    }
    
    // Getters y Setters
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Date getFechaInicioDescuento() {
        return fechaInicioDescuento;
    }

    public void setFechaInicioDescuento(Date fechaInicioDescuento) {
        this.fechaInicioDescuento = fechaInicioDescuento;
    }

    public Date getFechaFinDescuento() {
        return fechaFinDescuento;
    }

    public void setFechaFinDescuento(Date fechaFinDescuento) {
        this.fechaFinDescuento = fechaFinDescuento;
    }

    public int getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(int tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public String getDescripcionDescuento() {
        return descripcionDescuento;
    }

    public void setDescripcionDescuento(String descripcionDescuento) {
        this.descripcionDescuento = descripcionDescuento;
    }
}