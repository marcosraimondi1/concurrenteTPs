package Cola;
import java.util.concurrent.Semaphore;

public class Cola {
    private final Semaphore cola;

    public Cola(){
        cola = new Semaphore(0,true);
    }
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
    public Semaphore getCola() {
        return cola;
    }
}
