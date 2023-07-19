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

            if (transiciones[1])
            {
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                // en la RdP, T1 esta en conflicto con T2
                if (!transiciones[2]) {
                    return 1;
                }
                c1 = !c1;
                if (c1) {
                    return 1;
                }
                return 2;
            }
            if (transiciones[5])
            {
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                if (!transiciones[6]) {
                    return 5;
                }
                c2 = !c2;
                if (c2)
                    return 5;
                return 6;
            }
            if(transiciones[9]){
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                if (!transiciones[10]) {
                    return 9;
                }
                c3 = !c3;
                if (c3) {
                    return 9;
                }
                return 10;
            }
        ArrayList<Integer> sensibilizadas = new ArrayList<>();
        for(int i = 0; i<transiciones.length;i++){
            if(transiciones[i])
                sensibilizadas.add(i);
        }
        return getRandomElement(sensibilizadas); //TODO incluir en los random las transiciones de los conflictos para que no haya prioridad

    }
    public int getRandomElement(ArrayList<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

}
