package Procesos;

import Util.Contenedor;

import java.util.ArrayList;

public class ProcesoDos extends Proceso {
    private final ArrayList<Integer> mejoradas;

    public ProcesoDos(Contenedor contenedor, long demora) {
        super(contenedor, demora);
        mejoradas = new ArrayList<>();
    }

    private void mejorar() {
        try {
            Thread.sleep(demora);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        mejorar();
    }
}
