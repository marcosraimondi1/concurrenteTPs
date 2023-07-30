package Monitor;

import Politica.*;
import RdP.RdP;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static Constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {
    @Test
    void dispararTransicion() {
        // Testeo con la RdP del paper
        //------------------------------Inicio Politica----------------------------------------------//

        PoliticaTest politica = new PoliticaTest();

        //------------------------------Inicio RdP---------------------------------------------------//

        int invariantes_MAX = 200;

        RdP rdp = new RdP(W_MAS_PAPER.clone(),W_MENOS_PAPER.clone(),MI_PAPER.clone(),T_INV_PAPER.clone(),P_INV_PAPER.clone(),TIEMPOS_PAPER.clone(),invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//
        Monitor.resetMonitor();
        Monitor monitor = Monitor.getMonitor(rdp,politica);

        //------------------------------Test-1--------------------------------------------------------//
        // Testea que el hilo tome el mutex y no se quede esperando

        Thread thread = new Thread(()->{
            monitor.dispararTransicion(0); // Disparo T1
        });
        thread.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(monitor.getMutex().hasQueuedThreads()); // No espera en la cola de entrada del monitor

        // chequeo que se haya disparado la transicion 0
        int[] marcadoEsperado = {1, 1, 0, 0, 0, 1, 1, 2, 0, 0,  0,  1,  0,  0};
        int[] marcadoActual = rdp.getMarcadoActual();
        assertArrayEquals(marcadoEsperado,marcadoActual);


        //------------------------------Test-2--------------------------------------------------------//
        // Testea que el hilo libere el mutex una vez disparado

        thread = new Thread(()->{
            monitor.dispararTransicion(2); // Disparo T3
        });
        thread.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,monitor.getMutex().availablePermits());

        //------------------------------Test-3--------------------------------------------------------//
        // Testea cuantos hilos vuelven de una transición que se puede disparar una sola vez
        AtomicInteger variable = new AtomicInteger();

        Thread[] threads = new Thread[5];

        for(int i =0;i<5;i++){
            threads[i] = new Thread(()->{
                monitor.dispararTransicion(4);//Disparo T5
                variable.getAndIncrement();
            });
        }

        for(int i =0;i<5;i++){
            threads[i].start();
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Como T5 se puede disparar solo una vez, Solo un hilo va a incrementar "variable"
        assertEquals(1,variable.get());

        // el resto queda esperando en la cola de T5
        assertEquals(4,monitor.getCantidadEsperandoColas()[4]);
    }
    @Test
    void invarianteDeTransicionCheck() {
        //------------------------------Inicio Politica----------------------------------------------//
        Politica    politica   = new Politica1(CONFLICTOS_PAPER); // politica1 es la de 50-50

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][]     plaza_salida        = W_MAS_PAPER       ; // plazas a la salida de la transición (Matriz)
        int[][]     plaza_entrada       = W_MENOS_PAPER     ; // plazas a la entrada de la transición (Matriz)
        int[]       marcado             = MI_PAPER.clone()  ; // marcado inicial
        long[][]    tiempos             = TIEMPOS_PAPER     ; // tiempo de cada transicion
        int         invariantes_MAX     = 100               ; // cantidad de invariantes a realizar
        int[]       trans_invariantes   = T_INV_PAPER       ; // transiciones para contar invariantes (T14 marca una vuelta)
        int[][]     invariantes_plazas  = P_INV_PAPER       ; // invariantes de plaza de la red

        RdP rdp = new RdP(plaza_salida, plaza_entrada, marcado, trans_invariantes, invariantes_plazas, tiempos, invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor.resetMonitor(); // porque se comparte entre metodos de test de una misma clase test
        Monitor monitor = Monitor.getMonitor(rdp,politica);

        //------------------------------Inicio Hilos-------------------------------------------------//

        // Declaro las secuencias de disparo para los hilos
        int[] secuencia1 = {6,7,8,9}; // Caso 1 segmento S_e implica plazas P9,P10,P11
        int[] secuencia2 = {0};       // Caso 2 segmento S_a implica plazas P2
        int[] secuencia3 = {1,3};     // Caso 2 segmento S_b implica plazas P3
        int[] secuencia4 = {2,4};     // Caso 2 segmento S_c implica plazas P4
        int[] secuencia5 = {5};       // Caso 3 segmento S_d implica plazas P5

        int[][] secuencias      = {secuencia1,secuencia2,secuencia3,secuencia4,secuencia5};

        Thread [] threads       = new Thread[secuencias.length];

        CountDownLatch latch    = new CountDownLatch(threads.length);

        for (int i = 0; i< threads.length; i++)
        {
            int finalI = i;
            threads[i] = new Thread(()->{

                boolean condicion = true;

                while(condicion){

                    int[] secuencia = secuencias[finalI]; // selecciono una de las 5 secuancias

                    // disparo la secuencia invariante
                    for (int k : secuencia) {
                        if(!monitor.seAvanza()){
                            condicion = false;
                            break;
                        }else {
                            // se puede avanzar. apagar es false
                            monitor.dispararTransicion(k);
                        }
                    }
                }
                latch.countDown();
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        boolean success;

        try {
            success = latch.await(15000,java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // verifico que termine porque el countdown latch llego a 0 y no por timeout
        assertTrue(success);

        // verifico que se hayan ejecutado 100 invariantes
        assertEquals(invariantes_MAX,rdp.getCuentaInvariantes());

        // verifico que se termine en el marcado Inicial
        assertArrayEquals(MI_PAPER, rdp.getMarcadoActual());

        // verifico log con expresion regular
        assertTrue(rdp.logger.validateLog(REGEX_PAPER, REPLACE_PAPER));
    }
}