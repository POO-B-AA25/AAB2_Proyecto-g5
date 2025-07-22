package Controlador;

import java.util.ArrayList;

public class ImpuestoDeducible {
    public double porcentajeDeducible;
    public ArrayList<String> categorias;

    public ImpuestoDeducible() {
        this.categorias = new ArrayList<>();
        inicializarCategorias();
    }

    public ImpuestoDeducible(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    private void inicializarCategorias() {
        categorias.add("ALIMENTACION");
        categorias.add("EDUCACION");
        categorias.add("VIVIENDA");
        categorias.add("VESTIMENTA");
        categorias.add("SALUD");
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

    public double calcularValorDeducible(String categoria, double monto) {
        return getPorcentaje(categoria) * monto;
    }

    public double aplicarDeduccion(double totalBruto) {
        return totalBruto * 0.95;
    }
}