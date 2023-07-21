package RdP;

import org.junit.jupiter.api.Test;

import static Constants.Constants.MAX_TIME;
import static org.junit.jupiter.api.Assertions.*;

class SensibilizadoConTiempoTest {

    @Test
    void testVentana() {
        long[][] tiempos = new long[][]{
                // alfa ,  beta [ms]
                {   0   ,   MAX_TIME}, // T0
                {   500 ,   MAX_TIME}  // T1
        };

        SensibilizadoConTiempo sensibilizadoConTiempo = new SensibilizadoConTiempo(tiempos);

        // T0
        boolean ventana = sensibilizadoConTiempo.testVentana(0);
        assertFalse(ventana);

        sensibilizadoConTiempo.setTimeStamp(0);
        ventana = sensibilizadoConTiempo.testVentana(0);
        assertTrue(ventana);

        // T1
        sensibilizadoConTiempo.setTimeStamp(1);
        ventana = sensibilizadoConTiempo.testVentana(1);
        assertFalse(ventana);


        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ventana = sensibilizadoConTiempo.testVentana(1);
        assertFalse(ventana);

        long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(1);

        try {
            Thread.sleep(tiempoRestante);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ventana = sensibilizadoConTiempo.testVentana(1);

        assertTrue(ventana);

    }

    @Test
    void antesDeLaVentana() {
        long[][] tiempos = new long[][]{
                // alfa ,  beta [ms]
                {   0   ,   MAX_TIME}, // T0
                {   500 ,   MAX_TIME}  // T1
        };

        SensibilizadoConTiempo sensibilizadoConTiempo = new SensibilizadoConTiempo(tiempos);

        boolean antes = sensibilizadoConTiempo.antesDeLaVentana(0);
        assertFalse(antes);

        sensibilizadoConTiempo.setTimeStamp(0);
        antes = sensibilizadoConTiempo.antesDeLaVentana(0);
        assertFalse(antes); // va a estar en la ventana

        sensibilizadoConTiempo.setTimeStamp(1);
        antes = sensibilizadoConTiempo.antesDeLaVentana(1);
        assertTrue(antes);

        long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(1);

        try {
            Thread.sleep(tiempoRestante + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        antes = sensibilizadoConTiempo.antesDeLaVentana(1);
        assertFalse(antes);
    }

    @Test
    void getTiempoRestante() {
        long[][] tiempos = new long[][]{
                // alfa ,  beta [ms]
                {   0   ,   MAX_TIME}, // T0
                {   500 ,   MAX_TIME}  // T1
        };

        SensibilizadoConTiempo sensibilizadoConTiempo = new SensibilizadoConTiempo(tiempos);
        long tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(0);
        assertEquals(MAX_TIME-System.currentTimeMillis(), tiempoRestante);

        sensibilizadoConTiempo.setTimeStamp(0);
        tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(0);
        assertEquals(0,tiempoRestante);

        sensibilizadoConTiempo.setTimeStamp(1);
        tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(1);
        assertEquals(500,tiempoRestante);

        try {
            Thread.sleep(tiempoRestante - 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(1);

        assertTrue(tiempoRestante <= 100 );

        try {
            Thread.sleep(tiempoRestante);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tiempoRestante = sensibilizadoConTiempo.getTiempoRestante(1);
        assertEquals(0,tiempoRestante);


    }
}