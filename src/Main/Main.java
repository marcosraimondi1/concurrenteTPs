package Main;

import RdP.RdP;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

    }
    public RdP crearRed(){
        //crear red
        int[][] plazas_salida = null;
        int[][] plazas_entrada = null;
        int[][] marcado_inicial = null;
        
        return new RdP(plazas_salida,plazas_entrada,marcado_inicial);
    }
}

