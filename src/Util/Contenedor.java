package Util;

import Main.Main;

import java.util.ArrayList;
import java.util.Optional;

public class Contenedor {
    private final int maxSize;
    private final ArrayList<Imagen> contenedor;

    // KEYS ---------------------------------------
    private final Object keyMejoradas = new Object();
    private final Object keyAjuste = new Object();
    private final Object keyCopia = new Object();

    // CONTADORES ---------------------------------
    private int contadorCreadas = 0;
    private int contadorMejoradas = 0;
    private int contadorAjustadas = 0;
    private int contadorCopiadas = 0;

    public Contenedor(int maxSize) {
        this.maxSize = maxSize;
        contenedor = new ArrayList<>();
    }

    /**
     * Busca y remueve del contenedor una imagen que satisfaga la condicion dada
     *
     * @param condicion Condicion a satisfacer por la imagen buscada
     * @return Imagen que satisface la condicion
     */
    public Imagen getImage(ImageCondition condicion) throws ImagenNoEncontradaException {
        synchronized (this) {
            Optional<Imagen> imageOpt = contenedor.stream().filter(imagen -> condicion.verificar(imagen)).findFirst();
            if (imageOpt.isPresent()) {
                Imagen imagen = imageOpt.get();
                contenedor.remove(imagen);
                return imagen;
            }
            throw new ImagenNoEncontradaException(Thread.currentThread().getName() + " no pudo conseguir");
        }
    }


    /**
     * Agrega una imagen, si el contenedor no esta lleno
     *
     * @param imagen Imagen a agregar
     * @return false si no pudo agregar la imagen, true c.c.
     */
    public boolean addImage(Imagen imagen, boolean nueva) {

        synchronized (this) {
            if (nueva) {
                if (isFull())
                    return false;

                imagen.setId(contadorCreadas);
                aumentarContadorCreadas();

                if (Main.showPrints[0])
                    System.out.printf("\n%s agrego imagen %d", Thread.currentThread().getName(), imagen.getId());
            }

            contenedor.add(imagen);
        }
        return true;
    }

    /**
     * Determina si ya se han creado todas las imagenes
     * que pueden entrar al contenedor, independientemente
     * si se removieron despues para mejorarlas, ajustarlas, etc
     *
     * @return boolean
     */
    public boolean isFull() {
        return contadorCreadas == maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getContadorCreadas() {
        return contadorCreadas;
    }

    public void aumentarContadorCreadas() {
        contadorCreadas++;
    }

    public int getContadorMejoradas() {
        synchronized (keyMejoradas) {
            return contadorMejoradas;
        }
    }

    public void aumentarContadorMejoradas() {
        synchronized (keyMejoradas) {
            contadorMejoradas++;
        }
    }

    public int getContadorAjustadas() {
        synchronized (keyAjuste) {
            return contadorAjustadas;
        }
    }

    public void aumentarContadorAjustadas() {
        synchronized (keyAjuste) {
            contadorAjustadas++;
        }
    }

    public int getContadorCopiadas() {
        synchronized (keyCopia) {
            return contadorCopiadas;
        }
    }

    public void aumentarContadorCopiadas() {
        synchronized (keyCopia) {
            contadorCopiadas++;
        }
    }

}
