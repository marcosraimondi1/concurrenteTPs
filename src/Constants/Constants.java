package Constants;

public class Constants {

    public static String INV_LOG_PATH = ".\\data\\log.txt";
    public static String STATE_LOG_PATH = ".\\data\\stateLog.txt";

    public static long MAX_TIME = 253370764800000L; // 9999-01-01 00:00:00 en ms

    public static int INVARIANTES_MAX = 200; // cantidad de invariantes a realizar

    //------------------------------Constantes de TP2----------------------------------------------//
    public static boolean POLITICA1 = false;

    /**
     * Secuencias de disparo asignadas a cada hilo.
     * @return int[][] .
     */
    public static int[][] GET_SECUENCIAS_TP2(){
//          return getSecuencias1(); // hilos del paper
//          return getSecuencias2(); // se agrega secuencia para hilos para T9 y T10
        return getSecuencias3(); // se agrega secuencia para hilos para cada transicion en conflicto

//        return new int[][] {
//                {0,1,3,5,7,10,12,13,14},
//                {0,1,3,5,7,9,11,13,14},
//                {0,1,3,6,8,10,12,13,14},
//                {0,1,3,6,8,9,11,13,14},
//                {0,2,4,5,7,10,12,13,14},
//                {0,2,4,5,7,9,11,13,14},
//                {0,2,4,6,8,10,12,13,14},
//                {0,1,3,6,8,9,11,13,14},
//        };
//      return new int[][] {{0, 1, 3, 5, 7, 9, 11, 13, 14}}; // para secuencializado
//      return new int[][] {
//                {0},
//                {1, 3},
//                {2, 4},
//                {5, 7},
//                {6, 8},
//                {9, 11, 13, 14},
//        }; // para semi secuencializado
    }

    /**
     * Tiempos (en ms) para cada transicion de la red del tp
     */
    public static long[][] TIEMPOS = new long[][] {
            // alfa ,       beta
            {    10L,    MAX_TIME }, // T0
            {     0L,    MAX_TIME }, // T1
            {     0L,    MAX_TIME }, // T2
            {    20L,    MAX_TIME }, // T3
            {    20L,    MAX_TIME }, // T4
            {     0L,    MAX_TIME }, // T5
            {     0L,    MAX_TIME }, // T6
            {    20L,    MAX_TIME }, // T7
            {    20L,    MAX_TIME }, // T8
            {     0L,    MAX_TIME }, // T9
            {     0L,    MAX_TIME }, // T10
            {    20L,    MAX_TIME }, // T11
            {    20L,    MAX_TIME }, // T12
            {     0L,    MAX_TIME }, // T13
            {    10L,    MAX_TIME }  // T14

    };

    /**
     * Marcado inicial de la red del tp
     */
    public static int[] MI_TP2 = new int[] { 0, 1, 0, 3, 0, 1, 0, 1, 0, 2, 0, 1, 0, 0, 1, 0, 0, 0, 1 }; // P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18

