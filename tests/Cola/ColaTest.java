package Cola;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ColaTest {

    @Test
    void hayEsperando() {
        Cola cola = new Cola();
        assertFalse(cola.hayEsperando());
        Thread thread = new Thread(cola::esperar);
        thread.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(cola.hayEsperando());

        cola.sacar();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(cola.hayEsperando());

    }

    @Test
    void esperarySacar() {
        Cola cola = new Cola();
        AtomicBoolean variable = new AtomicBoolean(false);
        Thread thread = new Thread(()->{
            cola.esperar();
            variable.set(true);
        });
        thread.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(variable.get());

        cola.sacar();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertTrue(variable.get());
    }

}