package RdP;

import Monitor.Monitor;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class VectorSensibilizadas {
    private final boolean[] sensibilizadas              ;    // Transiciones sensibilizadas
    private final boolean[] sensibilizadasAnterior      ;
    private final int[][]   plazas_entrada_transiciones ;     // Matriz de incidencia - (denota las plazas a la entrada de una transición)
    private final SensibilizadoConTiempo sensibilizadoConTiempo;

    public VectorSensibilizadas (int[][] plazas_entrada_transiciones, int[] marcado_inicial, long[][] tiempos) {
        sensibilizadas                      = new boolean[tiempos.length];
        sensibilizadasAnterior              = new boolean[tiempos.length];
        this.plazas_entrada_transiciones    = plazas_entrada_transiciones;
        sensibilizadoConTiempo              = new SensibilizadoConTiempo(tiempos);

        Arrays.fill             (sensibilizadas         , false );
        Arrays.fill             (sensibilizadasAnterior , false );


        // Actualiza sensibilizadas de transiciones fuente
        boolean seActualizo = false;
        for (int transicion = 0; transicion < plazas_entrada_transiciones[0].length; transicion++ ) {

            if (isTransicionFuente(transicion)){
                seActualizo = true;
                actualizarSensibilizadas(marcado_inicial, transicion); // Actualiza el marcado inicial incluyendo al tiempo de las transiciones fuente
            }
        }
        if(!seActualizo){
            actualizarSensibilizadas(marcado_inicial, -1);
        }
    }

    /**
     * Actualiza las transiciones sensibilizadas ya que al disparar la transicion se modifico el marcado.
     * En caso de que haya cambiado el estado de la transicion, seteo el time stamp.
     */
    public void actualizarSensibilizadas(int[] marcado, int transicion) {
        // Para el caso de T0 el timeStamp correspondiente se actualiza siempre que se dispare esta transición

        for (int i = 0; i < sensibilizadas.length; i++) {
            sensibilizadas[i] = isSensibilizada(i, marcado); // Verifica si es sensibilizado por tokens solamente

            boolean isTransfuente = isTransicionFuente(transicion) && transicion == i; // Condicion para setear el timeStamp de transición fuente (si no se dispara una transición fuente se manda el -1)
            if ((sensibilizadas[i] && (sensibilizadas[i] != sensibilizadasAnterior[i])) || isTransfuente) {
                // Se sensibilizo la transicion i, actualizo el timestamp de la transicion
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
            // Verifica si la plaza i tiene los tokens necesarios en el marcada actual
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
            // Si no tiene los tokens necesarios, no esta sensibilizada
            return false;
        }

        if (sensibilizadoConTiempo.isInmediata(transicion)) {
             // Si es inmediata, esta sensibilizada solo con los tokens, lo cual se verifico arriba.
             return true;
        }

        // Verifica la ventana de tiempo
        boolean ventana = sensibilizadoConTiempo.testVentana(transicion);

        if (ventana) {
            // Llega el hilo dentro de la ventana de tiempo
            boolean esperando = sensibilizadoConTiempo.isEsperando(transicion);
            if (!esperando) {
                // Nadie durmiendo, esta sensibilizada
                sensibilizadoConTiempo.setTimeStamp(transicion); // Si puede disparar resetea su timesStamp al maximo como muestra de que se debe actualizar
                return true;
            }
            // Esta sensibilizada pero hay alguien durmiendo(esperando = true)
            // Si alguien esta esperando, no esta sensibilizada
            return false;
        } else {
            // NO llego el hilo dentro de la ventana de tiempo
            boolean antes = sensibilizadoConTiempo.antesDeLaVentana(transicion);
            if (antes) {
                // Si es antes libera el mutex y se va a dormir
                sensibilizadoConTiempo.setEsperando(transicion);    // Avisa que esta esperando
                Monitor.getMonitor().getMutex().release();

                long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(transicion);
                try {
                    Thread.sleep(tiempoRestante);
                } catch (InterruptedException e) {
                    // No deberia ser interrumpido
                    throw new RuntimeException(e);
                }

                try {
                    Monitor.getMonitor().getMutex().acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                sensibilizadoConTiempo.resetEsperando(transicion);  // Avisa que ya no esta esperando

                // Verifica que siga sensibilizada despues de dormir
                if (!sensibilizadas[transicion]) {
                    // Si no tiene los tokens necesarios, no esta sensibilizada
                    return false;
                }

                // Esta en la ventana de tiempo y tiene los tokens, por lo tanto se puede disparar
                return true;

            } else {
                // Si es despues, no esta sensibilizada y no va a poder dispararse
                // Lanza excepcion
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

    private boolean isTransicionFuente(int transicion) {
        if (transicion < 0)
            return false;

        // Revisa que toda la columna de la transicion sea 0
        for (int[] fila : plazas_entrada_transiciones)
            if (fila[transicion] != 0)
                return false;
        return true;
    }



}
