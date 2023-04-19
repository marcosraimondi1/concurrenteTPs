package Util;

public class Imagen {
    private final int id;
    private int improvements = 0;
    private boolean ajustada = false;

    public Imagen(int id) {
        this.id = id;
    }

    public void improve() {
        improvements += 1;
    }

    public boolean isImproved() {
        return improvements == 3;
    }

    public void ajustar() {
        ajustada = true;
    }

    public boolean isAjustada() {
        return ajustada;
    }

    public int getId() {
        return id;
    }
}
