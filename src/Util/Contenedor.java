package Util;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Contenedor {
    private final int maxSize;
    private int newImageId = 0;
    private ArrayList<Imagen> contenedor;

    public Contenedor(int maxSize) {
        this.maxSize = maxSize;
        contenedor = new ArrayList<Imagen>();
    }

    /**
     * Busca una imagen
     */
    public Imagen getImage() {
        return contenedor.remove(0);
    }

    public Imagen getImage(int index) {
        return contenedor.remove(index);
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
    public boolean addImage(Imagen imagen) {

        synchronized (this) {
            if (isFull())
                return false;

            imagen.setId(newImageId);
            newImageId++;

            contenedor.add(imagen);
            System.out.printf("\n%s agrego imagen %d", Thread.currentThread().getName(), imagen.getId());
        }
        return true;
    }

    public boolean isEmpty() {
        return contenedor.isEmpty();
    }

    public boolean isFull() {

        return contenedor.size() == maxSize;
    }
}
