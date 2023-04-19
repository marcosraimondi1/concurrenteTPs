package Procesos;

import Util.Contenedor;

public class ProcesoCuatro extends Proceso {
    private Contenedor contenedorFinal;

    public ProcesoCuatro(Contenedor contenedor, Contenedor contenedorFinal, long demora) {
        super(contenedor, demora);
        this.contenedorFinal = contenedorFinal;
    }

    private void copiar() {
        try {
            Thread.sleep(demora);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        copiar();
    }
}
