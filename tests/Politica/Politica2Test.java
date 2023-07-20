package Politica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Politica2Test {

    @Test
    void cual() {
        //------------------------------Politica 80% etapa 3--------------------------------------//
        int[][] conflictos = new int[][] {
                {9, 10} // T9 y T10
        };

        Politica politica = new Politica2(conflictos);

        boolean[] transiciones = new boolean[16];

        // conflicto T9 y T10
        transiciones[9] = true;
        transiciones[10] = true;

        int T9s = 0;
        int T10s = 0;

        int t1;
        int cantidadEjecuciones = 1000;
        for (int i = 0; i < cantidadEjecuciones; i++){
            t1 = politica.cual(transiciones);
            if (t1 == 9)
                T9s++;
            else if (t1 == 10)
                T10s++;
        }
        System.out.println("Veces que se disparo T9: "+T9s+" y Veces que se disparo T10: "+T10s);
        assertTrue(T9s > (cantidadEjecuciones*0.8 - cantidadEjecuciones*0.15) && T9s < (cantidadEjecuciones*0.8 + cantidadEjecuciones*0.15));
        assertTrue(T10s > (cantidadEjecuciones*0.2 - cantidadEjecuciones*0.15) && T10s < (cantidadEjecuciones*0.2 + cantidadEjecuciones*0.15));

    }
}