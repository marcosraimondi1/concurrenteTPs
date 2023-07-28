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
    private final Colas colas;  // tendremos una cola por cada transicion
    private boolean k;
    private Monitor(RdP red, Politica politica){
        this.politica   = politica;
        this.red        = red;

        mutex           = new Semaphore(1);
        this.colas      = new Colas(red.getCantidadTransiciones());
        k = false;
    }

    /**
     * crea el monitor en caso de que no se lo haya hecho antes y lo retorna
     * @param red
     * @param politica
     * @return monitor.
     */
    public static Monitor getMonitor(RdP red, Politica politica){
        if(monitor == null){
            monitor = new Monitor(red, politica);
        }
        return monitor;
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
                //se puede disparar la transicion
                boolean[] sensibilizadas        = red.getSensibilizadas();
                boolean[] transicionesConEspera = colas.hayEsperando();
                boolean[] transicionesConEsperaySensibilizadas = new boolean[sensibilizadas.length];

                for (int i = 0; i < sensibilizadas.length; i++) {
                    transicionesConEsperaySensibilizadas[i] = sensibilizadas[i] && transicionesConEspera[i];
                }

                if(!todoFalso(transicionesConEsperaySensibilizadas)){
                    // hay transiciones que se pueden disparar, la politica elige una
                    int indexDisparo = politica.cual(transicionesConEsperaySensibilizadas);

                    colas.getCola(indexDisparo).sacar();      // debe liberar el hilo que va a disparar
                    return;                                   // me vuelvo porque termine
                }else{
                    k = false;
                }
            } else{
                // NO se puede disparar la transicion
                // libero el acceso al monitor
                if (mutex.availablePermits() != 0) {
                    throw new RuntimeException("Se corrompio el mutex de entrada");
                }

                if(seAvanza()) {
                    // osea si apagar es false
                    // me voy a esperar a la cola correspondiente a la transicion que quiero disparar
                    mutex.release();
                    colas.getCola(transicion).esperar();    // acquire
                }
                else{
                    // apagar es true
                    // si se corto la ejecución despierto a los hilos uno a uno.
                    boolean libero = liberar(); // veo si puedo liberar un hilo que espera en una cola de condición
                    if(libero)
                    {
                        k = true; // si libero pongo el k en true para que cuando despierte no se vaya del while
                        return;
                    }
                    mutex.release(); // no habia hilos esperando en la cola de condición libero el mutex por si esperan en la entrada
                    return;
                }
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------ Fin Seccion Critica -----------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        mutex.release();
    }

    /**
     * Devuelve true si en el array estan todos los elementos en falso, osea si no hay transiciones sensibilizadas
     * con algun hilo esperando
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

    /**
     * Hace release a cada hilo que se encuentra esperando en la cola de su respectiva transicion, una vez que
     * seAvanza sea false
     * @return boolean
     */
    private boolean liberar(){
        boolean libero =false;
        boolean[] HilosEnEspera = colas.hayEsperando();
        for (int i = 0; i < HilosEnEspera.length ; i++) {
            if(HilosEnEspera[i]){
                libero = true;
                colas.getCola(i).sacar();
                break;
            }
        }
        return libero;
    }

    /**
     * lee el estado de la variable "apagar" de la red. Si apagar es true entonces NO seAvanza, sino si.
     * @return boolean
     */
    public boolean seAvanza(){
        return !red.getApagar();
    }

    public Semaphore getMutex() {
        return mutex;
    }
}
