package Proceso;

import Monitor.Monitor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Proceso implements Runnable {
    private final Monitor monitor;
    private final CyclicBarrier barrier;
    private final int[] secuencia;
    private final int secuencias_MAX;

    public Proceso(int[] secuencia, Monitor monitor, CyclicBarrier barrier, int secuencias_MAX){
        this.monitor    = monitor   ;
        this.barrier    = barrier   ;
        this.secuencia  = secuencia ;
        this.secuencias_MAX = secuencias_MAX;
    }

    @Override
    public void run() {

        boolean continuar = true;
        int contadorDisparos  = 0;
        int contadorSecuencia = 0;
        long tiempoConHandling = 0;
        long timeInicial = System.currentTimeMillis();

        while(continuar){

            // Dispara la secuencia invariante recientemente asignada
            for (int k : secuencia) {


                monitor.dispararTransicion(k);

                // Se puede avanzar. apagar es false
                if (!monitor.seAvanza())
                {
                    // Se termino el programa
                    continuar = false;
                    break;
                }

                // ACA HAGO LO QUE TENGA QUE HACER (TRABAJO FUERA DEL MONITOR)
                // --------------- TRABAJO ------------------
                //for (int j = 0; j < 10000; j++)
                //{
                //    int trabajo_duro = 0;
                //    for (int p = 0; p < 10000; p++)
                //    {
                //        trabajo_duro ++;
                //    }
                //}
                // --------------------------------------------
                contadorDisparos++;
            }


            contadorSecuencia ++;

            // Para la secuencia 0 (T0), solo la disparo invariantes_MAX para que la rdp vuelva al marcado inicial
            // y se pueda usar la regex para validar el log
            if (contadorSecuencia == secuencias_MAX) {
                contadorSecuencia ++;
                break;
            }
        }

        contadorSecuencia --;
        long timeFinal = System.currentTimeMillis();
        tiempoConHandling = (tiempoConHandling+(timeFinal-timeInicial))/contadorSecuencia;

        System.out.println(Thread.currentThread().getName()+" \tFINALIZO, disparo: "+contadorDisparos+" \tTransiciones y "+ contadorSecuencia+" \tSecuencias, observando un" +" Tiempo promedio con Handling: "+tiempoConHandling+ "[ms]");

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Error en la Barrera - "+Thread.currentThread().getName());
        }
    }
}
