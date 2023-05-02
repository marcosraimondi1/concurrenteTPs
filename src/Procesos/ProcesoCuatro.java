package Procesos;

import Main.Main;
import Util.Contenedor;
import Util.ImageCondition;
import Util.Imagen;
import Util.ImagenNoEncontradaException;

public class ProcesoCuatro extends Proceso {
    private final Contenedor contenedorFinal;
    private final ImageCondition condicionDeCopia;
    private int copiadas = 0;

    public ProcesoCuatro(Contenedor contenedor, Contenedor contenedorFinal, long demora) {
        super(contenedor, demora);
        this.contenedorFinal = contenedorFinal;
        condicionDeCopia = imagen -> (imagen.isAjustada() && imagen.isImproved());
    }

    private boolean copiar() {
        try {

            // buscar imagen sin ajustar
            Imagen imagen = contenedor.getImage(condicionDeCopia);

            try {
                Thread.sleep(demora);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // agregamos imagen al otro contenedor
            contenedorFinal.addImage(imagen, false);

            // aumentamos contadores de copia
            copiadas++;
            contenedor.aumentarContadorCopiadas();

            if (Main.showPrints[3])
                System.out.printf("\n%s copio imagen %d - total: %d", Thread.currentThread().getName(), imagen.getId(), copiadas);


        } catch (ImagenNoEncontradaException ex) {
            // NO HAY IMAGENES COPIABLES

            if (contenedor.getContadorCopiadas() == contenedor.getMaxSize())
                return false;
        }

        return true;
    }

    @Override
    public void run() {
        while (true) {
            if (!copiar())
                break;
        }

        System.out.printf("\nHilo %s copio %d imagenes --------------------", Thread.currentThread().getName(), copiadas);

    }
}
