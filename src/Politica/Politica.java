package Politica;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que define una politica de disparo aleatoria.
 */
public class Politica {
    public int cual(boolean[] transiciones) {
        ArrayList<Integer> sensibilizadas = new ArrayList<>();
        for (int i = 0; i < transiciones.length; i++) {
            if (transiciones[i])
                sensibilizadas.add(i);
        }

        return getRandomElement(sensibilizadas);
    }

    protected int getRandomElement(ArrayList<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
