package RdP;

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
    private int cuenta_invariantes = 0;
    private final int[] trans_invariantes;
    public RdP (int[][] plazas_salida_transiciones, int[][] plazas_entrada_transiciones, int[] marcado_inicial, int[] trans_invariantes) {
        // Las columnas de la matriz de incidencia son transiciones
        // Las filas de la matriz de incidencia son plazas
        this.plazas_entrada_transiciones = plazas_entrada_transiciones;
        this.plazas_salida_transiciones  = plazas_salida_transiciones;
        this.marcado_actual = marcado_inicial;
        this.cantidad_plazas = marcado_inicial.length;
        this.cantidad_transiciones = plazas_entrada_transiciones[0].length; // Cantidad de columnas de la matriz
        this.trans_invariantes = trans_invariantes;
        // guardo copia del marcado inicial
        this.marcado_inicial = new int[cantidad_plazas];
        for (int i = 0; i < cantidad_plazas; i++) {
            this.marcado_inicial[i] = marcado_inicial[i];
        }
    }

    /**
     * Dispara una transicion de la RdP si es posible, actualizando el marcado actual
     * @param transicion  transicion a disparar
     * @return true si se pudo disparar, false si no
     */
    public boolean disparar(int transicion) {
        if (!isSensibilizada(transicion)) {
            return false;
        }

        // actualizo el marcado actual
        // saco tokens de las entradas y agrego a las salidas
        for (int i = 0; i < cantidad_plazas; i++) {
            marcado_actual[i] -= plazas_entrada_transiciones[i][transicion];
            marcado_actual[i] += plazas_salida_transiciones[i][transicion];
        }

        String invariante = "T"+transicion;
        System.out.println(invariante);

        for (int inv : trans_invariantes)
            if (inv == transicion){
                cuenta_invariantes ++;
                break;
            }

        return true;
    }

    private boolean isEstadoInicial() {
        for (int i = 0; i < cantidad_plazas; i++) {
            if(marcado_actual[i] != marcado_inicial[i]){
                return false;
            }
        }
        return true;
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
