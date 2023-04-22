import Util.*;
import Procesos.*;

public class Main {
    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(100);

        // --------- HILOS ---------
        Thread procesoUnoHiloUno = new Thread(new ProcesoUno(contenedor, 50));
        Thread procesoUnoHiloDos = new Thread(new ProcesoUno(contenedor, 50));

        Thread procesoDosHiloUno = new Thread(new ProcesoDos(contenedor, 100));
        Thread procesoDosHiloDos = new Thread(new ProcesoDos(contenedor, 100));
        Thread procesoDosHiloTres = new Thread(new ProcesoDos(contenedor, 50));

        procesoUnoHiloUno.start();
        procesoUnoHiloDos.start();

        procesoDosHiloUno.start();
        procesoDosHiloDos.start();
        procesoDosHiloTres.start();
        // se puede hacer push

    }
}
