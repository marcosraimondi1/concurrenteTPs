package RdP;

import Exceptions.InvariantePlazaException;
import Logger.Logger;

import Main.*;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Constants.Constants.*;

/**
 * Clase que representa la Red de Petri
 */
public class RdP {
    private final int[][]   plazas_salida_transiciones  ;   // matriz de incidencia + (denota las plazas a la salida de una transición)
    private final int[][]   plazas_entrada_transiciones ;   // matriz de incidencia - (denota las plazas a la entrada de una transición)
    private final int       cantidad_plazas             ;   // cantidad de plazas de la RdP
    private final int       cantidad_transiciones       ;   // cantidad de transiciones de la RdP
    private final int[]     marcado_actual              ;   // estado de la RdP
    private final int       invariantes_MAX             ;
    private int             cuenta_invariantes = 0      ;
    private final int[]     cuentas_invariantes_individual = new int[INVARIANTES.length];
    private final int[]     trans_invariantes           ;
    private final int[][]   invariantes_plazas          ;
    private boolean         apagar                      ;
    private String          secuencia_actual = ""       ;
    public final Logger     logger = new Logger(INV_LOG_PATH)   ;
    private final int[]     contadores                          ;   // cuenta la cantidad de veces que se disparo cada transicion
    private final ReadWriteLock         lock                    ;
    private final VectorSensibilizadas  vectorSensibilizadas    ;

    public RdP (int[][] plazas_salida_transiciones, int[][] plazas_entrada_transiciones, int[] marcado_inicial, int[] trans_invariantes, int[][] invariantes_plazas, long[][] tiempos, int invariantes_MAX) {
        this.plazas_entrada_transiciones = plazas_entrada_transiciones;
        this.plazas_salida_transiciones  = plazas_salida_transiciones;
        this.marcado_actual = marcado_inicial;
        this.cantidad_plazas = marcado_inicial.length;
        this.cantidad_transiciones = plazas_entrada_transiciones[0].length; // Cantidad de columnas de la matriz
        this.trans_invariantes = trans_invariantes;
        this.invariantes_MAX = invariantes_MAX;
        this.invariantes_plazas = invariantes_plazas;

        this.apagar = false;
        this.lock = new ReentrantReadWriteLock(); // creo lock para proteger variable apagar
        vectorSensibilizadas = new VectorSensibilizadas(plazas_entrada_transiciones, marcado_inicial, tiempos);
        contadores = new int[cantidad_transiciones];
        Arrays.fill(contadores, 0);
        Arrays.fill(cuentas_invariantes_individual, 0);
    }

    /**
     * Dispara una transicion de la RdP si es posible, actualizando el marcado actual
     * @param transicion  transicion a disparar
     * @return true , si se pudo disparar, false si no
     */
    public boolean disparar(int transicion) {

        if (isApagada())
            return true;

        if(cuenta_invariantes >= invariantes_MAX){
            setApagar();    // apagar = true
            return true;
        }

        try {
            if (!vectorSensibilizadas.isSensibilizada(transicion)) {
                return false;
            }
        } catch (TimeoutException e) {
            System.out.println("TimeoutException en T"+transicion);
            throw new RuntimeException(e);
        }

        // Se puede disparar la transicion
        // actualizo el marcado actual
        // saco tokens de las entradas y agrego a las salidas
        for (int i = 0; i < cantidad_plazas; i++) {
            marcado_actual[i] -= plazas_entrada_transiciones[i][transicion];
            marcado_actual[i] += plazas_salida_transiciones[i][transicion];
        }

        // log transicion
        String trans = "T"+ transicion;
        logger.log(trans);

        secuencia_actual+=trans;

        contadores[transicion]++;

        verificarInvarianteTransicion(transicion);

        try {
            verificarInvariantePlaza();
        } catch (InvariantePlazaException e) {
            Main.STATE_LOG("ERROR", "RdP", Thread.currentThread().getName(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        vectorSensibilizadas.actualizarSensibilizadas(marcado_actual);

        return true;
    }

    /**
     * Verifica el cumplimiento de los invariantes de transicion debido a que previamente disparamos la transicion
     */
    private void verificarInvariantePlaza () throws InvariantePlazaException {
        // la matriz de invariantes de plaza es una matriz que representa un conjunto de ecuaciones.A modo de ejemplo:
        /*
        [ 0 1 1   1 ] -> P2 + P3 = 1
        [ 1 1 1   2 ] -> P1 + P2 + P3 = 2
        [ 1 0 1   1 ] -> P1 + P3 = 1
         */

        for (int j = 0; j < invariantes_plazas.length; j++) {
            // itero por la cantidad de invariantes de plaza
            int[] invariante = invariantes_plazas[j];
            int suma = 0;
            for (int i = 0; i < cantidad_plazas ; i++){
                suma += marcado_actual[i] * invariante[i];
            }

            // verifico la suma
            if (suma == invariante[invariante.length - 1])
                continue;


            String s = "No se cumplio el invariante "+j+" en el marcado " + Arrays.toString(marcado_actual);

            throw new InvariantePlazaException(s);

        }
    }

    /**
     * verifica si se disparo la transicion T14, para aumentar la cuenta_invariantes(marca una vuelta mas)
     * @param transicion  transicion disparada
     */
    private void verificarInvarianteTransicion(int transicion) {
        for (int inv : trans_invariantes) {
            if (inv != transicion)
                continue;

            cuenta_invariantes++;

            // obtengo el invariante
            Pattern pattern = Pattern.compile(REGEX);               // creo el objeto de la regex
            Matcher matcher = pattern.matcher(secuencia_actual);    // busco el invariante en la secuencia actual

            String invariante   = matcher.replaceAll(REPLACE_INV);    // me quedo con el invariante

            matcher = pattern.matcher(secuencia_actual);


            secuencia_actual    = matcher.replaceAll(REPLACE);        // me quedo con lo que no forme parte del invariante

            for (int i = 0; i < INVARIANTES.length; i++) {
                if (invariante.equals(INVARIANTES[i])) {
                    cuentas_invariantes_individual[i]++;
                    break;
                }
            }


            break;
        }
    }

    /**
     * Devuelve las transiciones sensibilizadas de la RdP
     * @return transiciones sensibilizadas
     */
    public boolean[] getSensibilizadas (){
        return vectorSensibilizadas.getSensibilizadas();
    }

    /**
     * Nos permite leer el estado de la variable apagar.
     * @return boolean
     */
    public boolean isApagada() {
        lock.readLock().lock();
        boolean aux = apagar;
        lock.readLock().unlock();
        return aux;
    }

    /**
     * seteamos apagar en true . con lock nos aseguramos que solamente haya un hilo escribiendo esa variable.
     */
    private void setApagar() {
        lock.writeLock().lock();
        this.apagar = true;
        lock.writeLock().unlock();
    }

    public int[] getMarcadoActual() {
        return marcado_actual;
    }

    public int getCantidadTransiciones() {
        return cantidad_transiciones;
    }

    public int getCuentaInvariantes(){
        return cuenta_invariantes;
    }
    public int[] getCuentasInvariantes(){
        return cuentas_invariantes_individual;
    }

    public int[] getContadores() {
        return contadores;
    }
}
