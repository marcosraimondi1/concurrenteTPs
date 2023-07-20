package Politica;

import java.util.Arrays;

/**
 * Politica1: para cada conflicto, se dispara la transicion que
 * no se haya disparado antes (Politica del 50%)
 */
public class Politica1 extends Politica {
    private final int[][] conflictos;
    private final boolean[] flags;

    /**
     * Constructor de la clase Politica1,
     * @param conflictos, los conflictos de la RdP
     */
    public Politica1(int[][] conflictos) {
        this.conflictos = conflictos;

        // inicializa todas las banderas en false
        flags = new boolean[conflictos.length];
        Arrays.fill(flags, false);
    }
    @Override
    public int cual(boolean[] transiciones){

        int index = super.cual(transiciones); // transicion aleatoria

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
            flags[i] = !flags[i];           // cambio el valor de la bandera
            index = flags[i] ? tx : ty;     // elijo la transicion correspondiente
        }

        return index;
    }

}
