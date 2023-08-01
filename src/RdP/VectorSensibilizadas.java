package RdP;

import Monitor.Monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class VectorSensibilizadas {
    private final boolean[] sensibilizadas              ;    // transiciones sensibilizadas
    private final boolean[] sensibilizadasAnterior      ;
    private final int[][]   plazas_entrada_transiciones ;     // matriz de incidencia - (denota las plazas a la entrada de una transición)
    private final SensibilizadoConTiempo sensibilizadoConTiempo;

    public VectorSensibilizadas (int[][] plazas_entrada_transiciones, int[] marcado_inicial, long[][] tiempos ,int[] trans_fuente ) {
        sensibilizadas                      = new boolean[tiempos.length];
        sensibilizadasAnterior              = new boolean[tiempos.length];
        this.plazas_entrada_transiciones    = plazas_entrada_transiciones;
        sensibilizadoConTiempo              = new SensibilizadoConTiempo(tiempos);

        Arrays.fill             (sensibilizadas         , false );
        Arrays.fill             (sensibilizadasAnterior , false );
        for(int fuente:trans_fuente) {
            actualizarSensibilizadas(marcado_inicial,fuente); //actualizo el marcado inicial incluyendo al tiempo de las transiciones fuente
        }

    }

    /**
     * Actualiza las transiciones sensibilizadas ya que al disparar la transicion se modifico el marcado.
     * En caso de que haya cambiado el estado de la transicion, seteo el time stamp.
     */
    public void actualizarSensibilizadas(int[] marcado, int transicion) {
        // para el caso de T0 el timeStamp correspondiente se actualiza siempre que se dispare esta transición
        for (int i = 0; i < sensibilizadas.length; i++) {
            sensibilizadas[i] = isSensibilizada(i, marcado); //verifico si es sensibilizado por tokens solamente
            boolean isTransfuente = transicion == i; // condicion para setear el timeStamp de transición fuente
            if (sensibilizadas[i] && (sensibilizadas[i] != sensibilizadasAnterior[i])|| isTransfuente) {
                // se sensibilizo la transicion i, actualizo el timestamp de la transicion
                 sensibilizadoConTiempo.setTimeStamp(i);
            }
            sensibilizadasAnterior[i] = sensibilizadas[i];
        }
    }


    /**
     * Verifica si una transicion esta sensibilizada con los tokens en cierto marcado.
     * Metodo utilizado solamente por actualizarSenbilizadas
     * @param transicion transicion a verificar
     * @param marcado marcado en el que debe verificar si esta sensibilizada
     * @return  true si esta sensibilizada, false si no
     */
    private boolean isSensibilizada(int transicion, int[] marcado) {

        for (int i = 0; i < marcado.length; i++) {
            // verifico si la plaza i tiene los tokens necesarios en el marcada actual
            if (marcado[i] < plazas_entrada_transiciones[i][transicion]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si una transicion esta sensibilizada por tokens y tiempo
     * @param transicion transicion a verificar
     * @return true si esta sensibilizada, false si no
     * @throws TimeoutException si la transicion se paso del tiempo maximo
     */
    public boolean isSensibilizada(int transicion) throws TimeoutException {

        if (!sensibilizadas[transicion]) {
            // si no tiene los tokens necesarios, no esta sensibilizada
            return false;
        }

        if (sensibilizadoConTiempo.isInmediata(transicion)) {
             // si es inmediata, esta sensibilizada solo con los tokens, lo cual se verifico arriba.
             return true;
        }

        // verifico la ventana de tiempo
        boolean ventana = sensibilizadoConTiempo.testVentana(transicion);

        if (ventana) {
            // llego el hilo dentro de la ventana de tiempo
            boolean esperando = sensibilizadoConTiempo.isEsperando(transicion);
            if (!esperando) {
                // nadie durmiendo, esta sensibilizada
                sensibilizadoConTiempo.setTimeStampReset(transicion); // si puedo disparar reseteo mi timesStamp al maximo como muestra de que se debe actualizar
                return true;
            }
            // esta sensibilizada pero hay alguien durmiendo(esperando = true)
            // si alguien esta esperando, no esta sensibilizada
            return false;
        } else {
            // NO llego el hilo dentro de la ventana de tiempo
            boolean antes = sensibilizadoConTiempo.antesDeLaVentana(transicion);
            if (antes) {
                // si es antes libero el mutex y me voy a dormir
                sensibilizadoConTiempo.setEsperando(transicion);    // aviso que esta esperando
                Monitor.getMonitor().getMutex().release();

                long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(transicion);
                try {
                    Thread.sleep(tiempoRestante);
                } catch (InterruptedException e) {
                    // no deberia ser interrumpido
                    throw new RuntimeException(e);
                }

                try {
                    Monitor.getMonitor().getMutex().acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                sensibilizadoConTiempo.resetEsperando(transicion);  // aviso que ya no esta esperando

                // verifico que siga sensibilizada despues de dormir
                if (!sensibilizadas[transicion]) {
                    // si no tiene los tokens necesarios, no esta sensibilizada
                    return false;
                }

                // esta en la ventana de tiempo y tiene los tokens,por lo tanto se puede disparar
                return true;

            } else {
                // si es despues, no esta sensibilizada y no va a poder dispararse
                // lanzo excepcion
                throw new TimeoutException("La transicion " + transicion + " se paso del tiempo maximo y no se puede disparar");
            }
        }
    }

    /**
     * Devuelve el vector que indica, cada transicion, si esta sensibilizada o no
     * @return boolean[] vector que indica el estado de las transiciones
     */
    public boolean[] getSensibilizadas (){
        return sensibilizadas;
    }



}
