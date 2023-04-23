package Main;

import Util.*;
import Procesos.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

public class Main {
    public static final boolean[] showPrints = {true, true, true, true};
    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        // contenedores de imagenes
        Contenedor contenedor = new Contenedor(100);
        Contenedor contenedorFinal = new Contenedor(100);

        // -------------------------------------------------------
        // ------             CREACION DE HILOS             ------
        // -------------------------------------------------------
        Thread[] threads = new Thread[10]; //declaracion de un arreglo de hilos

        // PROCESO UNO - CARGA
        threads[0] = new Thread(new ProcesoUno(contenedor, 110));
        threads[1] = new Thread(new ProcesoUno(contenedor, 110));

        // PROCESO DOS - MEJORAS
        threads[2] = new Thread(new ProcesoDos(contenedor, 100));
        threads[3] = new Thread(new ProcesoDos(contenedor, 100));
        threads[4] = new Thread(new ProcesoDos(contenedor, 100));

        // PROCESO TRES - AJUSTES
        threads[5] = new Thread(new ProcesoTres(contenedor, 200));
        threads[6] = new Thread(new ProcesoTres(contenedor, 200));
        threads[7] = new Thread(new ProcesoTres(contenedor, 200));

        // PROCESO CUATRO - COPIA
        threads[8] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));
        threads[9] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, 100));


        // -------------------------------------------------------
        // ------             START THREADS                 ------
        // -------------------------------------------------------

        for (Thread thread : threads) {
            thread.start();
        }

        // -------------------------------------------------------
        // ------           START STATUS PRINTER            ------
        // -------------------------------------------------------

        startStatusPrinter(contenedor, threads);
    }

    private static void startStatusPrinter(Contenedor contenedor, Thread[] threads) {
        // Wait for the finalization of the threads. Meanwhile,
        // write the status of those threads in a file

        try (FileWriter file = new FileWriter(".\\data\\log.txt"); PrintWriter pw = new PrintWriter(file)) {

            boolean finish = false;

            while (!finish) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                writeThreadInfo(pw, threads, contenedor);

                finish = true;

                for (Thread thread : threads) {
                    finish = finish && (thread.getState() == State.TERMINATED);
                }
            }

            pw.printf("\n\nTOTAL RUN TIME: %.2f [s]\n\n", (double) (System.currentTimeMillis() - startTime) / 1000);
            System.out.printf("\n\nTOTAL RUN TIME: %.2f [s]\n\n", (double) (System.currentTimeMillis() - startTime) / 1000);

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
        pw.printf("---------------------------------------------\n");

        pw.printf("\nCONTAINER STATUS: \n\n");

        pw.printf("\t- Cantidad de imagenes insertadas: \t%d\n", contenedor.getContadorCreadas());
        pw.printf("\t- Cantidad de imagenes mejoradas: \t%d\n", contenedor.getContadorMejoradas());
        pw.printf("\t- Cantidad de imagenes ajustadas: \t%d\n", contenedor.getContadorAjustadas());
        pw.printf("\t- Cantidad de imagenes copiadas: \t%d\n", contenedor.getContadorCopiadas());

        pw.printf("\nTHREADS STATUS: \n\n");

        for (Thread thread : threads) {
            pw.printf("\t- %s  State: \t%s\n", thread.getName(), thread.getState());
        }

        pw.printf("\ntime since start: %d [ms]\n\n", System.currentTimeMillis() - startTime);

    }


}



