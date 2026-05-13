package modelo.pago;

/**
 * POLIMORFISMO: implementa la interfaz Pago para pago con tarjeta.
 */
public class PagoTarjeta implements Pago {

    private String numeroTarjeta; // guardamos solo los últimos 4 dígitos
    private String titular;
    private String fechaExpiracion;
    private String cvv;

    public PagoTarjeta(String numeroTarjeta, String titular,
                       String fechaExpiracion, String cvv) {
        // Solo guardamos últimos 4 dígitos por seguridad
        this.numeroTarjeta = "**** **** **** " +
            numeroTarjeta.substring(numeroTarjeta.length() - 4);
        this.titular = titular;
        this.fechaExpiracion = fechaExpiracion;
        this.cvv = cvv;
    }

    @Override
    public boolean procesarPago(double monto) {
        // Aquí iría la integración con pasarela de pago real
        System.out.println("Procesando pago de $" + monto + " con tarjeta " + numeroTarjeta);
        return true; // simulado
    }

    @Override
    public String getDescripcionPago() {
        return "Tarjeta: " + numeroTarjeta + " | Titular: " + titular;
    }

    @Override
    public String getMetodo() {
        return "TARJETA";
    }

    // Getters
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public String getTitular() { return titular; }
    public String getFechaExpiracion() { return fechaExpiracion; }
}
