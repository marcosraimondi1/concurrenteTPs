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

    /**
     * Busca una imagen que satisfaga la condicion dada
     *
     * @param condicion Condicion a satisfacer por la imagen buscada
     * @return Imagen que satisface la condicion
     */
    public Imagen getImage(ImageCondition condicion) {
        return contenedor.stream().map(imagen -> condicion.verificar(imagen)).findFirst().get();
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
