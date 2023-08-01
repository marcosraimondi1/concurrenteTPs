package RdP;

import org.junit.jupiter.api.Test;

import static Constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class RdPTest {

    @Test
    void disparar() {
        // crear red
        int[][] plazas_salida   = {{0,1}, {0,1}, {1,0}};
        int[][] plazas_entrada  = {{1,0}, {1,0}, {0,1}};
        int[]   marcado_inicial = {0,0,1};
        int[][] invariantes_plazas = new int[][]{
                { 1, 0, 1, 1},
                { 0, 1, 1, 1}
        };
        long[][] tiempos = new long[][]{
                // alfa ,  beta [ms]
                {   0   ,   MAX_TIME}, // T0
                {   0   ,   MAX_TIME}  // T3
        };
        int invariante_MAX = 50;
        int[] sin_trans_fuente = new int[] {};
        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial, new int[]{1,1},invariantes_plazas,tiempos,invariante_MAX,sin_trans_fuente);

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

        // --------- Verificar que cuando no se cumple el invariante de plaza tira excepcion -------
        int[][] plazas_salida2   = {{0,1}, {0,1}, {1,0}};
        int[][] plazas_entrada2  = {{1,0}, {1,0}, {0,1}};
        int[]   marcado_inicial2 = {0,0,1};
        int[][] invariantes_plazas2 = new int[][]{
                { 1, 0, 1, 5}, // el invariante es 1 no 5 -> tiene que dar error
                { 0, 1, 1, 1}
        };

        RdP red2 = new RdP(plazas_salida2,plazas_entrada2,marcado_inicial2, new int[]{1,1},invariantes_plazas2,tiempos,invariante_MAX,sin_trans_fuente);

        assertThrows(RuntimeException.class, ()->red2.disparar(1));
    }

    @Test
    void getSensibilizadas() {
        // crear red
        int[][] plazas_salida   = {{0,1}, {0,1}, {1,0}};
        int[][] plazas_entrada  = {{1,0}, {1,0}, {0,1}};
        int[]   marcado_inicial = {0,0,1};
        long[][] tiempos = new long[][]{
                // alfa ,  beta [ms]
                {   0   ,   MAX_TIME}, // T0
                {   0   ,   MAX_TIME}, // T1
        };
        int[][] invariantes_plazas = P_INV_PAPER;
        int invariante_MAX = 50;
        int[] sin_trans_fuente = new int[] {};
        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial, new int[]{1,1}, invariantes_plazas,tiempos,invariante_MAX,sin_trans_fuente);

        // verificar sensibilizadas
        boolean[] sensibilizadas = red.getSensibilizadas();
        assertFalse(sensibilizadas[0]);
        assertTrue(sensibilizadas[1]);
    }

    @Test
    void cuentaInvariantes() {
        // crear red
        int[][] plaza_salida    = W_MAS_PAPER   ;   // plazas a la salida de la transición
        int[][] plaza_entrada   = W_MENOS_PAPER ;   // plazas a la entrada de la transición
        int[]   marcado         = MI_PAPER      ;   // marcado inicial
        long[][]tiempos         = TIEMPOS_PAPER ;
        int     invariante_MAX  = 250           ;

        int[][] invariantes_plazas = P_INV_PAPER;

        int[] sin_trans_fuente = new int[] {};

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado, new int[]{5,9}, invariantes_plazas,tiempos,invariante_MAX,sin_trans_fuente);

        assertEquals(0,rdp.getCuentaInvariantes());

        // disparo el invariante de transicion T1,T2,T4,T6
        int[] secuencia1 = {0,1,3,5};
        for (int transicion : secuencia1) {
            rdp.disparar(transicion);
        }

        assertEquals(1,rdp.getCuentaInvariantes());

        // disparo el invariante de transicion T1,T3,T5,T6
        int[] secuencia2 = {0,2,4,5};
        for (int transicion : secuencia2) {
            rdp.disparar(transicion);
        }

        assertEquals(2,rdp.getCuentaInvariantes());

        // disparo el invariante de transicion T7,T8,T9,T10
        int[] secuencia3 = {6,7,8,9};
        for (int transicion : secuencia3) {
            rdp.disparar(transicion);
        }

        assertEquals(3,rdp.getCuentaInvariantes());

        // disparar 200 invariantes
        for (int i = 0; i < 200; i++) {
            for (int transicion : secuencia3) {
                rdp.disparar(transicion);
            }
        }

        assertEquals(203,rdp.getCuentaInvariantes());

        String regex    = REGEX_PAPER;
        String replace  = REPLACE_PAPER;
        assertTrue(rdp.logger.validateLog(regex,replace));
    }
}