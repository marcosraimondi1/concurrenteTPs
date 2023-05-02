package Main;

import Util.*;
import Procesos.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

public class Main {
    public static final boolean[] showPrints = {true, false, false, false};
    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        // contenedores de imagenes
        int maxSize = 100;
        Contenedor contenedor = new Contenedor(maxSize);
        Contenedor contenedorFinal = new Contenedor(maxSize);

        // -------------------------------------------------------
        // ------             CREACION DE HILOS             ------
        // -------------------------------------------------------
        Thread[] threads = new Thread[10];

        // PROCESO UNO - CARGA
        long demoraUno = 110;
        threads[0] = new Thread(new ProcesoUno(contenedor, demoraUno));
        threads[1] = new Thread(new ProcesoUno(contenedor, demoraUno));
        threads[0].setName(threads[0].getName() + " P1");
        threads[1].setName(threads[1].getName() + " P1");

        // PROCESO DOS - MEJORAS
        long demoraDos = 100;
        threads[2] = new Thread(new ProcesoDos(contenedor, demoraDos));
        threads[3] = new Thread(new ProcesoDos(contenedor, demoraDos));
        threads[4] = new Thread(new ProcesoDos(contenedor, demoraDos));
        threads[2].setName(threads[2].getName() + " P2");
        threads[3].setName(threads[3].getName() + " P2");
        threads[4].setName(threads[4].getName() + " P2");

        // PROCESO TRES - AJUSTES
        long demoraTres = 200;
        threads[5] = new Thread(new ProcesoTres(contenedor, demoraTres));
        threads[6] = new Thread(new ProcesoTres(contenedor, demoraTres));
        threads[7] = new Thread(new ProcesoTres(contenedor, demoraTres));
        threads[5].setName(threads[5].getName() + " P3");
        threads[6].setName(threads[6].getName() + " P3");
        threads[7].setName(threads[7].getName() + " P3");

        // PROCESO CUATRO - COPIA
        long demoraCuatro = 100;
        threads[8] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, demoraCuatro));
        threads[9] = new Thread(new ProcesoCuatro(contenedor, contenedorFinal, demoraCuatro));
        threads[8].setName(threads[8].getName() + " P4");
        threads[9].setName(threads[9].getName() + " P4");

        // -------------------------------------------------------
        // ------             START THREADS                 ------
        // -------------------------------------------------------

        for (Thread thread : threads) {
            thread.start();
        }


//    Para que se ejecuten secuencialmente cada proceso
/*
        try {
            threads[0].start();
            threads[1].start();
            threads[0].join();
            threads[1].join();

            threads[2].start();
            threads[3].start();
            threads[4].start();
            threads[2].join();
            threads[3].join();
            threads[4].join();

            threads[5].start();
            threads[6].start();
            threads[7].start();
            threads[5].join();
            threads[6].join();
            threads[7].join();

            threads[8].start();
            threads[9].start();
            threads[8].join();
            threads[9].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
*/
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



