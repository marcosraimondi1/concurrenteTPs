package Procesos;

import Util.Contenedor;
import Util.Imagen;

public class ProcesoUno extends Proceso {
    private int loaded = 0;

    public ProcesoUno(Contenedor contenedor, long demora) {
        super(contenedor, demora);
    }

    private boolean cargar() {
        try {
            Thread.sleep(demora);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return contenedor.addImage(new Imagen(), true);
    }

    @Override
    public void run() {
        while (cargar()) {
            loaded++;
        }
        System.out.printf("\nHilo %s cargo %d imagenes --------------------", Thread.currentThread().getName(), loaded);
    }
}
