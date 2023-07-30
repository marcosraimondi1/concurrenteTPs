package Politica;


import java.util.Random;

public class PoliticaTest extends Politica{
    public PoliticaTest() {
    }

    @Override
    public int cual(boolean[] transiciones){
        for (int i = 0; i < transiciones.length; i++) {

            if (!transiciones[i])
                continue;

            if (i == 1)
            {
                if(!transiciones[i+1]) // verifico que ambas transiciones esten sensibilizadas
                    return i;

                // politica 80% hacia la izquierda
                boolean trans_izquierda = definirProbabilidad();
                if(trans_izquierda){
                    return i;  // INDEX DE T2 = 1
                }else{
                    return i+1; // INDEX DE T3 = 2  (20%)
                }
            }

              return i;
        }
        throw new RuntimeException("No hay transiciones para disparar");
    }


    private boolean definirProbabilidad() {

        double probabilidadDerecha = 0.2; // Probabilidad asociada a la T3

        // Crea una instancia de la clase Random
        Random random = new Random();

        // Genera un número aleatorio entre 0 y 1
        double numeroAleatorio = random.nextDouble();

        // Compara el número aleatorio con la probabilidad asociada
        if (numeroAleatorio <= probabilidadDerecha) {
            // La transicion seleccionada es T3
            return false;
        } else {
            // La transicion seleccionada es T2
            return true;
        }
    }

}
