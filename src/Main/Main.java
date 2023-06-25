package Main;

import Monitor.Monitor;
import Politica.Politica1;
import RdP.RdP;

public class Main {
    public static void main(String[] args) {
        //------------------------------Inicio Politica----------------------------------------------//

        Politica1 politica = new Politica1();

        //------------------------------Inicio RdP---------------------------------------------------//

        int[][] plaza_salida = getMatricesDeIncidencia(true);  // plazas a la salida de la transición (Matriz)
        int[][] plaza_entrada = getMatricesDeIncidencia(false);// plazas a la entrada de la transición
        int[] marcado = getMarcadoInicial();                           // marcado inicial
        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado);

        //------------------------------Inicio Monitor-----------------------------------------------//

        Monitor monitor = new Monitor(rdp,politica);

        //HACER COSAS CON EL MONITOR
        //CONTINUARA..........

    }
    public static int[][] getMatricesDeIncidencia(boolean numero){

        int[][] matriz;

        if(numero){
            // W+
            matriz = new int[][]{
                    //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14 T15 T16
                     {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P0
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P1
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P2
                     {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  1},//P3
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P4
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P5
                     {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P6
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0,  0,  0},//P7
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P8
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,  0,  0,  0,  0,  0,  0},//P9
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P10
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  0,  0,  0,  0,  0,  0},//P11
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P12
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0,  0,  0},//P13
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,  0,  0,  0,  0,  0,  0},//P14
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  1,  0,  0},//P15
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  0,  0,  0,  0,  0},//P16
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  0,  0,  0,  0},//P17
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  1,  0,  0},//P18
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1,  0},//P19
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  1},//P20
            };
        }else{
            //W-
            matriz = new int[][]{
                    //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14 T15 T16
                     {0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P0
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P1
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P2
                     {0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1,  0},//P3
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P4
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P5
                     {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P6
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P7
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P8
                     {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P9
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0,  0,  0},//P10
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},//P11
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0,  0,  0},//P12
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  0,  0,  0,  0,  0,  0},//P13
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  1,  0,  0,  0,  0},//P14
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  1,  0,  0,  0,  0},//P15
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  0,  0,  0},//P16
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0,  0},//P17
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1,  0},//P18
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  1},//P19
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1,  0},//P20
                     //{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0},
            };
        }

        return matriz;
    }
    public static int[] getMarcadoInicial(){
                                  //P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18 P19 P20

        return new int[]{0, 1, 0, 3, 0, 1, 0, 1, 0,  2,  0,  1,  0,  0,  0,  1,  0,  0,  0,  0, 1};
    }
}

