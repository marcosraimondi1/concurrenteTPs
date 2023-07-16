package Monitor;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import Politica.Politica;
import RdP.RdP;
import Cola.Colas;

public class Monitor {
    private final Semaphore mutex;
    private final Politica politica;
    private final RdP red;
    private final Colas colas;
    private boolean k;
    public Monitor(RdP red, Politica politica){
        mutex           = new Semaphore(1,true);
        this.politica   = politica;
        this.red        = red;
        this.colas      = new Colas(red.getCantidadTransiciones());
        k = false;
    }

    /**
     * Intenta disparar una transicion
     * @param transicion transicion a disparar
     */
    public void dispararTransicion(int transicion){

        try {
            mutex.acquire();                // si no puedo tomar el mutex me voy a esperar a la cola de entrada
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Seccion Critica ---------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        k = true; // Variable de estado
        while (k) {
            k = red.disparar(transicion);
            if (k) {
                //System.out.println("Hilo "+Thread.currentThread().getName()+"Disparo trans "+transicion);
                boolean[] sensibilizadas        = red.getSensibilizadas();
                //System.out.println(Arrays.toString(sensibilizadas));
                boolean[] transicionesConEspera = colas.hayEsperando();

                // m = sensibilizadas AND transicionesConEspera
                boolean[] transicionesConEsperaySensibilizadas = new boolean[sensibilizadas.length];
                for (int i = 0; i < sensibilizadas.length; i++) {
                    transicionesConEsperaySensibilizadas[i] = sensibilizadas[i] && transicionesConEspera[i];
                }

                if(!todoFalso(transicionesConEsperaySensibilizadas)){
                    // hay transiciones que se pueden disparar elegimos una
                    int indexDisparo = politica.cual(transicionesConEsperaySensibilizadas);
                    colas.getCola(indexDisparo).sacar();      // debe liberar el hilo que va a disparar
                    return;                                   // me vuelvo porque termine
                }else{
                    k = false;
                }
            } else{
                // libero el acceso al monitor
                if (mutex.availablePermits() != 0) {
                    throw new RuntimeException("Se corrompio el mutex de entrada");
                }
                mutex.release();

                //me voy a esperar a la cola correspondiente a la transicion que quiero disparar
                colas.getCola(transicion).esperar();
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Fin Seccion Critica -----------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        mutex.release();
    }



    /**
     * Devuelve true si el array esta todos en falso
     * @return boolean
     */
    private boolean todoFalso(boolean[] m){
        for (int i = 0 ; i < m.length ; i++) {
            if (m[i]) {
                return false;
            }
        }
        return true;
    }

    public Semaphore getMutex() {
        return mutex;
    }
    public Colas getColas() {
        return colas;
    }
    public RdP getRed() {
        return red;//solo para un hilo de test no para entradas concurrentes
    }

}
