package Cola;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColasTest {

    @Test
    void hayEsperando() {
        Colas colas = new Colas(5);
        for (int i = 0; i < 5; i++) {
            assertFalse(colas.hayEsperando()[i]);
        }
        Thread thread = new Thread(colas.getCola(0)::hacerCola);
        thread.start();
        Thread thread1 = new Thread(colas.getCola(1)::hacerCola);
        thread1.start();
        Thread thread2 = new Thread(colas.getCola(4)::hacerCola);
        thread2.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(colas.hayEsperando()[0]);
        assertTrue(colas.hayEsperando()[1]);
        assertFalse(colas.hayEsperando()[2]);
        assertFalse(colas.hayEsperando()[3]);
        assertTrue(colas.hayEsperando()[4]);

        colas.getCola(0).sacar();
        colas.getCola(1).sacar();
        colas.getCola(4).sacar();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 5; i++) {
            assertFalse(colas.hayEsperando()[i]);
        }

    }
}