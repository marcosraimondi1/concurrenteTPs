package Politica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Politica1: para cada conflicto, se dispara la transicion que
 * no se haya disparado antes
 */
public class Politica1 implements Politica {
    private boolean c1 = false;     // bandera para conflicto 1
    private boolean c2 = false;     // bandera para conflicto 2
    private boolean c3 = false;     // bandera para conflicto 3
    public int cual(boolean[] transiciones){

        ArrayList<Integer> sensibilizadas = new ArrayList<>();
        for(int i = 0; i<transiciones.length;i++){
            if(transiciones[i])
                sensibilizadas.add(i);
        }
        int index = getRandomElement(sensibilizadas);

        if (index == 1 || index == 2)
        {
            // en la RdP, T1 esta en conflicto con T2
            if (!transiciones[2]) {
                return 1;
            }
            if (!transiciones[1]) {
                return 2;
            }
            // las 2 transiciones correspondientes al conflicto esta sensibilizadas y tiene hilo asociado a ejecución
            c1 = !c1;
            if (c1) {
                return 1;
            }
            return 2;
        }
        if (index == 5 || index == 6)
        {
            // en la RdP, T1 esta en conflicto con T2
            if (!transiciones[6]) {
                return 5;
            }
            if (!transiciones[5]) {
                return 6;
            }
            // las 2 transiciones correspondientes al conflicto esta sensibilizadas y tiene hilo asociado a ejecución
            c2 = !c2;
            if (c2) {
                return 5;
            }
            return 6;
        }
        if (index == 9 || index == 10)
        {
            // en la RdP, T1 esta en conflicto con T2
            if (!transiciones[10]) {
                return 9;
            }
            if (!transiciones[9]) {
                return 10;
            }
            // las 2 transiciones correspondientes al conflicto esta sensibilizadas y tiene hilo asociado a ejecución
            c3 = !c3;
            if (c3) {
                return 9;
            }
            return 10;
        }

        return index; //TODO incluir en los random las transiciones de los conflictos para que no haya prioridad

    }
    public int getRandomElement(ArrayList<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

}
