package Modelo;

import java.io.File;

public class GestorArchivos {
    
    public void crearDirectorios() {
        File dirFacturas = new File("facturas");
        if (!dirFacturas.exists()) {
            boolean created = dirFacturas.mkdirs();
            if (created) {
                System.out.println("[INFO] Directorio 'facturas' creado");
            }
        }
    }
}