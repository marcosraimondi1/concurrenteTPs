package Util;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Contenedor {
    private final int maxSize;
    private final ArrayList<Imagen> contenedor;
    private int contadorCreadas = 0;

    public Contenedor(int maxSize) {
        this.maxSize = maxSize;
        contenedor = new ArrayList<>();
    }

    /**
     * Busca una imagen
     */
    public Imagen getImage() {
        return contenedor.remove(0);
    }

    /**
     * Busca una imagen que satisfaga la condicion dada
     *
     * @param condicion Condicion a satisfacer por la imagen buscada
     * @return Imagen que satisface la condicion
     */
    public Imagen getImage(ImageCondition condicion) throws NoSuchElementException {
        synchronized (this) {
            Imagen image = contenedor.stream().filter(imagen -> condicion.verificar(imagen)).findFirst().get();
            contenedor.remove(image);
            return image;
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
                contadorCreadas++;
                System.out.printf("\n%s agrego imagen %d", Thread.currentThread().getName(), imagen.getId());
            }

            contenedor.add(imagen);
        }
        return true;
    }

    public boolean isEmpty() {
        return contenedor.isEmpty();
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
}
