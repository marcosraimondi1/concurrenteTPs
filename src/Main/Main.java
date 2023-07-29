package Main;

import Logger.Logger;
import Monitor.Monitor;
import Politica.*;
import RdP.RdP;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static Constants.Constants.*;

public class Main {
    public final static Logger stateLogger = new Logger(STATE_LOG_PATH);
    private static RdP rdp;
    public static void main(String[] args) {
        //------------------------------Inicio Politica----------------------------------------------//
        boolean     usarPolitica1   = POLITICA1;

        Politica    politica1   = new Politica1(CONFLICTOS_TP2); // politica1 es la de 50-50
        Politica    politica2   = new Politica2(CONFLICTOS_TP2,CONFLICTOS_TP2_80); // politica2 es la de 80-20
        Politica    politica    = usarPolitica1? politica1 : politica2; // guardamos la politica a utilizar

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][]     plaza_salida        = W_MAS_TP2         ; // plazas a la salida de la transición (Matriz)
        int[][]     plaza_entrada       = W_MENOS_TP2       ; // plazas a la entrada de la transición (Matriz)
        int[]       marcado             = MI_TP2            ; // marcado inicial
        long[][]    tiempos             = TIEMPOS           ; // tiempo de cada transicion
        int         invariantes_MAX     = INVARIANTES_MAX   ; // cantidad de invariantes a realizar
        int[]       trans_invariantes   = T_INV_TP2         ; // transiciones para contar invariantes (T14 marca una vuelta)
        int[][]     invariantes_plazas  = P_INV_TP2         ; // invariantes de plaza de la red

        rdp = new RdP(plaza_salida,plaza_entrada,marcado, trans_invariantes,invariantes_plazas,tiempos,invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = Monitor.getMonitor(rdp,politica);

        //------------------------------Inicio Hilos-------------------------------------------------//

        int[][] secuencias = GET_SECUENCIAS_TP2(); // secuencias de disparo asignadas para cada hilo

        Thread [] threads   = new Thread[secuencias.length];

        long time = System.currentTimeMillis(); // tiempo al inicio del programa

        CyclicBarrier cyclic = new CyclicBarrier( threads.length + 1,() -> {

            // al terminar el programa verifico que se cumplan los invariantes

            System.out.println("Cantidad de Invariantes Completados : "+rdp.getCuentaInvariantes());
            System.out.println("Marcado Final: ");
            System.out.println(Arrays.toString(rdp.getMarcadoActual()));

            System.out.println("Contadores de transiciones: ");
            System.out.println(Arrays.toString(rdp.getContadores()));

            long timeFinal = System.currentTimeMillis();// tiempo al final del programa
            System.out.println("Tiempo final de ejecución: "+ (timeFinal-time)+" [mSeg]");

            rdp.logger.validateLog(REGEX,REPLACE);
        });


        for (int i = 0; i< threads.length; i++)
        {
            int finalI = i;

            threads[i] = new Thread(()->{

                boolean condicion = true;
                int contadorDisparos = 0;

                while(condicion){

                    int[] secuencia = secuencias[finalI]; // selecciono una de las 8 secuencias

                    // disparo la secuencia invariante recientemente asignada
                    for (int k : secuencia) {
                        if(!monitor.seAvanza()){ // NO se puede avanzar. apagar es true
                            condicion = false;
                            break;
                        }else {
                            // se puede avanzar. apagar es false
                            monitor.dispararTransicion(k);
                            contadorDisparos++;
                            if (finalI == 0 && contadorDisparos >= invariantes_MAX)
                            {
                                contadorDisparos++;
                                // la transicion 0 se disparo INVARIANTES_MAX veces
                                condicion = false;
                                break;
                            }
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName()+" FINALIZO, disparo: "+(contadorDisparos-1)+" Transiciones de la secuencia "+ finalI);
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

        stateLogger(threads);

        System.out.println("Hilos iniciados");

        //------------------------------Sincronizo Hilos al finalizar---------------------------------------//

        try {
            cyclic.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }

    private static void stateLogger(Thread[] threads){

        long startTime = System.currentTimeMillis();

        Thread stateLoggerThread = new Thread(()->{

            while (true) {

                String  marcadoActual = Arrays.toString(rdp.getMarcadoActual());
                String  contadores    = Arrays.toString(rdp.getContadores());
                int     invariantes   = rdp.getCuentaInvariantes();
                long    runningTime   = System.currentTimeMillis() - startTime;

                stateLogger.logn(formatLog("INFO", "MAIN", "TIME"    , String.valueOf(runningTime)   ));
                stateLogger.logn(formatLog("INFO", "MAIN", "MARCADO" , marcadoActual                 ));
                stateLogger.logn(formatLog("INFO", "MAIN", "SHOTS"   , contadores                    ));
                stateLogger.logn(formatLog("INFO", "MAIN", "INV"     , String.valueOf(invariantes)   ));

                int aliveThreads = 0;
                int runningThreads = 0;
                for (Thread thread : threads) {
                    if (thread.isAlive())
                        aliveThreads++;
                    if (thread.getState() == Thread.State.RUNNABLE)
                        runningThreads++;

                    stateLogger.logn(formatLog("INFO", "MAIN", "THREAD", thread.getName() + " " + thread.getState()));
                }

                String threadInfo = String.format("Alive: %1$d , Running: %2$d", aliveThreads, runningThreads);
                stateLogger.logn(formatLog("INFO", "MAIN", "THREAD"  , threadInfo                    ));
                stateLogger.logn(formatLog("INFO", "MAIN", "EOM"  , "--------------------------------"));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        stateLoggerThread.setDaemon(true);
        stateLoggerThread.start();

    }

    private static String formatLog(String logLevel, String step, String caller, String message){
        // Get the current date in the desired format
        String date = new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(new Date());

        // Format the log message
        return String.format("[%1$-10s] %2$s [%3$-4s] %4$s: %5$s",
                logLevel, date, step, caller, message);
    }
}

