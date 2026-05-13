package modelo.carrito;

import modelo.producto.Producto;
import modelo.excepciones.StockInsuficienteException;
import java.util.ArrayList;

/**
 * Gestiona los productos seleccionados por el cliente antes de confirmar el pedido.
 * COLECCIONES: usa ArrayList para los ítems.
 * EXCEPCIONES: lanza StockInsuficienteException si no hay stock.
 */
public class Carrito {

    // COLECCIONES: lista de ítems del carrito
    private ArrayList<ItemCarrito> items;
    private int idCliente;

    public Carrito(int idCliente) {
        this.idCliente = idCliente;
        this.items = new ArrayList<>();
    }

    /**
     * Agrega un producto al carrito.
     * MANEJO DE EXCEPCIONES: verifica stock antes de agregar.
     */
    public void agregarProducto(Producto producto, int cantidad)
            throws StockInsuficienteException {
        try {
            if (!producto.hayStock(cantidad)) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para: " + producto.getNombre() +
                    ". Disponible: " + producto.getStock() +
                    ", solicitado: " + cantidad
                );
            }

            // Si ya existe el producto, sumamos cantidad
            for (ItemCarrito item : items) {
                if (item.getProducto().getId() == producto.getId()) {
                    int nuevaCantidad = item.getCantidad() + cantidad;
                    if (!producto.hayStock(nuevaCantidad)) {
                        throw new StockInsuficienteException(
                            "Stock insuficiente al sumar cantidades de: " + producto.getNombre()
                        );
                    }
                    item.setCantidad(nuevaCantidad);
                    return;
                }
            }

            items.add(new ItemCarrito(producto, cantidad));

        } catch (StockInsuficienteException e) {
            throw e; // re-lanzamos para que la UI lo maneje
        }
    }

    /**
     * Elimina un producto del carrito.
     */
    public boolean eliminarProducto(int idProducto) {
        return items.removeIf(item -> item.getProducto().getId() == idProducto);
    }

    /**
     * Modifica la cantidad de un producto ya en el carrito.
     */
    public void modificarCantidad(int idProducto, int nuevaCantidad)
            throws StockInsuficienteException {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == idProducto) {
                if (!item.getProducto().hayStock(nuevaCantidad)) {
                    throw new StockInsuficienteException(
                        "Stock insuficiente: " + item.getProducto().getNombre()
                    );
                }
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
    }

    /**
     * Calcula el total del carrito en tiempo real.
     */
    public double calcularTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    // Getters
    public ArrayList<ItemCarrito> getItems() { return items; }
    public int getIdCliente() { return idCliente; }
    public int getCantidadItems() { return items.size(); }
}
