package Procesos;

import Util.Contenedor;
import Util.ImageCondition;
import Util.Imagen;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ProcesoDos extends Proceso {
    private final ArrayList<Integer> mejoradas; // arreglo con los ids de las imagenes mejoradas

    private final ImageCondition condicionDeMejora;

    public ProcesoDos(Contenedor contenedor, long demora) {
        super(contenedor, demora);
        mejoradas = new ArrayList<>();

        condicionDeMejora = imagen -> {
            if (imagen.isImproved())
                return false;

            if (mejoradas.contains(imagen.getId()))
                return false;

            return true;
        };
        /*
         public boolean nombreFuncion (Imagen imagen) {
            if (imagen.isImproved())
                return false;

            if (mejoradas.contains(imagen.getId()))
                return false;

            return true;
         }
         */
    }

    /**
     * Busca una imagen para mejorar simepre y cuando no la haya mejorado antes
     * y la imagen no tenga 3 mejoras acumuladas
     *
     * @return false si termino con todas las imagenes
     */
    private boolean mejorar() {

        try {
            // toma la imagen
            Imagen imagen = contenedor.getImage(condicionDeMejora);

            // la mejora
            try {
                Thread.sleep(demora);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // agrego mejora a la imagen
            imagen.improve();

            //Añado a lista de visitadas por el Thread
            mejoradas.add(imagen.getId());

            System.out.printf("\n%s mejoro imagen %d - total: %d", Thread.currentThread().getName(), imagen.getId(), mejoradas.size());

            // la guarda
            contenedor.addImage(imagen, false);

        } catch (NoSuchElementException ex) {
            // NO ENCONTRO IMAGENES PARA MEJORAR

            // Si ya mejoro todas las imagenes del contenedor, se detiene
            if (mejoradas.size() == contenedor.getMaxSize())
                return false;


//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }


        return true;
    }

    @Override
    public void run() {
        while (true) {
            if (!mejorar())
                break;
        }
        System.out.printf("\n%s mejoro %d imagenes - finalizo", Thread.currentThread().getName(), mejoradas.size());
    }
}
