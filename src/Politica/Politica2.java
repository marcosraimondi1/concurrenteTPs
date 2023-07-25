package Politica;

import java.util.Arrays;
import java.util.Random;

/**
 * Politica2: para cada conflicto, se dispara la transicion izquierda un 80% de las veces
 */
public class Politica2 extends Politica {

    private final int[][] conflictos;
    private final boolean[] flags;

    /**
     * Constructor de la clase Politica2,
     * @param conflictos, los conflictos de la RdP
     */
    public Politica2(int[][] conflictos) {
        this.conflictos = conflictos;
        // inicializa todas las banderas en false
        flags = new boolean[conflictos.length];
        Arrays.fill(flags, false);
    }

    @Override
    public int cual(boolean[] transiciones){
        int index = super.cual(transiciones);

        for (int i = 0; i < conflictos.length; i++) {
            int[] conflicto = conflictos[i];

            // transiciones en conflicto
            int tx = conflicto[0];
            int ty = conflicto[1];

            if (index != tx && index != ty)
                continue; // no se eligieron las transiciones del conflicto

            // verifico que ambas transiciones esten sensibilizdasa
            if (!transiciones[tx]) {
                return ty;
            }
            if (!transiciones[ty]) {
                return tx;
            }

            // hay conflicto efectivo -> aplico la politica

            if (tx == 9 || tx == 10) {
                // politica 80% hacia la izquierda
                boolean trans_izquierda = definirProbabilidad();
                if(trans_izquierda){
                    return 9;
                }
                return 10;
            }

            // aplico politica 50% para el resto de los conflictos
            flags[i] = !flags[i];           // cambio el valor de la bandera
            return flags[i] ? tx : ty;      // elijo la transicion correspondiente
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
        boolean izquierda = numeroAleatorio > probabilidadDerecha;

        return izquierda;

    }
}
