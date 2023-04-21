import Util.*;
import Procesos.*;

import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(100);

        // --------- HILOS ---------
        Thread procesoUnoHiloUno = new Thread(new ProcesoUno(contenedor, 50));
        Thread procesoUnoHiloDos = new Thread(new ProcesoUno(contenedor, 100));

        procesoUnoHiloUno.start();
        procesoUnoHiloDos.start();

    }
}
