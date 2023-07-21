package Monitor;

import Politica.PoliticaTest;
import RdP.RdP;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static Constants.Constants.MAX_TIME;
import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {
    long[][] tiempos = {
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
    };
    @Test
    void dispararTransicion() {
        // Testeo con la RdP del paper
        //------------------------------Inicio Politica----------------------------------------------//

        PoliticaTest politica = new PoliticaTest();

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][] plaza_salida = getMatrices(true);      // plazas a la salida de la transición
        int[][] plaza_entrada = getMatrices(false);    // plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                    // marcado inicial
        int[][] invariantes_plazas = getInvariantesPlazas();
        int invariantes_MAX = 200;

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado,new int[]{5,9},invariantes_plazas,tiempos,invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = new Monitor(rdp,politica);

        //------------------------------Test-1--------------------------------------------------------//
        // Testea que el hilo tome el mutex y no se quede esperando

        Thread thread = new Thread(()->{
            monitor.dispararTransicion(0);//Disparo T1
        });
        thread.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(monitor.getMutex().hasQueuedThreads());//No espera en la cola de entrada del monitor

        // chequeo que se haya disparado la transicion 0
        int[] marcadoEsperado = {1, 1, 0, 0, 0, 1, 1, 2, 0, 0,  0,  1,  0,  0};
        int[] marcadoActual = rdp.getMarcadoActual();
        assertArrayEquals(marcadoEsperado,marcadoActual);


        //------------------------------Test-2--------------------------------------------------------//
        // Testea que el hilo libere el mutex una vez disparado

        thread = new Thread(()->{
            monitor.dispararTransicion(2);//Disparo T3
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

        int[][] plaza_salida = getMatrices(true);      // plazas a la salida de la transición
        int[][] plaza_entrada = getMatrices(false);    // plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                    // marcado inicial
        int[][] invariantes_plazas = getInvariantesPlazas();
        int[] trans_invariantes = new int[]{5,9};              // index de T6 = 5 e index de T10 = 9 (se usan para contar vueltas completadas por hilo)
        int invariantes_MAX = 100;
        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado, trans_invariantes,invariantes_plazas,tiempos,invariantes_MAX);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = new Monitor(rdp,politica);

        // Declaro las secuencias de disparo para los hilos
        int[] secuencia1 = {6,7,8,9}; //Caso 1 segmento S_e implica plazas P9,P10,P11
        int[] secuencia2 = {0};       //Caso 2 segmento S_a implica plazas P2
        int[] secuencia3 = {1,3};     //Caso 2 segmento S_b implica plazas P3
        int[] secuencia4 = {2,4};     //Caso 2 segmento S_c implica plazas P4
        int[] secuencia5 = {5};       //Caso 3 segmento S_d implica plazas P5

        int[][] secuencias = {secuencia1,secuencia2,secuencia3,secuencia4,secuencia5};
        Thread [] threads = new Thread[5];
        CyclicBarrier cyclic = new CyclicBarrier( threads.length + 1,() -> {}); // el hilo de ejecución del test y el que termine


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

        try {
            cyclic.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        // verifico que se hayan ejecutado 100 invariantes
        System.out.println(rdp.getCuentaInvariantes());
        assertTrue(100<=rdp.getCuentaInvariantes());
        // verifico que se termine en el marcado Inicial
        System.out.println(Arrays.toString(rdp.getMarcadoActual()));
        assertArrayEquals(getMarcadoInicial(), rdp.getMarcadoActual());
        //verifico expresion regular
        String regex = "((T0)((T1)(.*?)(T3)(.*?)|(T2)(.*?)(T4)(.*?))(T5))|((T6)(T7)(T8)(T9))";
        String replace = "$5$7$9$11";
        assertTrue(rdp.logger.validateLog(regex, replace));
    }
    public int[][] getMatrices (boolean derecha){

        int[][] matriz;

        if(derecha){
            // W+
            matriz = new int[][]{
                    //T1 T2 T3 T4 T5 T6 T7 T8 T9 T10
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},//P1
                     {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},//P2
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},//P3
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},//P4
                     {0, 0, 0, 1, 1, 0, 0, 0, 0, 0},//P5
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},//P6
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},//P7
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},//P8
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},//P9
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},//P10
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},//P11
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},//P12
                     {0, 1, 1, 0, 0, 1, 0, 1, 0, 1},//P13
                     {0, 1, 1, 0, 0, 0, 0, 0, 1, 0},//P14


            };
        }else{
            //W-
            matriz = new int[][]{
                    //T1 T2 T3 T4 T5 T6 T7 T8 T9 T10
                     {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},//P1
                     {0, 1, 1, 0, 0, 0, 0, 0, 0, 0},//P2
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},//P3
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},//P4
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},//P5
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},//P6
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},//P7
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},//P8
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},//P9
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},//P10
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},//P11
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},//P12
                     {1, 0, 0, 1, 1, 0, 1, 0, 1, 0},//P13
                     {1, 0, 0, 0, 0, 0, 1, 0, 0, 0},//P14

                    //{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
        }

        return matriz;
    }
    public int[] getMarcadoInicial (){
                    //  P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14
        return new int[]{2, 0, 0, 0, 0, 1, 1, 2, 0, 0,  0,  1,  1,  1};
    }

    public int[][] getInvariantesPlazas () {
        /*
            1. M(P1) + M(P2) + M(P3) + M(P4) + M(P5) = 2
            2. M(P10) + M(P12) = 1
            3. M(P11) + M(P13) + M(P2) + M(P5) + M(P9) = 1
            4. M(P10) + M(P14) + M(P2) + M(P9) = 1
            5. M(P3) + M(P6) = 1
            6. M(P4) + M(P7) = 1
            7. M(P10) + M(P11) + M(P8) + M(P9) = 2
        */
        return new int[][]{
                // P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14  =  X
                {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,         2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0,         1},
                {0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0,         1},
                {0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1,         1},
                {0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,         1},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,         1},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,         2},
        };
    }

}