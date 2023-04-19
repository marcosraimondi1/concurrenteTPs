import java.util.ArrayList;

public class Contenedor {
    private final int maxSize;
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

    public void addImage(Imagen imagen) {
        contenedor.add(imagen);
    }

    public boolean isEmpty() {
        return contenedor.isEmpty();
    }

    public boolean isFull() {
        return contenedor.size() == maxSize;
    }
}
