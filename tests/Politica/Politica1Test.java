package Politica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Politica1Test {

    @Test
    void cual() {
        //------------------------------Politica 50%----------------------------------------------//
        Politica politica = new Politica1();
        boolean[] transiciones = new boolean[16];

        // conflicto 1
        transiciones[1] = true;
        transiciones[2] = true;

        assertEquals(1, politica.cual(transiciones));
        assertEquals(2, politica.cual(transiciones));

        int T1s = 0;
        int T2s = 0;

        int t;

        for (int i = 0; i < 1000; i++){
            t = politica.cual(transiciones);
            if (t == 1)
                T1s++;
            else if (t == 2)
                T2s++;
        }

        assertEquals(500, T1s);
        assertEquals(500, T2s);
        //------------------------------Politica 80% etapa 3--------------------------------------//
        politica = new Politica1(true);
        transiciones = new boolean[16];

        // conflicto T11 y T12
        transiciones[11] = true;
        transiciones[12] = true;

        int T11s = 0;
        int T12s = 0;

        int t1;

        for (int i = 0; i < 1000; i++){
            t1 = politica.cual(transiciones);
            if (t1 == 11)
                T11s++;
            else if (t1 == 12)
                T12s++;
        }
        System.out.println("Veces que se disparo T11: "+T11s+" y Veces que se disparo T12: "+T12s);
        assertTrue(T11s > 750 && T11s < 850);
        assertTrue(T12s > 150 && T12s < 250);
        //------------------------------Politica conflicto desabilitado--------------------------------------//
    }
}