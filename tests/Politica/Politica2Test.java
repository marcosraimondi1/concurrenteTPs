package Politica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Politica2Test {

    @Test
    void cual() {
        //------------------------------Politica 80% etapa 3--------------------------------------//
        Politica politica = new Politica2();
        boolean[] transiciones = new boolean[16];

        // conflicto T11 y T12
        transiciones[11] = true;
        transiciones[12] = true;

        int T11s = 0;
        int T12s = 0;

        int t1;
        int cantidadEjecuciones = 1000;
        for (int i = 0; i < cantidadEjecuciones; i++){
            t1 = politica.cual(transiciones);
            if (t1 == 11)
                T11s++;
            else if (t1 == 12)
                T12s++;
        }
        System.out.println("Veces que se disparo T11: "+T11s+" y Veces que se disparo T12: "+T12s);
        assertTrue(T11s > (cantidadEjecuciones*0.8 - cantidadEjecuciones*0.15) && T11s < (cantidadEjecuciones*0.8 + cantidadEjecuciones*0.15));
        assertTrue(T12s > (cantidadEjecuciones*0.2 - cantidadEjecuciones*0.15) && T12s < (cantidadEjecuciones*0.2 + cantidadEjecuciones*0.15));

    }
}