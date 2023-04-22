import Util.*;
import Procesos.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(100);
        Contenedor contenedorFinal = new Contenedor(100);
        Thread threads[] = new Thread[10]; //declaracion de un arreglo de hilos

        // --------- HILOS ---------
        // PROCESO UNO
        Thread procesoUnoHiloUno = new Thread(new ProcesoUno(contenedor, 10));
        Thread procesoUnoHiloDos = new Thread(new ProcesoUno(contenedor, 10));

        // PROCESO DOS
        Thread procesoDosHiloUno = new Thread(new ProcesoDos(contenedor, 20));
        Thread procesoDosHiloDos = new Thread(new ProcesoDos(contenedor, 20));
        Thread procesoDosHiloTres = new Thread(new ProcesoDos(contenedor, 20));

        // PROCESO TRES
        Thread procesoTresHiloUno = new Thread(new ProcesoTres(contenedor, 100));
        Thread procesoTresHiloDos = new Thread(new ProcesoTres(contenedor, 100));
        Thread procesoTresHiloTres = new Thread(new ProcesoTres(contenedor, 100));

        // PROCESO CUATRO
        Thread procesoCuatroHiloUno = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));
        Thread procesoCuatroHiloDos = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));

        threads[0]=procesoUnoHiloUno;
        threads[1]=procesoUnoHiloDos;
        threads[2]=procesoDosHiloUno;
        threads[3]=procesoDosHiloDos;
        threads[4]=procesoDosHiloTres;
        threads[5]=procesoTresHiloUno;
        threads[6]=procesoTresHiloDos;
        threads[7]=procesoTresHiloTres;
        threads[8]=procesoCuatroHiloUno;
        threads[9]=procesoCuatroHiloDos;


        // Wait for the finalization of the threads. Meanwhile,
        // write the status of those threads in a file

        try(FileWriter file = new FileWriter(".\\data\\log.txt"); PrintWriter pw = new PrintWriter(file);) {

            // Start the ten threads
            for (int i = 0; i < 10; i++) {
                threads[i].start();
            }

            // Wait for the finalization of the threads. We save the status of
            // the threads and only write the status if it changes.
            boolean finish = false;
            while (!finish) {
                    try {
                        Thread.sleep(500);
                        } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                        }
                    writeThreadInfo(pw, threads,contenedor);


                finish = true;
                for (int i = 0; i < 10; i++) {
                    finish = finish && (threads[i].getState() == State.TERMINATED);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

       /* procesoUnoHiloUno.start();
        procesoUnoHiloDos.start();

        procesoDosHiloUno.start();
        procesoDosHiloDos.start();
        procesoDosHiloTres.start();

        procesoTresHiloUno.start();
        procesoTresHiloDos.start();
        procesoTresHiloTres.start();

        procesoCuatroHiloUno.start();
        procesoCuatroHiloDos.start();*/

    }
    /**
     * This method writes the state of a thread in a file
     *
     * @param pw
     *            : PrintWriter to write the data
     * @param thread
     *            : Thread whose information will be written
     * @param state
     *            : Old state of the thread
     */
    private static void writeThreadInfo(PrintWriter pw, Thread threads[],Contenedor contenedor) {
        pw.printf("Main : ************\n");
        pw.printf("Main :Cantidad de imagenes insertadas: %d\n",contenedor.getContadorCreadas());
        pw.printf("Main :Cantidad de imagenes mejoradas: %d\n",contenedor.getContadorMejoradas());
        pw.printf("Main :Cantidad de imagenes ajustadas: %d\n",contenedor.getContadorAjustadas());
        pw.printf("Main :Cantidad de imagenes copiadas: %d\n",contenedor.getContadorCopiadas());
        for (int i = 0; i < 10; i++) {
            pw.printf("Main : %s", threads[i].getName());
            pw.printf(": New State: %s\n", threads[i].getState());
        }
    }






}



