package vista;

import modelo.usuario.Usuario;
import persistencia.GestorPersistencia;
import servicio.*;

/**
 * Contexto compartido entre todas las ventanas de la aplicación.
 * Evita pasar todos los servicios como parámetros a cada ventana.
 */
public class AppContext {

    private Usuario usuarioActual;
    private UsuarioService usuarioService;
    private ProductoService productoService;
    private PedidoService pedidoService;
    private CarritoService carritoService;
    private GestorPersistencia persistencia;

    public AppContext(UsuarioService usuarioService,
                      ProductoService productoService,
                      PedidoService pedidoService,
                      CarritoService carritoService,
                      GestorPersistencia persistencia) {
        this.usuarioService  = usuarioService;
        this.productoService = productoService;
        this.pedidoService   = pedidoService;
        this.carritoService  = carritoService;
        this.persistencia    = persistencia;
    }

    // Getters y Setters
    public Usuario getUsuarioActual() { return usuarioActual; }
    public void setUsuarioActual(Usuario u) { this.usuarioActual = u; }

    public UsuarioService getUsuarioService()   { return usuarioService; }
    public ProductoService getProductoService() { return productoService; }
    public PedidoService getPedidoService()     { return pedidoService; }
    public CarritoService getCarritoService()   { return carritoService; }
    public GestorPersistencia getPersistencia() { return persistencia; }
}
