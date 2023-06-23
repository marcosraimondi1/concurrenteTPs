package RdP;

/**
 * Clase que representa una Red de Petri
 */
public class RdP {
    private int[][] plazas_salida_transiciones;      // matriz de incidencia +
    private int[][] plazas_entrada_transiciones;     // matriz de incidencia -
    private int[][] marcado_actual;     // estado de la RdP
    private int[][] marcado_inicial;    // estado inicial de la RdP
    private final int cantidad_plazas;        // cantidad de plazas de la RdP
    private final int cantidad_transiciones;  // cantidad de transiciones de la RdP
    public RdP (int[][] plazas_salida_transiciones, int[][] plazas_entrada_transiciones, int[][] marcado_inicial) {
        System.out.println("RdP");
        this.plazas_entrada_transiciones = plazas_entrada_transiciones;
        this.plazas_salida_transiciones  = plazas_salida_transiciones;
        this.marcado_inicial = marcado_inicial;
        this.marcado_actual = marcado_inicial;
        this.cantidad_plazas = plazas_entrada_transiciones.length;
        this.cantidad_transiciones = plazas_entrada_transiciones[0].length;
    }

    /**
     * Dispara una transicion de la RdP si es posible, actualizando el marcado actual
     * @param transicion  transicion a disparar
     * @return true si se pudo disparar, false si no
     */
    public boolean disparar(int transicion) {
        return false;
    }

    /**
     * Devuelve las transiciones sensibilizadas de la RdP
     * @return transiciones sensibilizadas
     */
    public boolean[] getSensibilizadas (){
        boolean[] sensibilizadas = new boolean[cantidad_transiciones];
        
        for (int i = 0; i < cantidad_transiciones; i++) {
            sensibilizadas[i] = true;
        }
        
        return sensibilizadas;
    }

}
