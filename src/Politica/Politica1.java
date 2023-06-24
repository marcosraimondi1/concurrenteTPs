package Politica;

/**
 * Politica1: para cada conflicto, se dispara la transicion que
 * no se haya disparado antes
 */
public class Politica1 implements Politica {
    private boolean c1 = false;     // bandera para conflicto 1
    private boolean c2 = false;     // bandera para conflicto 2
    private boolean c3 = false;     // bandera para conflicto 3

    public int cual(boolean[] transiciones){
        for (int i = 0; i < transiciones.length; i++) {
            if (!transiciones[i])
                continue;

            if (i == 1)
            {
                // en la RdP, T1 esta en conflicto con T2
                c1 = !c1;
                if (c1)
                    return i;
                return i+1;
            }

            if (i == 5)
            {
                c2 = !c2;
                if (c2)
                    return i;
                return i+1;
            }

            if (i == 11)
            {
                c3 = !c3;
                if (c3) {
                    return i;
                }
                return i+1;
            }

            return i;
        }
        throw new RuntimeException("No hay transiciones para disparar");
    }
}
