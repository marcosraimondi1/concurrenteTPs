package Monitor;

import Politica.PoliticaTest;
import RdP.RdP;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
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

        RdP rdp = new RdP(W_MAS_PAPER,W_MENOS_PAPER,MI_PAPER,T_INV_PAPER,P_INV_PAPER,TIEMPOS_PAPER,invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

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
    }
    @Test
    void invarianteDeTransicionCheck() {
        //------------------------------Inicio Politica----------------------------------------------//

        PoliticaTest politica = new PoliticaTest();

        //------------------------------Inicio RdP---------------------------------------------------//

        int     invariantes_MAX     = 100                       ;

        RdP rdp = new RdP(W_MAS_PAPER, W_MENOS_PAPER, MI_PAPER, T_INV_PAPER, P_INV_PAPER, TIEMPOS_PAPER, invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = Monitor.getMonitor(rdp,politica);

        // Declaro las secuencias de disparo para los hilos
        int[] secuencia1 = {6,7,8,9}; // Caso 1 segmento S_e implica plazas P9,P10,P11
        int[] secuencia2 = {0};       // Caso 2 segmento S_a implica plazas P2
        int[] secuencia3 = {1,3};     // Caso 2 segmento S_b implica plazas P3
        int[] secuencia4 = {2,4};     // Caso 2 segmento S_c implica plazas P4
        int[] secuencia5 = {5};       // Caso 3 segmento S_d implica plazas P5

        int[][] secuencias      = {secuencia1,secuencia2,secuencia3,secuencia4,secuencia5};
        Thread [] threads       = new Thread[5];
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
                            monitor.dispararTransicion(k);
                        }
                    }

                }
                System.out.println("El "+Thread.currentThread().getName()+" Termino de ejecutar y volvió");
                latch.countDown();
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        boolean success;

        try {
            success = latch.await(10000,java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(success);

        // verifico que se hayan ejecutado 100 invariantes

        System.out.println(rdp.getCuentaInvariantes());

        assertEquals(100,rdp.getCuentaInvariantes());

        // verifico que se termine en el marcado Inicial

        System.out.println(Arrays.toString(rdp.getMarcadoActual()));
        assertArrayEquals(MI_PAPER, rdp.getMarcadoActual());

        // verifico expresion regular
        String regex = "((T0)((T1)(.*?)(T3)(.*?)|(T2)(.*?)(T4)(.*?))(T5))|((T6)(T7)(T8)(T9))";
        String replace = "$5$7$9$11";

        assertTrue(rdp.logger.validateLog(regex, replace));
    }
}