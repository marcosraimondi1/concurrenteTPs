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
        for (int i = 0; i < transiciones.length; i++) {
            if (!transiciones[i])
                continue;

            if (i == 1 && transiciones[i+1]){
                // las 2 transiciones correspondientes al conflicto esta sensibilizadas
                if(condicion){
                    //politica 80% hacia la izquierda
                    boolean trans_izquierda = definirProbabilidad();
                    if(trans_izquierda){
                        contadorT2++;
                        return i;
                    }else{
                        contadorT3++;
                        return i+1;
                    }
                }else{
                    c1 = !c1;
                    if (c1) {
                        contadorT2++;
                        return i;
                    }
                    contadorT3++;
                    return i+1;
                }
            }else if(i == 1 || i ==2){
                //solo una de las 2 sensibilizadas y retorno esa
                if(i == 1){
                    contadorT2++;
                    return i;
                }
                contadorT3++;
                return i;
            }


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
