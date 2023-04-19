package Procesos;

import Util.Contenedor;

public class ProcesoUno extends Proceso {
    public ProcesoUno(Contenedor contenedor, long demora) {
        super(contenedor, demora);
    }

    private void cargar() {
        try {
            Thread.sleep(demora);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        cargar();
    }
}
