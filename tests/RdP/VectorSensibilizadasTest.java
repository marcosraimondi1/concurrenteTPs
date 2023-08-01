package RdP;

import Monitor.Monitor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static Constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class VectorSensibilizadasTest {

    @Test
    void isSensibilizada() {
        int[][]     plazas_entrada_transiciones = W_MENOS_TP2   ;
        int[]       marcado_inicial             = MI_TP2        ;
        long[][]    tiempos                     = TIEMPOS       ;
        int[] sin_trans_fuente = new int[] {};

        VectorSensibilizadas vectorSensibilizadas = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente
        );
        boolean sensibilizada;
        long tic = System.currentTimeMillis();
        try {
            sensibilizada = vectorSensibilizadas.isSensibilizada(0);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        long toc = System.currentTimeMillis();

        // is sensibilizada, si alcanzan los tokens duerme hasta que este en la ventana y devuelve true
        assertTrue(sensibilizada);
        assertTrue(toc - tic >= TIEMPOS[0][0]);

        try {
            sensibilizada = vectorSensibilizadas.isSensibilizada(1);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        assertFalse(sensibilizada);

        try {
            sensibilizada = vectorSensibilizadas.isSensibilizada(3);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        assertFalse(sensibilizada);

        // -------------- PRUEBO SI TIRA ERROR DE TIMEOUT ----------------
        plazas_entrada_transiciones = new int[][] {
                //  T0
                {1}, // P0
        };
        marcado_inicial             = new int[] { 1 };

        tiempos                     = new long[][] {
                //  alfa ,    beta
                {100L,  500L        }, // T0
        };

        vectorSensibilizadas = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente
        );
        try {
            // pasan 100ms, no deberia tirar error
            assertTrue(vectorSensibilizadas.isSensibilizada(0));
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(tiempos[0][1]); // espero mas alla del beta -> debe tirar error
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        VectorSensibilizadas finalVectorSensibilizadas = vectorSensibilizadas;
        assertThrows(TimeoutException.class, ()-> finalVectorSensibilizadas.isSensibilizada(0));

    }

    @Test
    void actualizarSensibilizadas() {
        int[][]     plazas_entrada_transiciones = new int[][] {
            //  T0 T1 T2
                {0, 1, 1}, // P0
                {1, 0, 1}, // P1
        };
        int[]       marcado_inicial             = new int[] { 0, 0 };

        // ------------------ SENSIBILIZADO DE TRANSICIONES INMEDIATAS ------------------
        long[][]    tiempos                     = new long[][] {
            //  alfa ,    beta
                {0L  ,  MAX_TIME    }, // T0
                {0L  ,  MAX_TIME    }, // T1
                {0L  ,  MAX_TIME    }, // T2
        };
        int[] sin_trans_fuente = new int[] {};
        VectorSensibilizadas vectorSensibilizadas = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente
        );
        ArrayList<Integer> transicions = new ArrayList<Integer>();
        transicions.add(0);

        boolean sensibilizada;
        try {
            sensibilizada = vectorSensibilizadas.isSensibilizada(0);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        assertFalse(sensibilizada);

        int[] nuevo_marcado = new int[] { 0, 1 }; // sensibilizo T0
  ;
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);

        try {
            sensibilizada = vectorSensibilizadas.isSensibilizada(0);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        assertTrue(sensibilizada);


        nuevo_marcado = new int[] { 1, 0 }; // sensibilizo T1

        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1 );

        try {
            assertFalse(vectorSensibilizadas.isSensibilizada(0));
            assertTrue(vectorSensibilizadas.isSensibilizada(1));
            assertFalse(vectorSensibilizadas.isSensibilizada(2));
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }


        nuevo_marcado = new int[] { 1, 1 }; // sensibilizo T0,T1,T2

        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1 );

        try {
            assertTrue(vectorSensibilizadas.isSensibilizada(0));
            assertTrue(vectorSensibilizadas.isSensibilizada(1));
            assertTrue(vectorSensibilizadas.isSensibilizada(2));
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        // ------------------ SENSIBILIZADO DE TRANSICIONES TEMPORIZADAS ------------------
        marcado_inicial             = new int[] { 0, 0 };
        tiempos                     = new long[][] {
                //  alfa ,    beta
                {50L  ,  MAX_TIME    }, // T0
                {100L ,  MAX_TIME    }, // T1
                {40L  ,  MAX_TIME    }, // T2
        };

        vectorSensibilizadas = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente

        );

        ArrayList<Integer> transicionesDisp= new ArrayList<Integer>();

        try {
            long tic = System.currentTimeMillis();
            assertFalse(vectorSensibilizadas.isSensibilizada(0));
            long toc = System.currentTimeMillis();
            assertTrue(toc - tic < tiempos[0][0]); // porque no alcanzan los tokens
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        nuevo_marcado = new int[] { 0, 1 }; // sensibilizo T0
        transicionesDisp.add(0);
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);

        try {
            long tic = System.currentTimeMillis();
            assertTrue(vectorSensibilizadas.isSensibilizada(0));
            long toc = System.currentTimeMillis();
            assertTrue(toc - tic >= tiempos[0][0]); // tiempo que demora en disparar
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        transicionesDisp.clear();
        nuevo_marcado = new int[] { 1, 1 }; // sensibilizo T0, T1, T2

        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);

        try {
            // veo si T2 se sensibiliza
            long tic = System.currentTimeMillis();
            assertTrue(vectorSensibilizadas.isSensibilizada(2));
            long toc = System.currentTimeMillis();
            assertTrue(toc - tic >= tiempos[2][0]); // tiempo que demora en disparar
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }


        // verifico que se actualice el timestamp de la transicion
        transicionesDisp.clear();
        nuevo_marcado = new int[] { 0, 0 };
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);
        try {
            // veo si T2 se desensibiliza
            long tic = System.currentTimeMillis();
            assertFalse(vectorSensibilizadas.isSensibilizada(2));
            long toc = System.currentTimeMillis();
            assertTrue(toc - tic < tiempos[2][0]);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        nuevo_marcado = new int[] { 1, 1 };
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);
        try {
            // veo si T2 actualizo su timestamp
            long tic = System.currentTimeMillis();
            assertTrue(vectorSensibilizadas.isSensibilizada(2));
            long toc = System.currentTimeMillis();
            // si no se actualizo, el tiempo de sensibilizado deberia ser menor a alfa
            assertTrue(toc - tic >= tiempos[2][0]);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        // -------------------------------------------------------------------------------------
        nuevo_marcado = new int[] { 0, 0 };
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);
        nuevo_marcado = new int[] { 1, 0 }; // sensibilizo T1
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);

        try {
            Thread.sleep(tiempos[1][0]);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        nuevo_marcado = new int[] { 1, 1 }; // sensibilizo T1 de nuevo -> no debe cambiar el timestamp
        vectorSensibilizadas.actualizarSensibilizadas(nuevo_marcado,-1);

        try {
            long tic = System.currentTimeMillis();
            assertTrue(vectorSensibilizadas.isSensibilizada(1));
            long toc = System.currentTimeMillis();
            // ya habia pasado alfa por lo que deberia sensibilizarse inmediatamente
            assertTrue(toc - tic < tiempos[1][0]);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void esperando(){
        // verifico que cuando hay una transicion esperando no se intente disparar otra
        int[][]     plazas_entrada_transiciones = new int[][] {
                //  T0 T1 T2
                    {0, 1, 1}, // P0
                    {1, 0, 1}, // P1
        };

        int[][]     plazas_salida_transiciones = new int[][] {
                //  T0 T1 T2
                    {1, 0, 0}, // P0
                    {0, 1, 0}, // P1
        };

        int[]       marcado_inicial             = new int[] { 1, 1 };

        long[][]    tiempos                     = new long[][] {
                //  alfa ,    beta
                {1000L  ,  MAX_TIME    }, // T0
                {1000L  ,  MAX_TIME    }, // T1
                {2000L  ,  MAX_TIME    }, // T2
        };
        int[] sin_trans_fuente = new int[] {};
        Monitor.resetMonitor();
        Monitor.getMonitor(
                new RdP(plazas_salida_transiciones, plazas_entrada_transiciones, marcado_inicial, new int[] {1,2}, new int [][] {{1,1,2}}, tiempos, 500,sin_trans_fuente),
                new Politica.Politica()
        );

        VectorSensibilizadas vectorSensibilizadas = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente
        );

        CountDownLatch latch = new CountDownLatch(3);

        // si pregunto por todas distintas no debe haber problema
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            threads[i] = new Thread(()->{
                try {
                    assertTrue(vectorSensibilizadas.isSensibilizada(finalI));
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            });
            threads[i].start();
        }

        boolean passed;
        try {
            passed = latch.await(10000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(passed);

        CountDownLatch latch2 = new CountDownLatch(3);

        // disparando al msima seguido solo una tiene q devolveer true
        System.out.println("PARTE 2 -----------------");
        VectorSensibilizadas vectorSensibilizadas2 = new VectorSensibilizadas(
                plazas_entrada_transiciones,
                marcado_inicial,
                tiempos,
                sin_trans_fuente
        );

        threads = new Thread[3];
        AtomicInteger count = new AtomicInteger();

        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(()->{
                try {
                    try {
                        Monitor.getMonitor().getMutex().acquire();

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Verificando "+Thread.currentThread().getName());
                   if(vectorSensibilizadas2.isSensibilizada(0))
                       count.getAndIncrement();
                    Monitor.getMonitor().getMutex().release();

                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
                latch2.countDown();
            });
            threads[i].start();

        }

        try {
            passed = latch2.await(10000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(passed);
        assertEquals(1, count.get());
    }
}