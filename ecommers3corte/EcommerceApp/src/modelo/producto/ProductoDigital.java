package modelo.producto;

/**
 * HERENCIA: ProductoDigital extiende Producto.
 * No tiene stock físico limitado — se puede vender infinitas veces.
 */
public class ProductoDigital extends Producto {

    private String urlDescarga;
    private String formatoArchivo; // PDF, MP3, ZIP, etc.
    private double tamanoMB;

    public ProductoDigital(int id, String nombre, String descripcion,
                           double precio, String categoria,
                           String urlDescarga, String formatoArchivo, double tamanoMB) {
        // Stock "ilimitado" para productos digitales
        super(id, nombre, descripcion, precio, Integer.MAX_VALUE, categoria);
        this.urlDescarga = urlDescarga;
        this.formatoArchivo = formatoArchivo;
        this.tamanoMB = tamanoMB;
    }

    @Override
    public String getTipoProducto() {
        return "DIGITAL";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Formato: " + formatoArchivo + " | Tamaño: " + tamanoMB + " MB";
    }

    // El stock de digitales no se reduce
    @Override
    public void reducirStock(int cantidad) {
        // No hace nada — producto digital es ilimitado
    }

    public String getUrlDescarga() { return urlDescarga; }
    public void setUrlDescarga(String urlDescarga) { this.urlDescarga = urlDescarga; }

    public String getFormatoArchivo() { return formatoArchivo; }
    public void setFormatoArchivo(String formatoArchivo) { this.formatoArchivo = formatoArchivo; }

    public double getTamanoMB() { return tamanoMB; }
    public void setTamanoMB(double tamanoMB) { this.tamanoMB = tamanoMB; }
}
