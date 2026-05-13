package modelo.producto;

/**
 * Clase abstracta que representa un producto del catálogo.
 * ABSTRACCIÓN: define la estructura general de cualquier producto.
 * HERENCIA: ProductoFisico y ProductoDigital la extienden.
 */
public abstract class Producto {

    // ENCAPSULACIÓN: todos los atributos privados
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String categoria;

    public Producto(int id, String nombre, String descripcion,
                    double precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // POLIMORFISMO: cada subclase define su tipo
    public abstract String getTipoProducto();

    // POLIMORFISMO: cada subclase puede personalizar cómo se muestra
    public abstract String getDetalleEspecifico();

    // Método de negocio compartido
    public boolean hayStock(int cantidad) {
        return this.stock >= cantidad;
    }

    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    // Getters y Setters (ENCAPSULACIÓN)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return String.format("[%s] %s — $%.2f (stock: %d)", getTipoProducto(), nombre, precio, stock);
    }
}
