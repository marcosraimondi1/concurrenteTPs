package RdP;

import Exceptions.InvariantePlazaException;
import Logger.Logger;


import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Clase que representa una Red de Petri
 */
public class RdP {
    private final int[][] plazas_salida_transiciones;      // matriz de incidencia + (denota las plazas a la salida de una transición)
    private final int[][] plazas_entrada_transiciones;     // matriz de incidencia - (denota las plazas a la entrada de una transición)
    private final int cantidad_plazas;                      // cantidad de plazas de la RdP
    private final int cantidad_transiciones;                // cantidad de transiciones de la RdP
    private final int[] marcado_actual;                     // estado de la RdP
    private final int[] marcado_inicial;                    // estado inicial de la RdP
    private boolean cortarEjecucion;
    private final int invariantes_MAX;
    private int cuenta_invariantes = 0;
    private final int[] trans_invariantes;
    private final int[][] invariantes_plazas;
    //private String invariante_real = "";
    private boolean apagar;
    private final ReadWriteLock lock;
    public final Logger logger = new Logger(".\\data\\log.txt");
    public RdP (int[][] plazas_salida_transiciones, int[][] plazas_entrada_transiciones, int[] marcado_inicial, int[] trans_invariantes, int[][] invariantes_plazas, int invariantes_MAX) {
        // Las columnas de la matriz de incidencia son transiciones
        // Las filas de la matriz de incidencia son plazas
        this.plazas_entrada_transiciones = plazas_entrada_transiciones;
        this.plazas_salida_transiciones  = plazas_salida_transiciones;
        this.marcado_actual = marcado_inicial;
        this.cantidad_plazas = marcado_inicial.length;
        this.cantidad_transiciones = plazas_entrada_transiciones[0].length; // Cantidad de columnas de la matriz
        this.trans_invariantes = trans_invariantes;
        this.invariantes_MAX = invariantes_MAX;
        this.apagar = false;
        this.cortarEjecucion = false;
        // guardo copia del marcado inicial
        this.marcado_inicial = new int[cantidad_plazas];
        for (int i = 0; i < cantidad_plazas; i++) {
            this.marcado_inicial[i] = marcado_inicial[i];
        }
        this.invariantes_plazas = invariantes_plazas;
        this.lock = new ReentrantReadWriteLock(); //creo lock para el Apagado
    }

    /**
     * Dispara una transicion de la RdP si es posible, actualizando el marcado actual
     * @param transicion  transicion a disparar
     * @return true si se pudo disparar, false si no
     */
    public boolean disparar(int transicion) {
        if(CortarEjecucion()){
            setApagar();
            return false;
        }
        //System.out.println("T"+transicion);
        if (!isSensibilizada(transicion)) {
            return false;
        }

        // actualizo el marcado actual
        // saco tokens de las entradas y agrego a las salidas
        for (int i = 0; i < cantidad_plazas; i++) {
            marcado_actual[i] -= plazas_entrada_transiciones[i][transicion];
            marcado_actual[i] += plazas_salida_transiciones[i][transicion];
        }


        verificarInvarianteTransicion(transicion);

        try {
            verificarInvariantePlaza();
        } catch (InvariantePlazaException e) {
            throw new RuntimeException(e.getMessage());
        }

        return true;
    }

    private void verificarInvariantePlaza () throws InvariantePlazaException {
        // la matriz de invariantes de plaza es una matriz que representa un conjunto de ecuaciones
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

    private void verificarInvarianteTransicion(int transicion) {
        boolean es_invariante = false;
        for (int inv : trans_invariantes)
            if (inv == transicion ){
                cuenta_invariantes ++;
                es_invariante = true;
                break;
            }

        String invariante = "T"+ transicion;
        //System.out.println(invariante);
        //if(transicion==12)
         //System.out.println(Arrays.toString(marcado_actual));
        if (es_invariante){
            System.out.println(invariante);
            if(cuenta_invariantes == invariantes_MAX){
                cortarEjecucion = true;
            }
        }
        logger.log(invariante);
    }

    /**
     * Devuelve las transiciones sensibilizadas de la RdP
     * @return transiciones sensibilizadas
     */
    public boolean[] getSensibilizadas (){
        boolean[] sensibilizadas = new boolean[cantidad_transiciones];
        
        for (int i = 0; i < cantidad_transiciones; i++) {
            // por cada transicion verifico si esta sensibilizada
            sensibilizadas[i] = isSensibilizada(i);
        }

        return sensibilizadas;
    }

    /**
     * Verifica si una transicion esta sensibilizada
     * @param transicion transicion a verificar
     * @return  true si esta sensibilizada, false si no
     */
    private boolean isSensibilizada(int transicion) {

        for (int i = 0; i < cantidad_plazas; i++) {
            // verifico si la plaza i tiene los tokens necesarios en el marcada actual
            if (marcado_actual[i] < plazas_entrada_transiciones[i][transicion]) {
               return false;
            }
        }
        return true;
    }

    public boolean getApagar() {
        lock.readLock().lock();
        boolean aux = apagar;
        lock.readLock().unlock();
        return aux;
    }
    private void setApagar() {
        lock.writeLock().lock();
        this.apagar = true;
        lock.writeLock().unlock();
    }
    public boolean CortarEjecucion() {
        return (cortarEjecucion && Arrays.equals(marcado_actual,marcado_inicial));
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
}
