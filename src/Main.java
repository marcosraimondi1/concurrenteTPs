import Util.*;
import Procesos.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

public class Main {

    public static void main(String[] args) {

        // contenedores de imagenes
        Contenedor contenedor = new Contenedor(100);
        Contenedor contenedorFinal = new Contenedor(100);

        // -------------------------------------------------------
        // ------             CREACION DE HILOS             ------
        // -------------------------------------------------------
        Thread[] threads = new Thread[10]; //declaracion de un arreglo de hilos

        // PROCESO UNO
        threads[0] = new Thread(new ProcesoUno(contenedor, 10));
        threads[1] = new Thread(new ProcesoUno(contenedor, 10));

        // PROCESO DOS
        threads[2] = new Thread(new ProcesoDos(contenedor, 20));
        threads[3] = new Thread(new ProcesoDos(contenedor, 20));
        threads[4] = new Thread(new ProcesoDos(contenedor, 20));

        // PROCESO TRES
        threads[5] = new Thread(new ProcesoTres(contenedor, 100));
        threads[6] = new Thread(new ProcesoTres(contenedor, 100));
        threads[7] = new Thread(new ProcesoTres(contenedor, 100));

        // PROCESO CUATRO
        threads[8] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));
        threads[9] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));

        // START THREADS
        for (Thread thread : threads) {
            thread.start();
        }


        // Wait for the finalization of the threads. Meanwhile,
        // write the status of those threads in a file

        try (FileWriter file = new FileWriter(".\\data\\log.txt"); PrintWriter pw = new PrintWriter(file);) {


            // Wait for the finalization of the threads. We save the status of
            // the threads and only write the status if it changes.
            boolean finish = false;

            while (!finish) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                writeThreadInfo(pw, threads, contenedor);

                finish = true;
                for (int i = 0; i < 10; i++) {
                    finish = finish && (threads[i].getState() == State.TERMINATED);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method writes the state of a thread in a file
     *
     * @param pw         PrintWriter to write the data
     * @param threads    Threads whose information will be written
     * @param contenedor Contenedor de imagenes
     */
    private static void writeThreadInfo(PrintWriter pw, Thread[] threads, Contenedor contenedor) {
        pw.printf("Main : ************\n");
        pw.printf("Main :Cantidad de imagenes insertadas: %d\n", contenedor.getContadorCreadas());
        pw.printf("Main :Cantidad de imagenes mejoradas: %d\n", contenedor.getContadorMejoradas());
        pw.printf("Main :Cantidad de imagenes ajustadas: %d\n", contenedor.getContadorAjustadas());
        pw.printf("Main :Cantidad de imagenes copiadas: %d\n", contenedor.getContadorCopiadas());
        for (int i = 0; i < 10; i++) {
            pw.printf("%s", threads[i].getName());
            pw.printf(": New State: %s\n", threads[i].getState());
        }
    }


}



