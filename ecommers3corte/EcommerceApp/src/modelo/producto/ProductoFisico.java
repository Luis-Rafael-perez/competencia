package modelo.producto;

/**
 * HERENCIA: ProductoFisico extiende Producto.
 * Tiene peso y dimensiones para calcular envío.
 */
public class ProductoFisico extends Producto {

    private double pesoKg;
    private String dimensiones; // "LxAxA en cm"

    public ProductoFisico(int id, String nombre, String descripcion,
                          double precio, int stock, String categoria,
                          double pesoKg, String dimensiones) {
        super(id, nombre, descripcion, precio, stock, categoria);
        this.pesoKg = pesoKg;
        this.dimensiones = dimensiones;
    }

    @Override
    public String getTipoProducto() {
        return "FÍSICO";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Peso: " + pesoKg + " kg | Dimensiones: " + dimensiones;
    }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
}
