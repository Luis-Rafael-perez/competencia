package modelo.pago;

/**
 * ABSTRACCIÓN: Interfaz que modela el concepto general de un pago.
 * Cualquier método de pago debe implementar estos métodos.
 */
public interface Pago {
    /**
     * Procesa el pago por el monto indicado.
     * @return true si el pago fue exitoso
     */
    boolean procesarPago(double monto);

    /**
     * Devuelve una descripción del método de pago usado.
     */
    String getDescripcionPago();

    /**
     * Retorna el nombre del método (TARJETA, EFECTIVO, etc.)
     */
    String getMetodo();
}
