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

        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial, new int[]{1,1});

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

        RdP red = new RdP(plazas_salida,plazas_entrada,marcado_inicial, new int[]{1,1});

        // verificar sensibilizadas
        boolean[] sensibilizadas = red.getSensibilizadas();
        assertFalse(sensibilizadas[0]);
        assertTrue(sensibilizadas[1]);
    }

    @Test
    void cuentaInvariantes() {
        // crear red
        int[][] plaza_salida = getMatrices(true);      // plazas a la salida de la transición
        int[][] plaza_entrada = getMatrices(false);    // plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                    // marcado inicial

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado, new int[]{5,9});

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