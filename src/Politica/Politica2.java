package Politica;

import java.util.Arrays;
import java.util.Random;

/**
 * Politica2: para el conflicto de la Rdp del TP2
 * se dispara la transicion izquierda (T9) un 80% de las veces y (T10) un 20% de las veces
 * El resto de conflictos se dispara con una politica del 50%
 */
public class Politica2 extends Politica {

    private final int[][] conflictos;
    private final boolean[] conflictosProbabilisticos;
    private final boolean[] flags;

    /**
     * Constructor de la clase Politica2,
     * @param conflictos, los conflictos de la RdP
     */
    public Politica2(int[][] conflictos, boolean[] conflictosProbabilisticos) {
        this.conflictos = conflictos;
        this.conflictosProbabilisticos = conflictosProbabilisticos;
        // inicializa todas las banderas en false
        flags = new boolean[conflictos.length];
        Arrays.fill(flags, false);
    }

    @Override
    public int cual(boolean[] transiciones){
        int index = super.cual(transiciones);

        for (int i = 0; i < conflictos.length; i++) {
            int[] conflicto = conflictos[i];
            boolean conflicto80 = conflictosProbabilisticos[i];

            // transiciones en conflicto
            int tx = conflicto[0];
            int ty = conflicto[1];

            if (index != tx && index != ty)
                continue; // la transicion elegida no forma parte de este conflicto

            // verifico que ambas transiciones esten sensibilizadas y con algun hilo esperando
            if (!transiciones[tx]) {
                return ty;
            }
            if (!transiciones[ty]) {
                return tx;
            }

            // hay conflicto efectivo y hay hilos esperando para disparar ambas transiciones -> aplico la politica

            // pregunto si al conflicto se le aplica una politica del 80 %
            if (conflicto80) {
                // politica 80% hacia la izquierda
                boolean trans_izquierda = definirProbabilidad();
                if(trans_izquierda){
                    return tx;
                }
                return ty;
            }

            // aplico politica 50% para el resto de los conflictos
            flags[i] = !flags[i];           // cambio el valor de la bandera
            return flags[i] ? tx : ty;      // elijo la transicion correspondiente
        }

        return index;
    }

    /**
     * metodo utilizado por el metodo cual para aplicar el 80-20
     * @return boolean,
     */
    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T10 (Tp) ó probabilidad asociada a ty en general

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();

        // Compara el número aleatorio con la probabilidad asociada
        boolean izquierda = numeroAleatorio > probabilidadDerecha;

        return izquierda;

    }
}
