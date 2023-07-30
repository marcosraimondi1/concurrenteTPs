package Cola;
import java.util.concurrent.Semaphore;

public class Cola {
    private final Semaphore cola;

    /**
     * Cola es implementada a traves de Semaphore
     */
    public Cola(){
        cola = new Semaphore(0,true);
    }

    /**
     * Devuelve true si hay algun hilo esperando en la cola, o false si no lo hay
     * @return boolean .
     */
    public boolean hayEsperando(){
        return cola.hasQueuedThreads();
    }
    public void sacar(){
        if (cola.availablePermits() != 0)
            throw new RuntimeException("Se corrompio la cola");
        cola.release();
    }
    public void esperar(){
        try {
            cola.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCantidadEsperando() {
        return cola.getQueueLength();
    }
}
