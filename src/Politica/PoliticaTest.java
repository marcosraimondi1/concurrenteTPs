package Politica;

import java.util.Arrays;
import java.util.Random;

public class PoliticaTest implements Politica{
    private boolean c1 = false;   // bandera para conflicto 1 (T1 Y T7)
    private boolean condicion = false; // seleccion entre politica del 50% y el 80% (solo en etapa 1 en red de test)

    public PoliticaTest() {
    }
    public PoliticaTest(boolean Condicion) {
        this.condicion = Condicion;
    }

    public int cual(boolean[] transiciones){
        for (int i = (transiciones.length) -1  ; i >= 0; i--) {

            if (!transiciones[i])
                continue;

            /*if ((i+1) == 1)
            {
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                // en la RdP, T1 esta en conflicto con T7
                c1 = !c1;
                if (c1)
                    return i;// INDEX DE T1 = 0
                return 7-1;// INDEX DE T7 = 6
            }*/

            /*if ((i+1) == 2)
            {
                // las 2 transiciones correspondientes al conflicto estan sensibilizadas
                // politica 80% hacia la izquierda
                boolean trans_izquierda = definirProbabilidad();
                if(trans_izquierda){
                    return i; // INDEX DE T2 = 1
                }else{
                    return i+1; // INDEX DE T3 = 2
                }
            }*/


              return i;
        }
        //System.out.println(Arrays.toString(transiciones));
        throw new RuntimeException("No hay transiciones para disparar");
    }


    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T3

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();

        // Compara el número aleatorio con la probabilidad asociada
        if (numeroAleatorio < probabilidadDerecha) {
            // La transicion seleccionada es T3
            return false;
        } else {
            // La transicion seleccionada es T2
            return true;
        }
    }

}
