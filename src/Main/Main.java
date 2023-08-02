package Main;

import Logger.Logger;
import Monitor.Monitor;
import Proceso.Proceso;
import RdP.RdP;
import Politica.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static Constants.Constants.*;

public class Main {
    private final static Logger stateLogger = new Logger(STATE_LOG_PATH);
    private static RdP rdp;
    public static void main(String[] args) {
        //------------------------------Inicio Politica----------------------------------------------//

        Politica politica = getPolitica();

        //------------------------------Inicio RdP---------------------------------------------------//

        iniciarRdP();

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = Monitor.getMonitor(rdp,politica);

        //------------------------------Inicio de Prgrama -------------------------------------------------//

        iniciarPrograma(monitor);

        System.out.println("\n------------- FIN DE PROGRAMA -----------------\n");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static void iniciarPrograma(Monitor monitor) {
        int[][] secuencias  = GET_SECUENCIAS_TP2()      ;   // secuencias de disparo asignadas para cada hilo
        long    time        = System.currentTimeMillis();   // tiempo al inicio del programa

        Thread[] threads    = new Thread[secuencias.length];

        CyclicBarrier barrier = new CyclicBarrier( threads.length + 1,() -> {
            // al terminar el programa verifico que se cumplan los invariantes
            System.out.println("\nTiempo Total: "+(System.currentTimeMillis()-time)+" ms\n");
            System.out.println("\nVerificando Invariantes...\n");
            rdp.logger.validateLog(REGEX,REPLACE);

            int[] cuentas = rdp.logger.contarInvariantes(REGEX,REPLACE_INV);
            int total = 0;
            System.out.println("\nCuentas de Invariantes: ");
            for (int i = 0; i<cuentas.length; i++){
                System.out.println(i+".\t"+INVARIANTES[i]+"\t= "+cuentas[i]);
                total += cuentas[i];
            }
            System.out.println("TOTAL\t=\t"+total);
        });

        //----------------------------- Inicio Hilos ------------------------------------------------//

        threads[0] = new Thread(new Proceso(secuencias[0], monitor, barrier, INVARIANTES_MAX));
        for (int i = 1; i< threads.length; i++)
            threads[i] = new Thread(new Proceso(secuencias[i], monitor, barrier, -1));

        for (int i = 0; i< threads.length; i++){
            threads[i].setName("Secuencia "+i);
            threads[i].start();
        }

        System.out.println("Hilos iniciados");

        stateLogger(threads);             // inicio el logger de estado

        //----------------------------- Sincronizo Hilos al finalizar --------------------------------------//

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("NO SE PUDO VALIDAR LOG, PROBAR CON PYTHON");
        }
    }

    private static void iniciarRdP() {
        int[][]     plaza_salida        = W_MAS_TP2         ; // plazas a la salida de la transición (Matriz)
        int[][]     plaza_entrada       = W_MENOS_TP2       ; // plazas a la entrada de la transición (Matriz)
        int[]       marcado             = MI_TP2            ; // marcado inicial
        long[][]    tiempos             = TIEMPOS           ; // tiempo de cada transicion
        int         invariantes_MAX     = INVARIANTES_MAX   ; // cantidad de invariantes a realizar
        int[]       trans_invariantes   = T_INV_TP2         ; // transiciones para contar invariantes (T14 marca una vuelta)
        int[][]     invariantes_plazas  = P_INV_TP2         ; // invariantes de plaza de la red

        rdp = new RdP(plaza_salida,plaza_entrada,marcado, trans_invariantes,invariantes_plazas,tiempos,invariantes_MAX);
    }

    private static Politica getPolitica() {
        boolean     usarPolitica1   = POLITICA1;

        Politica    politica1   = new Politica1(CONFLICTOS_TP2); // politica1 es la de 50-50
        Politica    politica2   = new Politica2(CONFLICTOS_TP2,CONFLICTOS_TP2_80); // politica2 es la de 80-20
        return usarPolitica1 ? politica1 : politica2;
    }

    private static void stateLogger(Thread[] threads){

        long startTime = System.currentTimeMillis();

        Thread stateLoggerThread = new Thread(()->{
            while (true) {
                logState(threads, startTime);
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

    private static void logState(Thread[] threads, long startTime) {
        String  marcadoActual = Arrays.toString(rdp.getMarcadoActual());
        String  contadores    = Arrays.toString(rdp.getContadores());
        int     invariantes   = rdp.getCuentaInvariantes()  ;
        long    runningTime   = System.currentTimeMillis() - startTime;

        STATE_LOG("INFO", Thread.currentThread().getName(), "TIME"      , String.valueOf(runningTime)  );
        STATE_LOG("INFO", Thread.currentThread().getName(), "MARCADO"   , marcadoActual                );
        STATE_LOG("INFO", Thread.currentThread().getName(), "CONT TRANS", contadores                   );
        STATE_LOG("INFO", Thread.currentThread().getName(), "CONT INV"  , String.valueOf(invariantes)  );
        STATE_LOG("INFO", Thread.currentThread().getName(), "COLAS"     , Arrays.toString(Monitor.getMonitor().getCantidadEsperandoColas()));

        int aliveThreads = 0;
        int runningThreads = 0;
        for (Thread thread : threads) {
            if (thread.isAlive())
                aliveThreads++;
            if (thread.getState() == Thread.State.RUNNABLE)
                runningThreads++;

            STATE_LOG("INFO", Thread.currentThread().getName(), "THREAD", thread.getName() + " " + thread.getState());
        }

        String threadInfo = String.format("Alive: %1$d , Running: %2$d", aliveThreads, runningThreads);
        STATE_LOG("INFO", Thread.currentThread().getName(), "THREAD"  , threadInfo                    );
        STATE_LOG("INFO", Thread.currentThread().getName(), "EOM"  , "--------------------------------");
    }

    private static String formatLog(String logLevel, String step, String caller, String message){
        // Get the current date in the desired format
        String date = new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(new Date());

        // Format the log message
        return String.format("[%1$-10s] %2$s [%3$-4s] %4$s: %5$s",
                logLevel, date, step, caller, message);
    }
    public static void STATE_LOG(String logLevel, String step, String caller, String message){
        stateLogger.logn(formatLog(logLevel, step, caller, message));
    }
}

