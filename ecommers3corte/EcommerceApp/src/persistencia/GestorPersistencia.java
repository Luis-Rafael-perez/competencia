package persistencia;

import servicio.*;
import java.util.ArrayList;

/**
 * Punto central de persistencia.
 * Conecta los servicios con los repositorios de archivo.
 * Se llama al iniciar y cerrar la aplicación.
 */
public class GestorPersistencia {

    private UsuarioService usuarioService;
    private ProductoService productoService;
    private PedidoService pedidoService;

    public GestorPersistencia(UsuarioService usuarioService,
                               ProductoService productoService,
                               PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    /**
     * Carga todos los datos al iniciar la aplicación.
     * Si los archivos no existen, usa los datos de prueba de los servicios.
     */
    public void cargarTodo() {
        try {
            GestorArchivos.inicializar();

            // Cargar usuarios
            if (GestorArchivos.existe("usuarios.csv")) {
                var usuarios = UsuarioRepositorio.cargar();
                if (!usuarios.isEmpty()) {
                    usuarioService.setUsuarios(usuarios);
                    System.out.println("✓ Usuarios cargados desde archivo.");
                }
            }

            // Cargar productos
            if (GestorArchivos.existe("productos.csv")) {
                var productos = ProductoRepositorio.cargar();
                if (!productos.isEmpty()) {
                    productoService.setProductos(productos);
                    System.out.println("✓ Productos cargados desde archivo.");
                }
            }

            // Cargar pedidos
            if (GestorArchivos.existe("pedidos.csv")) {
                var pedidos = PedidoRepositorio.cargar(null);
                if (!pedidos.isEmpty()) {
                    pedidoService.setPedidos(pedidos);
                    System.out.println("✓ Pedidos cargados desde archivo.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
        }
    }

    /**
     * Guarda todos los datos.
     * Llamar cuando se modifica algo o al cerrar la app.
     */
    public void guardarTodo() {
        try {
            UsuarioRepositorio.guardar(usuarioService.getTodosLosUsuarios());
            ProductoRepositorio.guardar(productoService.getTodosLosProductos());
            PedidoRepositorio.guardar(pedidoService.getTodosLosPedidos());
            System.out.println("✓ Datos guardados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    /**
     * Guarda solo los usuarios (después de un registro o modificación).
     */
    public void guardarUsuarios() {
        UsuarioRepositorio.guardar(usuarioService.getTodosLosUsuarios());
    }

    /**
     * Guarda solo los productos (después de agregar, editar o eliminar).
     */
    public void guardarProductos() {
        ProductoRepositorio.guardar(productoService.getTodosLosProductos());
    }

    /**
     * Guarda solo los pedidos (después de confirmar un pedido).
     */
    public void guardarPedidos() {
        PedidoRepositorio.guardar(pedidoService.getTodosLosPedidos());
    }
}
