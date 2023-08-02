package Politica;

import java.util.Arrays;

/**
 * Politica1: para el conflicto de la Rdp del TP Final
 * para cada conflicto, se dispara la transicion que no se haya disparado antes (Politica del 50%)
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

        // Inicializa todas las banderas en false
        flags = new boolean[conflictos.length];
        Arrays.fill(flags, false);
    }
    @Override
    public int cual(boolean[] transiciones){

        int index = super.cual(transiciones); // Transicion a disparar selecionada aleatoriamente

        for (int i = 0; i < conflictos.length; i++) {
            int[] conflicto = conflictos[i];

            // Transiciones en conflicto
            int tx = conflicto[0];
            int ty = conflicto[1];

            if (index != tx && index != ty)
                continue; // La transicion elegida aleatoreamente no forma parte de este conflicto

            // Verifica que ambas transiciones esten sensibilizadas y con algun hilo esperando
            if (!transiciones[tx]) {
                return ty;
            }
            if (!transiciones[ty]) {
                return tx;
            }

            // Hay conflicto efectivo y hay hilos esperando para disparar ambas transiciones -> aplica la politica
            flags[i] = !flags[i];          // Cambia el valor de la bandera (togle).
            return flags[i] ? tx : ty;     // Elije la transicion correspondiente
        }

        return index; // Retorna el index aleatorio una vez verificado que no pertenece a un conflicto
    }

}
