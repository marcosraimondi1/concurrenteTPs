package  Colas;
import java.util.concurrent.Semaphore;

public class Colas {
    private Semaphore[] ColasDeEspera;

    public Colas(){
        ColasDeEspera[5]=new Semaphore(1);//cambiar el 5
    }
    /**
     *
     * @param 
     * @return int[]
    */    public int[] quienesEstan(){
        //array de transiciones con hilos que estan esperando
        return null;
    }

    public Semaphore getCola(int index) {
        return ColasDeEspera[index];
    }

    public void setColasDeEspera(Semaphore[] colasDeEspera) {
        ColasDeEspera = colasDeEspera;
    }

}
