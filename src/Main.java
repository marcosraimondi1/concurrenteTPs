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
        Thread.State status[] = new State[10];//declaracion de un arreglo de estados de hilos

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

            // Write the status of the threads
            for (int i = 0; i < 10; i++) {
                pw.println("Main : Status of Thread " + i + " : " + threads[i].getState());
                status[i] = threads[i].getState();
            }

            // Start the ten threads
            for (int i = 0; i < 10; i++) {
                threads[i].start();
            }

            // Wait for the finalization of the threads. We save the status of
            // the threads and only write the status if it changes.
            boolean finish = false;
            while (!finish) {
                for (int i = 0; i < 10; i++) {
                    if (threads[i].getState() != status[i]) {
                        writeThreadInfo(pw, threads[i], status[i]);
                        status[i] = threads[i].getState();
                    }
                }

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
    private static void writeThreadInfo(PrintWriter pw, Thread thread, State state) {
        pw.printf("Main : %s\n", thread.getName());
        pw.printf("Main : Old State: %s\n", state);
        pw.printf("Main : New State: %s\n", thread.getState());
        pw.printf("Main : ************\n");
    }

}



