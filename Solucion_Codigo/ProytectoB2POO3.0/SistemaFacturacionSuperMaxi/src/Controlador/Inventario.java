package Controlador;

import java.util.ArrayList;

public class Inventario {
    public ArrayList<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
    }

    public Inventario(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    /** Busca un producto por ID; retorna null si no existe */
    public Producto buscarProducto(String id) {
        for (Producto p : productos) {
            if (p.idProducto.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void listarInventario() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("ID       | Nombre                    | Categoría    |  Precio  | Stock | Caduca   | Tipo");
        System.out.println("--------------------------------------------------------------------------");
        for (Producto p : productos) {
            String tipo = (p instanceof ProductoPromocional) ? "PROMOCIONAL" : "NORMAL";
            System.out.println(p.toString() + " | " + tipo);
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTARIO ===\n");
        for (Producto p : productos) {
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }
}
