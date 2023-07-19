package Politica;

import java.util.Arrays;
import java.util.Random;

/**
 * Politica1: para cada conflicto, se dispara la transicion que
 * no se haya disparado antes
 */
public class Politica2 implements Politica {
    private boolean c1 = false;     // bandera para conflicto 1
    private boolean c2 = false;     // bandera para conflicto 2

    public int cual(boolean[] transiciones){
        for (int i = 0; i < transiciones.length; i++) {
            if (!transiciones[i])
                continue;

            if (i == 1)
            {
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                // en la RdP, T1 esta en conflicto con T2
                if (!transiciones[i + 1]) {
                    return i;
                }
                c1 = !c1;
                if (c1)
                    return i;
                return i+1;
            }
            if (i == 5)
            {
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                if (!transiciones[i + 1]) {
                    return i;
                }
                c2 = !c2;
                if (c2)
                    return i;
                return i+1;
            }

            if(i == 9){
                // las 2 transiciones correspondientes al conflicto estan sensibilizadas
                // politica 80% hacia la izquierda
                boolean trans_izquierda = definirProbabilidad();
                if(trans_izquierda){
                    return i;
                }else{
                    return i+1;
                }
            }
            return i;
        }
        System.out.println(Arrays.toString(transiciones));
        throw new RuntimeException("No hay transiciones para disparar");
    }

    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T10

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();

        // Compara el número aleatorio con la probabilidad asociada
        boolean dispararT9 = numeroAleatorio >= probabilidadDerecha;

        return dispararT9;

    }
}
