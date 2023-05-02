package Procesos;

import Main.Main;
import Util.*;

public class ProcesoTres extends Proceso {
    private final ImageCondition condicionDeAjuste;
    private int ajustadas = 0;

    public ProcesoTres(Contenedor contenedor, long demora) {
        super(contenedor, demora);
        condicionDeAjuste = imagen -> (!imagen.isAjustada() && imagen.isImproved());
    }

    private boolean ajustar() {
        try {
            // buscar imagen sin ajustar
            Imagen imagen = contenedor.getImage(condicionDeAjuste);

            // ajustamos la imagen
            try {
                Thread.sleep(demora);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            imagen.ajustar();

            // aumentamos contador de ajustadas
            contenedor.aumentarContadorAjustadas();
            ajustadas++;

            if (Main.showPrints[2])
                System.out.printf("\n%s ajusto imagen %d - total: %d", Thread.currentThread().getName(), imagen.getId(), ajustadas);

            // guardar la imagen
            contenedor.addImage(imagen, false);


        } catch (ImagenNoEncontradaException ex) {
            // NO HAY IMAGENES AJUSTABLES

            if (contenedor.getContadorAjustadas() == contenedor.getMaxSize())
                return false;
        }

        return true;
    }

    @Override
    public void run() {
        while (true) {
            if (!ajustar())
                break;
        }

        System.out.printf("\n%s ajusto %d imagenes --------------------", Thread.currentThread().getName(), ajustadas);

    }
}
