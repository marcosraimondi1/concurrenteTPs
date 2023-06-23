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
    private boolean k;
    private int[] m;
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
     */
    public void DispararTransicion(int Transicion){

        try {
            mutex.acquire(); //si no puedo tomar el mutex me voy a esperar a la cola de entrada
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //------------Accede un solo hilo-----------------------------------------------------------
         k = true;
        while(k){
            k = red.disparar(Transicion);
            if(k==true){
                boolean[] sensibilizadas = red.getSensibilizadas();
                int[] quienesEstan = colas.quienesEstan();
                //hago un and de los arreglos y declamos m
                m[1] = 1;//hay que definirla bien

                if(m.length!=0){ //verificar si son todos ceros NO ESTA BIEN m.length!=0
                   int indexDisparo = politica.cual(m);
                   colas.getCola(indexDisparo).release(); //debe liberar el hilo que va a disparar
                   return; //me vuelvo porque termine
                }else{
                    k = false;
                }

            }else if(k==false){
                //Libero el acceso al monitor
               mutex.release();
                //me voy a esperar a la cola correspondiente a la transicion que quiero disparar
                try {
                    colas.getCola(Transicion).acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }
        mutex.release();
        //------------------------------------------------------------------------------------------


    }



}
