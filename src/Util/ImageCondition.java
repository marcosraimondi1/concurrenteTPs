package Util;

/**
 * Interfaz Funcional para crear condiciones a cumplir
 * por una imagen a buscar.
 * Ej de uso:
 * ImageCondition condicion = (imagen) -> { return imagen.isAjustada(); }
 */
public interface ImageCondition {
    boolean verificar(Imagen imagen);
}
