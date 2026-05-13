package modelo.pago;

/**
 * POLIMORFISMO: implementa la interfaz Pago para pago en efectivo.
 */
public class PagoEfectivo implements Pago {

    private double montoEntregado;
    private double cambio;

    public PagoEfectivo(double montoEntregado) {
        this.montoEntregado = montoEntregado;
    }

    @Override
    public boolean procesarPago(double monto) {
        if (montoEntregado < monto) {
            System.out.println("Efectivo insuficiente. Faltan $" + (monto - montoEntregado));
            return false;
        }
        this.cambio = montoEntregado - monto;
        System.out.println("Pago en efectivo aceptado. Cambio: $" + cambio);
        return true;
    }

    @Override
    public String getDescripcionPago() {
        return "Efectivo entregado: $" + montoEntregado + " | Cambio: $" + cambio;
    }

    @Override
    public String getMetodo() {
        return "EFECTIVO";
    }

    public double getCambio() { return cambio; }
    public double getMontoEntregado() { return montoEntregado; }
}
