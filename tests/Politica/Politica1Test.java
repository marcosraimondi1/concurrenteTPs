package Politica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Politica1Test {

    @Test
    void cual() {
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
    }
}