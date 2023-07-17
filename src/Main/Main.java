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
        int[] trans_invariantes = new int[]{14};                      // transiciones para contar invariantes (T14 marca una vuelta)
        int[][] invariantes_plazas = getInvariantesPlazas();
        int invariantes_MAX = 200;

        RdP rdp = new RdP(plaza_salida,plaza_entrada,marcado, trans_invariantes,invariantes_plazas,invariantes_MAX);

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
                    //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14
                     {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P0
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P1
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P2
                     {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1  },//P3
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P4
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P5
                     {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P6
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0  },//P7
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0  },//P8
                     {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0,  0,  0,  0,  0  },//P9
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0  },//P10
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0  },//P11
                     {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0,  0,  0,  0,  0  },//P12
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0  },//P13
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  1,  0,  0  },//P14
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  0,  0,  0,  0  },//P15
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  1,  0,  0  },//P16
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  0  },//P17
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1  },//P18

            };
        }else{
            //W-
            matriz = new int[][]{
                    //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14
                     {0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P0
                     {0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P1
                     {0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P2
                     {0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0  },//P3
                     {0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P4
                     {0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P5
                     {0, 0, 0, 0, 0, 1, 1, 0, 0, 0,  0,  0,  0,  0,  0  },//P6
                     {0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0  },//P7
                     {0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0,  0  },//P8
                     {0, 0, 0, 0, 0, 1, 1, 0, 0, 0,  0,  0,  0,  0,  0  },//P9
                     {0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0,  0  },//P10
                     {0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0,  0  },//P11
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  1,  0,  0,  0,  0  },//P12
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  0,  0,  0  },//P13
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  1,  0,  0,  0,  0  },//P14
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  0,  0  },//P15
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0  },//P16
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1  },//P17
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0  },//P18
                     //{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0  },
            };
        }

        return matriz;
    }
    public static int[] getMarcadoInicial(){
                     // P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18
        return new int[]{0, 1, 0, 3, 0, 1, 0, 1, 0, 2,  0,  1,  0,  0,  1,  0,  0, 0, 1};
    }
    public static int[][] getInvariantesPlazas() {
    /*
        0. M(P0) + M(P2) + M(P4) + M(P6) + M(P8) + M(P10) + M(P12) + M(P13) + M(P15) + M(P16) + M(P17) = N (Numero de tokens en P0) ESTA MAL SI CONSIDERAMOS TRANSICIÓN FUENTE
        1. M(P1) + M(P2) = 1
        2. M(P4) + M(P5) = 1
        3. M(P13) + M(P14) + M(P15) = 1
        4. M(P7) + M(P8) = 1
        5. M(P10) + M(P11) = 1
        6. M(P8) + M(P9) + M(P10) = 2
        7. M(P17) + M(P18) = 1
        8. M(P2) + M(P3) + M(P4) + M(P17) = 3
    */
        return new int[][]{
                // P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18 = X
                  {0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0,    1 },
                  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0,    1 },
                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  1,  1,  0,  0, 0,    1 },
                  {0, 0, 0, 0, 0, 0, 0, 1, 1, 0,  0,  0,  0,  0,  0,  0,  0,  0, 0,    1 },
                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  1,  0,  0,  0,  0,  0,  0, 0,    1 },
                  {0, 0, 0, 0, 0, 0, 0, 0, 1, 1,  1,  0,  0,  0,  0,  0,  0,  0, 0,    2 },
                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  1, 1,    1 },
                  {0, 0, 1, 1, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  1, 0,    3 },
        };

    }
}

