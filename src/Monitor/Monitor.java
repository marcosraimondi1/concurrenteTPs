package Monitor;

import java.util.concurrent.Semaphore;

import Politica.Politica;
import RdP.RdP;
import Colas.Colas;

public class Monitor {
    //Variables
    private Semaphore mutex;
    private Politica politica;
    private RdP red;
    private Colas colas;
    private int[] quienesEstan;
    private Boolean k;

    //Constructor
    public Monitor(RdP redParam, Politica politicaParam,Colas colasParam ){
        mutex = new Semaphore(1,true); //semaforo binario y justo
      politica = politicaParam;
      red = redParam;
      colas = colasParam;
      k = false;

    }

    /**
     * @param Transicion el index de la transicion a disparar por el hilo
     * @return true si se pudo disparar, false si no
     */
    public void DispararTransicion(int Transicion){
       // hacer un acquire() sobre el mutex




    }



}
