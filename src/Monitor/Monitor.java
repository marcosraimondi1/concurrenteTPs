package Monitor;

import java.util.concurrent.Semaphore;

import Politica.Politica;
import RdP.RdP;
import Cola.Colas;

/**
 * Clase que representa el monitor de concurrencia de una Red De Petri
 * Utiliza el patron Singleton
 * @see RdP
 */
public class Monitor {
    private static Monitor monitor = null;
    private final Semaphore mutex;
    private final Politica politica;
    private final RdP red;
    private final Colas colas;  // Tendremos una cola por cada transicion
    private boolean k;
    private Monitor(RdP red, Politica politica){
        this.politica   = politica;
        this.red        = red;

        mutex           = new Semaphore(1);
        this.colas      = new Colas(red.getCantidadTransiciones());
        k = false;
    }

    /**
     * Crea el monitor en caso de que no se lo haya hecho antes y lo retorna
     * @param red red de petri a la que se le va a aplicar el monitor
     * @param politica politica a utilizar
     * @return monitor.
     */
    public static Monitor getMonitor(RdP red, Politica politica){
        if(monitor == null){
            monitor = new Monitor(red, politica);
        }
        return monitor;
    }

    public static void resetMonitor(){
        monitor = null;
    }

    /**
     * @return monitor.
     */
    public static Monitor getMonitor(){
        return monitor;
    }

    /**
     * Intenta disparar una transicion
     * @param transicion a disparar
     */
    public void dispararTransicion(int transicion){

        try {
            mutex.acquire();                // Si no puede tomar el mutex, va a esperar a la cola de entrada
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
                // Se puede disparar la transicion
                boolean[] sensibilizadas        = red.getSensibilizadas();
                boolean[] transicionesConEspera = colas.hayEsperando();
                boolean[] transicionesConEsperaySensibilizadas = new boolean[sensibilizadas.length];

                for (int i = 0; i < sensibilizadas.length; i++) {
                    transicionesConEsperaySensibilizadas[i] = sensibilizadas[i] && transicionesConEspera[i];
                }

                if (!seAvanza())
                    // Si no se avanza, la red esta apagada
                    // Saca todos los hilos que esten esperando sin importar si estan sensibilizados
                    transicionesConEsperaySensibilizadas = transicionesConEspera;

                if(!todoFalso(transicionesConEsperaySensibilizadas)){
                    // Hay transiciones que se pueden disparar, la politica elige una
                    int indexDisparo = politica.cual(transicionesConEsperaySensibilizadas);

                    colas.getCola(indexDisparo).sacar();      // Debe liberar el hilo que va a disparar
                    return;                                   // Vuelve porque termino
                }else{
                    k = false;                                // Sale del while para hacer el release del mutex e ingrese otro hilo
                }

            } else{
                // NO se puede disparar la transicion
                if (mutex.availablePermits() != 0) {
                    throw new RuntimeException("Se corrompio el mutex de entrada");
                }

                // Va a esperar a la cola correspondiente a la transicion que quiere disparar y libera el mutex
                mutex.release();
                colas.getCola(transicion).esperar();    // acquire
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Fin Seccion Critica -----------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        mutex.release(); // Si no se libera a nadie, libera el mutex y se va

    }

    /**
     * Devuelve true si en el array estan todos los elementos en falso, o sea si no hay transiciones sensibilizadas
     * con algun hilo esperando
     * @return boolean
     */
    private boolean todoFalso(boolean[] m){
        for (boolean b : m) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lee el estado de la variable "apagar" de la red. Si apagar es true entonces NO seAvanza, sino si.
     * @return boolean
     */
    public boolean seAvanza(){
        return !red.isApagada();
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public int[] getCantidadEsperandoColas(){
        return colas.getCantidadEsperando();
    }
}
