package Proceso;

import Monitor.Monitor;
import RdP.RdP;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Proceso implements Runnable {
    private final Monitor monitor;
    private final RdP rdp;
    private final CyclicBarrier barrier;
    private final int[] secuencia;
    private final int secuencias_MAX;

    public Proceso(int[] secuencia, Monitor monitor, RdP rdp, CyclicBarrier barrier, int secuencias_MAX){
        this.monitor    = monitor   ;
        this.rdp        = rdp       ;
        this.barrier    = barrier   ;
        this.secuencia  = secuencia ;
        this.secuencias_MAX = secuencias_MAX;
    }

    @Override
    public void run() {

        boolean continuar = true;
        int contadorDisparos  = 0;
        int contadorSecuencia = 0;

        while(continuar){

            // disparo la secuencia invariante recientemente asignada
            for (int k : secuencia) {

                // se puede avanzar. apagar es false
                monitor.dispararTransicion(k);

                if (rdp.isApagada())
                {
                    // SE TERMINO EL PROGRAMA
                    continuar = false;
                    break;
                }

                // ACA HAGO LO QUE TENGA QUE HACER (TRABAJO FUERA DEL MONITOR)
                // --------------- TRABAJO ------------------
                for (int j = 0; j < 10000; j++)
                {
                    int trabajo_duro = 0;
                    for (int p = 0; p < 10000; p++)
                    {
                        trabajo_duro ++;
                    }
                }
                // --------------------------------------------
                contadorDisparos++;
            }

            contadorSecuencia ++;

            // para la secuencia 0 (T0), solo la disparo invariantes_MAX para que la rdp vuelva al marcado incial
            // y se pueda usar la regex para validar el log
            if (contadorSecuencia == secuencias_MAX) {
                contadorSecuencia ++;
                break;
            }
        }

        contadorSecuencia --;
        System.out.println(Thread.currentThread().getName()+" \tFINALIZO, disparo: "+contadorDisparos+" \tTransiciones y "+ contadorSecuencia+" \tSecuencias" );

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
