package Util;

public class Imagen {
    private int id = -1;
    private int improvements = 0;
    private boolean ajustada = false;

    public Imagen() {
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

    public void setId(int id) {
        if (this.id == -1)
            this.id = id;
    }
}
