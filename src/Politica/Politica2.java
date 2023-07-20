package Politica;

import java.util.ArrayList;
import java.util.Random;

/**
 * Politica1: para cada conflicto, se dispara la transicion que
 * no se haya disparado antes
 */
public class Politica2 extends Politica {
    private boolean c1 = false;     // bandera para conflicto 1
    private boolean c2 = false;     // bandera para conflicto 2
    private int contadorT9 = 0;
    private int contadorT10 = 0;
    @Override
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
            // politica 80% hacia la izquierda
            boolean trans_izquierda = definirProbabilidad();
            if(trans_izquierda){
                contadorT9++;
                return 9;
            }else {
                contadorT10++;
                return 10;
            }
        }

        return index;

    }


    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T10

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();
        //System.out.println(numeroAleatorio);
        // Compara el número aleatorio con la probabilidad asociada
        boolean dispararT9 = numeroAleatorio > probabilidadDerecha;

        return dispararT9;

    }
    public int getContadorT9() {
        return contadorT9;
    }

    public int getContadorT10() {
        return contadorT10;
    }

}
