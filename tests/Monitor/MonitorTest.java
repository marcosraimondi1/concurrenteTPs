package Monitor;

import Politica.Politica1;
import RdP.RdP;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {
    @Test
    void dispararTransicion() {
        // Testeo con la RdP del paper
        //------------------------------Inicio Politica----------------------------------------------//

        Politica1 politica = new Politica1();

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][] plaza_salida = getMatrices(true);      // plazas a la salida de la transición
        int[][] plaza_entrada = getMatrices(false);    // plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                    // marcado inicial

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado);

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

        Politica1 politica = new Politica1();

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][] plaza_salida = getMatrices(true);      // plazas a la salida de la transición
        int[][] plaza_entrada = getMatrices(false);    // plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                    // marcado inicial

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = new Monitor(rdp,politica);

        // disparo el invariante de transicion T1,T2,T4,T6
        int[] secuencia1 = {0,1,3,5};
        for (int transicion : secuencia1) {
            monitor.dispararTransicion(transicion);
        }

        int[] marcadoEsperado = getMarcadoInicial();
        int[] marcadoActual = rdp.getMarcadoActual();

        assertArrayEquals(marcadoEsperado,marcadoActual);

        // disparo el invariante de transicion T1,T3,T5,T6
        int[] secuencia2 = {0,2,4,5};
        for (int transicion : secuencia2) {
            monitor.dispararTransicion(transicion);
        }

        marcadoActual = rdp.getMarcadoActual();

        assertArrayEquals(marcadoEsperado,marcadoActual);

        // disparo el invariante de transicion T7,T8,T9,T10
        int[] secuencia3 = {6,7,8,9};
        for (int transicion : secuencia3) {
            monitor.dispararTransicion(transicion);
        }

        marcadoActual = rdp.getMarcadoActual();

        assertArrayEquals(marcadoEsperado,marcadoActual);


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
}