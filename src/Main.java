import Util.*;
import Procesos.*;

public class Main {
    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(100);
        Contenedor contenedorFinal = new Contenedor(100);

        // --------- HILOS ---------
        // PROCESO UNO
        Thread procesoUnoHiloUno = new Thread(new ProcesoUno(contenedor, 10));
        Thread procesoUnoHiloDos = new Thread(new ProcesoUno(contenedor, 10));

        // PROCESO DOS
        Thread procesoDosHiloUno = new Thread(new ProcesoDos(contenedor, 20));
        Thread procesoDosHiloDos = new Thread(new ProcesoDos(contenedor, 20));
        Thread procesoDosHiloTres = new Thread(new ProcesoDos(contenedor, 20));

        // PROCESO TRES
        Thread procesoTresHiloUno = new Thread(new ProcesoTres(contenedor, 30));
        Thread procesoTresHiloDos = new Thread(new ProcesoTres(contenedor, 30));
        Thread procesoTresHiloTres = new Thread(new ProcesoTres(contenedor, 30));

        // PROCESO CUATRO
        Thread procesoCuatroHiloUno = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));
        Thread procesoCuatroHiloDos = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));

        procesoUnoHiloUno.start();
        procesoUnoHiloDos.start();

        procesoDosHiloUno.start();
        procesoDosHiloDos.start();
        procesoDosHiloTres.start();

        procesoTresHiloUno.start();
        procesoTresHiloDos.start();
        procesoTresHiloTres.start();

        procesoCuatroHiloUno.start();
        procesoCuatroHiloDos.start();

    }
}
