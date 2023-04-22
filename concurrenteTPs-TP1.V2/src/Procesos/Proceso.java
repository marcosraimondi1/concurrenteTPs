package Procesos;

import Util.Contenedor;

public abstract class Proceso implements Runnable {
    protected final Contenedor contenedor;
    protected final long demora; // demora del proceso en ms

    public Proceso(Contenedor contenedor, long demora) {
        this.contenedor = contenedor;
        this.demora = demora;
    }

}
