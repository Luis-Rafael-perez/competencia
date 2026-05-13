import servicio.*;
import persistencia.*;
import vista.*;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 * Inicializa servicios, carga datos persistidos y lanza la interfaz gráfica.
 */
public class Main {

    public static void main(String[] args) {
        // Mejorar apariencia en el sistema operativo
        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Usar look and feel por defecto si falla
        }

        // Inicializar servicios
        UsuarioService usuarioService   = new UsuarioService();
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService     = new PedidoService();
        CarritoService carritoService   = new CarritoService(productoService);

        // Inicializar persistencia y cargar datos guardados
        GestorPersistencia persistencia = new GestorPersistencia(
                usuarioService, productoService, pedidoService);
        persistencia.cargarTodo();

        // Crear contexto compartido entre ventanas
        AppContext contexto = new AppContext(
                usuarioService, productoService,
                pedidoService, carritoService, persistencia);

        // Lanzar interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin(usuarioService, contexto).setVisible(true);
        });
    }
}
