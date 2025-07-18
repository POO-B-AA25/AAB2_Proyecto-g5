package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Inventario {
    private int idInventario;
    private Date inventarioFechaCaducidad;
    private Object inventarioProducto;
    private ArrayList<Producto> inventarioProductoArrayListProducto;
    private List<Producto> inventarioProductoListaProducto;
    
    // Mantener compatibilidad con código existente
    public ArrayList<Producto> productos;

    public Inventario() {
        this.idInventario = 1;
        this.inventarioFechaCaducidad = new Date();
        this.inventarioProducto = new Object();
        this.inventarioProductoArrayListProducto = new ArrayList<>();
        this.inventarioProductoListaProducto = new ArrayList<>();
        
        // Mantener compatibilidad
        this.productos = this.inventarioProductoArrayListProducto;
    }
    
    public Inventario(int idInventario, Date inventarioFechaCaducidad) {
        this.idInventario = idInventario;
        this.inventarioFechaCaducidad = inventarioFechaCaducidad;
        this.inventarioProducto = new Object();
        this.inventarioProductoArrayListProducto = new ArrayList<>();
        this.inventarioProductoListaProducto = new ArrayList<>();
        
        // Mantener compatibilidad
        this.productos = this.inventarioProductoArrayListProducto;
    }

    /** Agrega un producto nuevo */
    public void agregarProducto(Producto p) {
        inventarioProductoArrayListProducto.add(p);
        inventarioProductoListaProducto.add(p);
    }
    
    /** Elimina un producto del inventario */
    public void eliminarProducto(Producto p) {
        inventarioProductoArrayListProducto.remove(p);
        inventarioProductoListaProducto.remove(p);
    }

    /** Busca un producto por ID; retorna null si no existe */
    public Producto buscarProducto(String id) {
        for (Producto p : inventarioProductoArrayListProducto) {
            if (p.getIdProducto().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    /** Busca inventario por ID */
    public void buscarInventario(int id) {
        if (this.idInventario == id) {
            System.out.println("Inventario encontrado: ID " + id);
            listarInventario();
        } else {
            System.out.println("Inventario con ID " + id + " no encontrado");
        }
    }

    /** Muestra internamente una tabla con todos los productos del inventario */
    public void listarInventario() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("ID       | Nombre                    | Categoría    |  Precio  | Stock | Caduca");
        System.out.println("--------------------------------------------------------------------------");
        for (Producto p : inventarioProductoArrayListProducto) {
            System.out.println(p.toString());
        }
        System.out.println("--------------------------------------------------------------------------");
    }
    
    // Getters y Setters
    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public Date getInventarioFechaCaducidad() {
        return inventarioFechaCaducidad;
    }

    public void setInventarioFechaCaducidad(Date inventarioFechaCaducidad) {
        this.inventarioFechaCaducidad = inventarioFechaCaducidad;
    }

    public Object getInventarioProducto() {
        return inventarioProducto;
    }

    public void setInventarioProducto(Object inventarioProducto) {
        this.inventarioProducto = inventarioProducto;
    }

    public ArrayList<Producto> getInventarioProductoArrayListProducto() {
        return inventarioProductoArrayListProducto;
    }

    public void setInventarioProductoArrayListProducto(ArrayList<Producto> inventarioProductoArrayListProducto) {
        this.inventarioProductoArrayListProducto = inventarioProductoArrayListProducto;
        this.productos = inventarioProductoArrayListProducto; // Mantener sincronización
    }

    public List<Producto> getInventarioProductoListaProducto() {
        return inventarioProductoListaProducto;
    }

    public void setInventarioProductoListaProducto(List<Producto> inventarioProductoListaProducto) {
        this.inventarioProductoListaProducto = inventarioProductoListaProducto;
    }
}