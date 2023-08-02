package Politica;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que define una politica de disparo aleatoria.
 */
public class Politica {

    /**
     * Metodo que nos devuelve una unica transicion la cual sera disparada. si o si debe devolver una.
     * @param transiciones, los conflictos de la RdP
     * @return int . transicion a disparar
     */
    public int cual(boolean[] transiciones) {
        ArrayList<Integer> sensibilizadas = new ArrayList<>();
        for (int i = 0; i < transiciones.length; i++) {
            if (transiciones[i])
                sensibilizadas.add(i);
        }

        return getRandomElement(sensibilizadas); // Seleccionamos una de sensibilizadas, de manera aleatoria
    }

    /**
     * Metodo utilizado por el metodo cual, que elije una sola transcion de manera aleatoria, que luego sera disparada
     * @param list, lista de transiciones sensibilizadas
     * @return int . transicion elegida
     */
    protected int getRandomElement(ArrayList<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
