package Cola;

public class Colas {
    private final Cola[] colas;
    private final int cantidad;
    public Colas (int cantidad) {
        this.cantidad = cantidad;
        colas = new Cola[cantidad];
        for (int i = 0; i < cantidad; i++) {
            colas[i] = new Cola();
        }
    }
    public Cola getCola (int index) {
        return colas[index];
    }

    /**
     * Devuelve un array de booleanos que indica si hay hilos esperando en cada cola
     * @return  boolean[]
     */
    public boolean[] hayEsperando(){
        boolean[] colasConEspera = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) {
            colasConEspera[i] = getCola(i).hayEsperando();
        }
        return colasConEspera;
    }
}
