package servicio;

import modelo.carrito.Carrito;
import modelo.carrito.ItemCarrito;
import modelo.excepciones.StockInsuficienteException;
import modelo.producto.Producto;

/**
 * Gestiona el carrito de compras del cliente activo.
 * Intermediario entre la vista y el modelo Carrito.
 */
public class CarritoService {

    private Carrito carritoActual;
    private ProductoService productoService;

    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Inicia un nuevo carrito para el cliente que acaba de iniciar sesión.
     */
    public void iniciarCarrito(int idCliente) {
        this.carritoActual = new Carrito(idCliente);
    }

    /**
     * Agrega un producto al carrito verificando stock.
     * MANEJO DE EXCEPCIONES: captura StockInsuficienteException.
     */
    public void agregarProducto(int idProducto, int cantidad)
            throws StockInsuficienteException, Exception {
        try {
            if (carritoActual == null)
                throw new Exception("No hay un carrito activo. Inicia sesión primero.");

            Producto producto = productoService.buscarPorId(idProducto);
            if (producto == null)
                throw new Exception("Producto no encontrado con ID: " + idProducto);

            carritoActual.agregarProducto(producto, cantidad);

        } catch (StockInsuficienteException e) {
            throw e; // la vista mostrará el mensaje al usuario
        }
    }

    /**
     * Elimina un producto del carrito.
     */
    public boolean eliminarProducto(int idProducto) throws Exception {
        if (carritoActual == null)
            throw new Exception("No hay un carrito activo.");
        return carritoActual.eliminarProducto(idProducto);
    }

    /**
     * Modifica la cantidad de un ítem en el carrito.
     */
    public void modificarCantidad(int idProducto, int nuevaCantidad)
            throws StockInsuficienteException, Exception {
        if (carritoActual == null)
            throw new Exception("No hay un carrito activo.");
        if (nuevaCantidad <= 0) {
            eliminarProducto(idProducto);
            return;
        }
        carritoActual.modificarCantidad(idProducto, nuevaCantidad);
    }

    /**
     * Retorna el total del carrito en tiempo real.
     */
    public double calcularTotal() {
        if (carritoActual == null) return 0;
        return carritoActual.calcularTotal();
    }

    public void vaciarCarrito() {
        if (carritoActual != null) carritoActual.vaciar();
    }

    public boolean estaVacio() {
        return carritoActual == null || carritoActual.estaVacio();
    }

    public Carrito getCarritoActual() {
        return carritoActual;
    }

    public java.util.ArrayList<ItemCarrito> getItems() {
        if (carritoActual == null) return new java.util.ArrayList<>();
        return carritoActual.getItems();
    }
}
