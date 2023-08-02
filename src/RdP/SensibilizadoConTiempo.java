package RdP;

import java.util.Arrays;

import static Constants.Constants.MAX_TIME;

public class SensibilizadoConTiempo {
    long [] timeStamps;     // en ms
    long [][] tiempos;      // en ms
    boolean [] esperando;   // indica si hay hilos esperando para ingresar en la ventana de tiempo en cada transicion
    public SensibilizadoConTiempo(long[][] tiempos) {
        timeStamps = new long[tiempos.length];
        esperando = new boolean[tiempos.length];

        this.tiempos = tiempos;

        Arrays.fill(esperando, false); // Inicializo como que no hay nadie esperando
        Arrays.fill(timeStamps, MAX_TIME); // inicializo el tiempo en el que las transiciones empiezan a estar sensibilizadas en el maximo
    }


    /**
     * Setea el timestamp de la transicion con el tiempo actual
     * @param transicion transicion a setear el timestamp
     */
    public void setTimeStamp(int transicion) {

        timeStamps[transicion] = System.currentTimeMillis();
    }
    public void setTimeStampReset(int transicion) {

        timeStamps[transicion] = MAX_TIME;
    }
    /**
     * Verifica si una transicion es inmediata (alfa == 0)
     * @param transicion transicion a verificar
     * @return true si es inmediata, false si no
     */
    public boolean isInmediata(int transicion) {
        return tiempos[transicion][0] == 0;
    }

    /**
     * Verifica si una transicion esta en la ventana de tiempo
     * @param transicion transicion a verificar
     * @return true si esta en la ventana, false si no
     */
    public boolean testVentana(int transicion) {
        long alfa = tiempos[transicion][0];
        long beta = tiempos[transicion][1];
        long ahora = System.currentTimeMillis();
        long timeStamp = timeStamps[transicion]; //Tiempo desde el cual empezo a estar sensibilizada
        return ((ahora - timeStamp) >= alfa) && ((ahora - timeStamp) <= beta);
    }

    /**
     * Verifica si una transicion esta esperando por su ventana de tiempo
     * @param transicion transicion a verificar
     * @return true si esta esperando, false si no
     */
    public boolean isEsperando(int transicion) {
        return esperando[transicion];
    }

    /**
     * Setea en false que la transicion esta esperando por su ventana de tiempo
     * @param transicion transicion a setear
     */
    public void resetEsperando(int transicion) {
        esperando[transicion] = false;
    }

    /**
     * Setea en true que la transicion esta esperando por su ventana de tiempo
     * @param transicion transicion a setear
     */
    public void setEsperando(int transicion) {
        esperando[transicion] = true;
    }

    /**
     * Verifica si esta antes de la ventana de tiempo (ahora-timestamp < alfa)
     * @param transicion transicion a verificar
     * @return true si esta antes de la ventana, false si no
     */
    public boolean antesDeLaVentana(int transicion) {
        long alfa = tiempos[transicion][0];
        long ahora = System.currentTimeMillis();
        long timeStamp = timeStamps[transicion];

        if (timeStamp > ahora) return false;

        return (ahora - timeStamp) < alfa;
    }

    /**
     * Devuelve el tiempo restante para que la transicion este en la ventana de tiempo
     * @param transicion transicion a verificar
     * @return tiempo restante en ms
     */
    public long getTiempoRestante(int transicion) {
        long alfa = tiempos[transicion][0];
        long ahora = System.currentTimeMillis();
        long timeStamp = timeStamps[transicion];
        long tiempoRestante = timeStamp + alfa - ahora;
        if (tiempoRestante < 0) tiempoRestante = 0;
        return tiempoRestante;
    }
}
