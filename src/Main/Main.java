package Main;

import Monitor.Monitor;
import Politica.*;
import RdP.RdP;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static Constants.Constants.*;

public class Main {
    public static void main(String[] args) {
        //------------------------------Inicio Politica----------------------------------------------//
        boolean     usarPolitica1   = POLITICA1;

        Politica    politica1   = new Politica1(CONFLICTOS_TP2);
        Politica    politica2   = new Politica2(CONFLICTOS_TP2);
        Politica    politica    = usarPolitica1 ? politica1 : politica2;

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][]     plaza_salida        = W_MAS_TP2         ; // plazas a la salida de la transición (Matriz)
        int[][]     plaza_entrada       = W_MENOS_TP2       ; // plazas a la entrada de la transición
        int[]       marcado             = MI_TP2            ; // marcado inicial
        long[][]    tiempos             = TIEMPOS           ;
        int         invariantes_MAX     = INVARIANTES_MAX   ;
        int[]       trans_invariantes   = T_INV_TP2         ; // transiciones para contar invariantes (T14 marca una vuelta)
        int[][]     invariantes_plazas  = P_INV_TP2         ;

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado, trans_invariantes,invariantes_plazas,tiempos,invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = Monitor.getMonitor(rdp,politica);

        // Declaro las secuencias de disparo para los hilos
        int[] secuencia1    = {0        };
        int[] secuencia2    = {1,   3   };
        int[] secuencia3    = {2,   4   };
        int[] secuencia4    = {5,   7   };
        int[] secuencia5    = {6,   8   };
        int[] secuencia6    = {11       };
        int[] secuencia7    = {12       };
        int[] secuencia8    = {13   ,14 };

        // poner hilos de  dicados a 9 y 10 para que funcione bien la politica2
        int[] secuencia9    = {9};
        int[] secuencia10   = {10};


//        Declaro las secuencias de disparo para los hilos
//        int[] secuencia1    = {0}       ;
//        int[] secuencia2    = {1}       ;
//        int[] secuencia3    = {3}       ;
//        int[] secuencia4    = {2}       ;
//        int[] secuencia5    = {4}       ;
//        int[] secuencia6    = {5}       ;
//        int[] secuencia7    = {7}       ;
//        int[] secuencia8    = {6}       ;
//        int[] secuencia9    = {8}       ;
//        int[] secuencia10   = {9}       ; // Hilo 9
//        int[] secuencia11   = {11}      ;
//        int[] secuencia12   = {10}      ; // Hilo 11
//        int[] secuencia13   = {12}      ;
//        int[] secuencia14   = {13,14}   ;

        int[][] secuencias  = {secuencia1,secuencia2,secuencia3,secuencia4,secuencia5,secuencia6,secuencia7,secuencia8,secuencia9,secuencia10};//,secuencia11,secuencia12,secuencia13,secuencia14};

        Thread [] threads   = new Thread[secuencias.length];

        CyclicBarrier cyclic = new CyclicBarrier( threads.length + 1,() -> {
            // al terminar el programa verifico que se cumplan los invariantes

            System.out.println("Cantidad De Invariantes Completados : "+rdp.getCuentaInvariantes());
            System.out.println("Marcado Final: ");
            System.out.println(Arrays.toString(rdp.getMarcadoActual()));

            System.out.println("Contadores de transiciones: ");
            System.out.println(Arrays.toString(rdp.getContadores()));

            rdp.logger.validateLog(REGEX,REPLACE);
        });


        for (int i = 0; i< threads.length; i++)
        {
            int finalI = i;
            threads[i] = new Thread(()->{

                boolean condicion = true;
                int contadorDisparos = 0;
                while(condicion){

                    int[] secuencia = secuencias[finalI]; // selecciono una de las 5 secuancias

                    // disparo la secuencia invariante
                    for (int k : secuencia) {
                        if(!monitor.seAvanza()){
                            condicion = false;
                            break;
                        }else {
                            monitor.dispararTransicion(k);
                            contadorDisparos++;
                            if (finalI == 0 && contadorDisparos >= invariantes_MAX)
                            {
                                // la transicion 0 se disparo 200 veces
                                condicion = false;
                                break;
                            }
                        }

                    }



                }
                System.out.println(Thread.currentThread().getName()+" FINALIZO, disparo: "+contadorDisparos+" Transiciones de la secuencia "+ finalI);
                try {
                    cyclic.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        System.out.println("Inicio los hilos");
        try {
            cyclic.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }

}

