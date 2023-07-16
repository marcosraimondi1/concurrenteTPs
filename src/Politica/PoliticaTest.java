package Politica;

import java.util.Arrays;
import java.util.Random;

public class PoliticaTest implements Politica{
    private boolean c1 = false;     // bandera para conflicto 1
    private int contadorT2 = 0;
    private int contadorT3 = 0;
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


              return i;
        }
        System.out.println(Arrays.toString(transiciones));
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
    public int getContadorT2() {
        return contadorT2;
    }

    public int getContadorT3() {
        return contadorT3;
    }
}
