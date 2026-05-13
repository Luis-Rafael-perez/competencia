package modelo.carrito;

import modelo.producto.Producto;

/**
 * Representa un producto añadido al carrito con su cantidad.
 */
public class ItemCarrito {

    private Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    // Getters y Setters
    public Producto getProducto() { return producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad +
               " = $" + String.format("%.2f", getSubtotal());
    }
}
