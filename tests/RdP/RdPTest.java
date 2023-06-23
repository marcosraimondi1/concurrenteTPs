package RdP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RdPTest {

    @Test
    void disparar() {
        // crear red
        int[][] plazas_salida   = {{0,1}, {0,1}, {1,0}};
        int[][] plazas_entrada  = {{1,0}, {1,0}, {0,1}};
        int[]   marcado_inicial = {0,0,1};

        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial);

        int[] marcado_actual = red.getMarcadoActual();

        for (int i = 0; i < marcado_inicial.length; i++) {
            assertEquals(marcado_inicial[i], marcado_actual[i]);
        }

        // disparar transicion 0
        boolean disparada = red.disparar(0);
        assertFalse(disparada);

        // disparar transicion 1
        disparada = red.disparar(1);
        assertTrue(disparada);

        // verifiacr marcado
        marcado_actual = red.getMarcadoActual();

        assertEquals(1, marcado_actual[0]);
        assertEquals(1, marcado_actual[1]);
        assertEquals(0, marcado_actual[2]);

    }

    @Test
    void getSensibilizadas() {
        // crear red
        int[][] plazas_salida   = {{0,1}, {0,1}, {1,0}};
        int[][] plazas_entrada  = {{1,0}, {1,0}, {0,1}};
        int[]   marcado_inicial = {0,0,1};

        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial);

        // verificar sensibilizadas
        boolean[] sensibilizadas = red.getSensibilizadas();
        assertFalse(sensibilizadas[0]);
        assertTrue(sensibilizadas[1]);
    }

}