    /**
     * Matriz de incidencia de entrada, contiene el peso de los brazos que entran a las transiciones desde las plazas.
     */
    public static int[][] W_MENOS_TP2 = new int[][] {
            // T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14
            {   0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P0
            {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P1
            {   0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P2
            {   0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0 }, // P3
            {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P4
            {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P5
            {   0, 0, 0, 0, 0, 1, 1, 0, 0, 0,  0,  0,  0,  0,  0 }, // P6
            {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P7
            {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0,  0 }, // P8
            {   0, 0, 0, 0, 0, 1, 1, 0, 0, 0,  0,  0,  0,  0,  0 }, // P9
            {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0,  0 }, // P10
            {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0,  0 }, // P11
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  1,  0,  0,  0,  0 }, // P12
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  0,  0,  0 }, // P13
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  1,  0,  0,  0,  0 }, // P14
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  1,  0,  0 }, // P15
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0 }, // P16
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1 }, // P17
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0 }, // P18
    };

    /**
     * Matriz de incidencia de salida, contiene el peso de los brazos que salen de las transiciones y van hacia las plazas.
     */
    public static int[][] W_MAS_TP2 = new int[][] {
            // T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13 T14
            {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P0
            {   0, 0, 0, 1, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P1
            {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P2
            {   0, 0, 0, 1, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1 }, // P3
            {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P4
            {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P5
            {   0, 0, 0, 1, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P6
            {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0,  0,  0,  0,  0,  0 }, // P7
            {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0 }, // P8
            {   0, 0, 0, 0, 0, 0, 0, 1, 1, 0,  0,  0,  0,  0,  0 }, // P9
            {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0,  0,  0,  0,  0,  0 }, // P10
            {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0,  0,  0,  0,  0,  0 }, // P11
            {   0, 0, 0, 0, 0, 0, 0, 1, 1, 0,  0,  0,  0,  0,  0 }, // P12
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1,  0,  0,  0,  0,  0 }, // P13
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0 }, // P14
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  1,  0,  0,  0,  0 }, // P15
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0 }, // P16
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  1,  0 }, // P17
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  1 }, // P18

    };

    /**
     * Conflictos (3) de la red, surgidos debido a transiciones que tienen el mismo conjunto de plazas de entrada
     */
    public static int[][] CONFLICTOS_TP2 = new int[][] {
            { 1, 2         },   // conflicto1
            { 5, 6         },   // conflicto2
            { 9, 10        },   // conflicto3
    };

    /**
     * Conflictos (3) de la red que tienen una politica del 80%/20%
     */
    public static boolean[] CONFLICTOS_TP2_80 = new boolean[]{
            false, // conflicto1
            false, // conflicto2
            true   // conflicto3
    };

    /**
     * transicion para contar invariantes (T14 marca una vuelta)
     */
    public static int [] T_INV_TP2 = new int[] {14};

    /*  INVARIANTES DE PLAZA TP
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

    /**
     * Invariantes de plaza de la red del tp
     */
    public static int[][] P_INV_TP2 = new int[][]{
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

    /**
     * Expresion regular
     */
     public static String REGEX = "(?<I0>T0)(?<T0>(?:T\\d+)*?)(?:(?<I1>T1)(?<T1>(?:T\\d+)*?)(?<I3>T3)|(?<I2>T2)(?<T2>(?:T\\d+)*?)(?<I4>T4))(?<T3T4>(?:T\\d+)*?)(?:(?<I5>T5)(?<T5>(?:T\\d+)*?)(?<I7>T7)|(?<I6>T6)(?<T6>(?:T\\d+)*?)(?<I8>T8))(?<T7T8>(?:T\\d+)*?)(?:(?<I10>T10)(?<T10>(?:T\\d+)*?)(?<I12>T12)|(?<I9>T9)(?<T9>(?:T\\d+)*?)(?<I11>T11))(?<T11T12>(?:T\\d+)*?)(?<I13>T13)(?<T13>(?:T\\d+)*?)(?<I14>T14)(?<T14>(?:T\\d+)*?)";

    /**
     * Grupos de reemplazo
     */
    public static String REPLACE = "${T0}${T1}${T2}${T3T4}${T5}${T6}${T7T8}${T9}${T10}${T11T12}${T13}${T14}";
    public static String REPLACE_INV = "-${I0}${I1}${I3}${I2}${I4}${I5}${I7}${I6}${I8}${I10}${I12}${I9}${I11}${I13}${I14}-${T0}${T1}${T2}${T3T4}${T5}${T6}${T7T8}${T9}${T10}${T11T12}${T13}${T14}";

    public static String[] INVARIANTES = new String[]{
            "T0T1T3T5T7T10T12T13T14"    ,
            "T0T1T3T6T8T10T12T13T14"    ,
            "T0T2T4T6T8T10T12T13T14"    ,
            "T0T2T4T5T7T10T12T13T14"    ,
            "T0T1T3T5T7T9T11T13T14"     ,
            "T0T1T3T6T8T9T11T13T14"     ,
            "T0T2T4T5T7T9T11T13T14"     ,
            "T0T2T4T6T8T9T11T13T14"
    };

    /**
     * Devuelve la secuencia de disparo asignada a cada hilo, segun el algoritmo del paper.
     * @return int[][] secuencias
     */
    private static int[][] getSecuencias1() {
        return new int[][]{
                { 0       } ,
                { 1, 3    } ,
                { 2, 4    } ,
                { 5, 7    } ,
                { 6, 8    } ,
                { 9, 11   } ,
                { 10, 12  } ,
                { 13, 14  }
        };
    }

    /**
     * A cada hilo se le asigna una secuencia de disparo.
     * Se agregan dos hilos dedicados para disparar T9 y T10 para que funcione bien la politica2
     * Devuelve la secuencia de disparo asignada a cada hilo.
     * @return int[][] secuencias
     */
    private static int[][] getSecuencias2() {
        return new int[][]{
                { 0       } ,
                { 1, 3    } ,
                { 2, 4    } ,
                { 5, 7    } ,
                { 6, 8    } ,
                { 11      } ,
                { 12      } ,
                { 13, 14  } ,
                { 9       } ,
                { 10      }
        };
    }

    /**
     * A cada hilo se le asigna una secuencia de disparo.
     * Se agrega un hilo para cada transición con conflicto
     * Devuelve la secuencia de disparo asignada a cada hilo.
     * @return int[][] secuencias
     */
    private static int[][] getSecuencias3() {
        return new int[][]{
                { 0       } ,
                { 1       } ,
                { 2       } ,
                { 3       } ,
                { 4       } ,
                { 5       } ,
                { 6       } ,
                { 7       } ,
                { 8       } ,
                { 9       } ,
                { 10      } ,
                { 11      } ,
                { 12      } ,
                { 13, 14  }
        };
    }


    //------------------------------------------------------------------------------------------------------//
    //----------------------------- Constantes de PAPER (para pruebas) -------------------------------------//
    //------------------------------------------------------------------------------------------------------//
    /**
     * Marcado inicial de la red del Paper
     */
    public static int[] MI_PAPER = new int[] { 2, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 1, 1, 1 };

    /**
     * Matriz de incidencia negativa Paper
     * Matriz de incidencia de entrada, contiene el peso de los brazos que entran a las transiciones desde las plazas.
     */
    public static int[][] W_MENOS_PAPER = new int[][] {
            // T1 T2 T3 T4 T5 T6 T7 T8 T9 T10
            {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // P1
            {   0, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // P2
            {   0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // P3
            {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, // P4
            {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // P5
            {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // P6
            {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // P7
            {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, // P8
            {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, // P9
            {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // P10
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // P11
            {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, // P12
            {   1, 0, 0, 1, 1, 0, 1, 0, 1, 0 }, // P13
            {   1, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, // P14
    };

    /**
     *  Matriz de incidencia positiva Paper
     * Matriz de incidencia de salida, contiene el peso de los brazos que salen de las transiciones y van hacia las plazas.
     */
    public static int[][] W_MAS_PAPER = new int[][] {
            // T1 T2 T3 T4 T5 T6 T7 T8 T9 T10
            {   0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // P1
            {   1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // P2
            {   0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // P3
            {   0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // P4
            {   0, 0, 0, 1, 1, 0, 0, 0, 0, 0 }, // P5
            {   0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // P6
            {   0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, // P7
            {   0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // P8
            {   0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, // P9
            {   0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, // P10
            {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // P11
            {   0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // P12
            {   0, 1, 1, 0, 0, 1, 0, 1, 0, 1 }, // P13
            {   0, 1, 1, 0, 0, 0, 0, 0, 1, 0 }, // P14
    };
    /**
     * Tiempos (en ms) para cada transicion de la red del Paper (inmediata)
     */
    public static long[][] TIEMPOS_PAPER = {
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
            { 0, MAX_TIME},
    };

    /*      INVARIANTES DE PLAZA PAPER
            1. M(P1) + M(P2) + M(P3) + M(P4) + M(P5) = 2
            2. M(P10) + M(P12) = 1
            3. M(P11) + M(P13) + M(P2) + M(P5) + M(P9) = 1
            4. M(P10) + M(P14) + M(P2) + M(P9) = 1
            5. M(P3) + M(P6) = 1
            6. M(P4) + M(P7) = 1
            7. M(P10) + M(P11) + M(P8) + M(P9) = 2
    */
    /**
     * Invariantes de plaza de la red del Paper
     */
    public static int[][] P_INV_PAPER = new int[][]{
        // P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14  =  X
        {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,         2},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0,         1},
        {0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0,         1},
        {0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1,         1},
        {0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,         1},
        {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,         1},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,         2},
    };

    /**
     * transicion para contar invariantes (T6 y T10 marcan una vuelta)
     */
    public static int[] T_INV_PAPER = new int[]{5,9}; // index respectivo de T6 = 5 y de T10 = 9

    public static int[][] CONFLICTOS_PAPER = new int[][]{
            { 1, 2 }
    };

    /**
     * Expresion regular
     */
    public static String REGEX_PAPER = "((T0)((T1)(.*?)(T3)(.*?)|(T2)(.*?)(T4)(.*?))(T5))|((T6)(T7)(T8)(T9))";

    /**
     * Grupos de reemplazo
     */
    public static String REPLACE_PAPER = "$5$7$9$11";

}
