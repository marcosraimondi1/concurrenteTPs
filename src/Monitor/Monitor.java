package Monitor;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import Politica.Politica;
import RdP.RdP;
import Cola.Cola;

public class Monitor {
    private Semaphore mutex;
    private Politica politica;
    private RdP red;
    private Cola[] colas;
    private boolean k = false;
    private boolean[] m;
    public Monitor(RdP red, Politica politica){
        mutex           = new Semaphore(1,true);
        this.politica   = politica;
        this.red        = red;
        this.m          = new boolean[red.getCantidadTransiciones()];
        this.colas      = new Cola[red.getCantidadTransiciones()];

        // se crea una cola para cada transicion
        for (int i = 0; i < red.getCantidadTransiciones(); i++) {
            colas[i] = new Cola();
        }


    }

    /**
     * Intenta disparar una transicion
     * @param transicion transicion a disparar
     */
    public void dispararTransicion(int transicion){

        try {
            mutex.acquire();                //si no puedo tomar el mutex me voy a esperar a la cola de entrada
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Seccion Critica ---------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        k = true;
        while (k) {
            k = red.disparar(transicion);
            if (k) {
                boolean[] sensibilizadas        = red.getSensibilizadas();
                boolean[] transicionesConEspera = hayEsperando();

                // m = sensibilizadas AND transicionesConEspera

                for (int i = 0; i < sensibilizadas.length; i++) {
                    m[i] = sensibilizadas[i] && transicionesConEspera[i];
                }

                if(!mVacio()){
                    // hay transiciones que se pueden disparar elegimos una
                    int indexDisparo = politica.cual(m);
                    colas[indexDisparo].release();           // debe liberar el hilo que va a disparar
                    return; //me vuelvo porque termine
                }else{
                    k = false;
                }
            } else{
                // libero el acceso al monitor
                mutex.release();

                //me voy a esperar a la cola correspondiente a la transicion que quiero disparar
                colas[transicion].acquire();
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Fin Seccion Critica -----------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        mutex.release();

    }

    /**
     * Devuelve un array de booleanos que indica si hay hilos esperando en cada cola
     * @return  boolean[]
     */
    private boolean[] hayEsperando(){
        boolean[] transicionesConEspera = new boolean[colas.length];
        for (int i = 0; i < colas.length; i++) {
            transicionesConEspera[i] = colas[i].hayEsperando();
        }
        return transicionesConEspera;
    }

    /**
     * Devuelve true si el array m no tiene transiciones esperando (si estan todos en false)
     * @return boolean
     */
    private boolean mVacio(){
        for (int i = 0 ; i < m.length ; i++) {
            if (m[i]) {
                return false;
            }
        }
        return true;
    }


}
