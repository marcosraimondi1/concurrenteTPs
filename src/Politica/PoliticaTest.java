package Politica;

import java.util.Arrays;
import java.util.Random;

public class PoliticaTest implements Politica{
    private boolean c1 = false;     // bandera para conflicto 1
    private boolean c2 = false;     // bandera para conflicto 2
    private boolean c3 = false;     // bandera para conflicto 3
    private boolean condicion = false; // seleccion entre politica del 50% y el 80% (solo en etapa 3)

    public PoliticaTest() {
    }
    public PoliticaTest(boolean Condicion) {
        this.condicion = Condicion;
    }

    public int cual(boolean[] transiciones){
        for (int i = 0; i < transiciones.length; i++) {
            if (!transiciones[i])
                continue;

            if (i == 1)
            {
                // en la RdP, T1 esta en conflicto con T2
                c1 = !c1;
                if (c1)
                    return i;
                return i+1;
            }

            if (i == 5)
            {
                c2 = !c2;
                if (c2)
                    return i;
                return i+1;
            }

            if (i == 11){
                if(condicion){
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
