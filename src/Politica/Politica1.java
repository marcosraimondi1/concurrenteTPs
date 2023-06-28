package Politica;

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
    private boolean condicion = false; // seleccion entre politica del 50% y el 80% (solo en etapa 3)

    public Politica1() {
    }
    public Politica1(boolean Condicion) {
        this.condicion = Condicion;
    }

    public int cual(boolean[] transiciones){
        for (int i = 0; i < transiciones.length; i++) {
            if (!transiciones[i])
                continue;

            //if (i == 1 && transiciones[i+1])
            if (i == 1)
            {   // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                // en la RdP, T1 esta en conflicto con T2
                c1 = !c1;
                if (c1)
                    return i;
                return i+1;
            }
            /*else if (i == 1 || i == 2){
                //solo una de las 2 sensibilizadas y retorno esa
                return i;
            }*/
            //if (i == 5 && transiciones[i+1])
            if (i == 5)
            {   // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                c2 = !c2;
                if (c2)
                    return i;
                return i+1;
            }
            /*else if (i == 5 || i == 6){
                //solo una de las 2 sensibilizadas y retorno esa
                return i;
            }*/

            //if (i == 11 && transiciones[i+1])
            if(i == 11){
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                if(condicion){
                    //politica 80% hacia la izquierda
                    boolean trans_izquierda = definirProbabilidad();
                    if(trans_izquierda){
                        return i;
                    }else{
                        return i+1;
                    }
                }else{
                c3 = !c3;
                if (c3) {
                    return i;
                }
                return i+1;
                }

            }
            /*else if (i == 11 || i == 12){
                //solo una de las 2 sensibilizadas y retorno esa
                return i;
            }*/

            return i;
        }
        System.out.println(Arrays.toString(transiciones));
        throw new RuntimeException("No hay transiciones para disparar");
    }

    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T12

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();

        // Compara el número aleatorio con la probabilidad asociada
        if (numeroAleatorio < probabilidadDerecha) {
            // La transicion seleccionada es T12
            return false;
        } else {
            // La transicion seleccionada es T11
            return true;
        }
    }
}
