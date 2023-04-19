/**
 * Interfaz Funcional para crear condiciones a cumplir
 * por una imagen a buscar.
 * Ej de uso:
 * ImageCondition condicion = (Imagen imagen) -> { if (imagen.isAjustada()) return imagen; }
 */
public interface ImageCondition {
    Imagen verificar(Imagen imagen);
}
