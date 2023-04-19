import Util.*;

import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(100);
        for (int i = 0; i < 100; i++) {
            Imagen imagen = new Imagen(i);
//            if (i == 10) imagen.ajustar();

            contenedor.addImage(imagen);
        }

        ImageCondition ajustada = Imagen::isAjustada;

        try {
            Imagen image = contenedor.getImage(ajustada);
            System.out.println(image.getId());
        } catch (NoSuchElementException ex) {
            System.out.println("No hay ajustada");
        }
    }
}
