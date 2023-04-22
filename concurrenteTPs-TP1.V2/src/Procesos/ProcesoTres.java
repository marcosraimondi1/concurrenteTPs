package Procesos;

import Util.Contenedor;

public class ProcesoTres extends Proceso {
    public ProcesoTres(Contenedor contenedor, long demora) {
        super(contenedor, demora);
    }

    private void ajustar() {
        try {
            Thread.sleep(demora);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        ajustar();
    }
}
