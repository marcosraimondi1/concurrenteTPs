package RdP;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class VectorSensibilizadas {
    boolean[] sensibilizadas;
    boolean[] sensibilizadasAnterior;

    private final int[][] plazas_entrada_transiciones;     // matriz de incidencia - (denota las plazas a la entrada de una transición)

    private final SensibilizadoConTiempo sensibilizadoConTiempo;

    public VectorSensibilizadas (int[][] plazas_entrada_transiciones, int[] marcado_inicial, long[][] tiempos) {
        sensibilizadas                      = new boolean[tiempos.length];
        sensibilizadasAnterior              = new boolean[tiempos.length];
        this.plazas_entrada_transiciones    = plazas_entrada_transiciones;
        sensibilizadoConTiempo              = new SensibilizadoConTiempo(tiempos);

        Arrays.fill             (sensibilizadas         , false );
        Arrays.fill             (sensibilizadasAnterior , false );
        actualizarSensibilizadas(marcado_inicial);
    }

    public void actualizarSensibilizadas(int[] marcado) {
        for (int i = 0; i < sensibilizadas.length; i++) {
            sensibilizadas[i] = isSensibilizada(i, marcado);
            if (sensibilizadas[i] && (sensibilizadas[i] != sensibilizadasAnterior[i])) {
                // se sensibilizo la transicion i, actualizo el timestamp de la transicion
                 sensibilizadoConTiempo.setTimeStamp(i);
            }
            sensibilizadasAnterior[i] = sensibilizadas[i];
        }
    }


    /**
     * Verifica si una transicion esta sensibilizada con los tokens en cierto marcado
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
             // si es inmediata, esta sensibilizada solo con los tokens
             return true;
         }

        // verifico la ventana de tiempo
        boolean ventana = sensibilizadoConTiempo.testVentana(transicion);

        if (ventana) {

            boolean esperando = sensibilizadoConTiempo.isEsperando(transicion);

            if (!esperando) {
                // nadie durmiendo, esta sensibilizada
                // todo : sensibilizadoConTiempo.setTimeStamp(transicion); // ver si es necesario
                return true;
            }
            // esta sensibilizada pero hay alguien durmiendo
            // esperando = true
            // si alguien esta esperando, no esta sensibilizada
            return false;

        } else {
            boolean antes = sensibilizadoConTiempo.antesDeLaVentana(transicion);
            if (antes) {
                // si es antes libero el mutex y me voy a dormir
                sensibilizadoConTiempo.setEsperando(transicion);    // aviso que esta esperando
                // todo: Monitor.getMutex.release();

                long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(transicion);
                try {
                    Thread.sleep(tiempoRestante);
                } catch (InterruptedException e) {
                    // no deberia ser interrumpido creo
                    throw new RuntimeException(e);
                }

                // todo: Monitor.getMutex.acquire();

                sensibilizadoConTiempo.resetEsperando(transicion);  // aviso que ya no esta esperando

                // verifico que siga sensibilizada despues de dormir
                if (!sensibilizadas[transicion]) {
                    // si no tiene los tokens necesarios, no esta sensibilizada
                    return false;
                }

                // esta en la ventana de tiempo y tiene los tokens
                return true;

            } else {
                // si es despues, no esta sensibilizada y nunca va a poder dispararse
                // lanzo excepcion
                throw new TimeoutException("La transicion " + transicion + " se paso del tiempo maximo y no se puede disparar");
            }
        }
    }
}
