package modelo.excepciones;

/**
 * MANEJO DE EXCEPCIONES: excepción personalizada para stock insuficiente.
 * Se lanza cuando se intenta agregar al carrito más unidades de las disponibles.
 */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
