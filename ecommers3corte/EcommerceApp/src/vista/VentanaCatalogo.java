package vista;

import modelo.excepciones.StockInsuficienteException;
import modelo.producto.Producto;
import modelo.carrito.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCatalogo extends JFrame {

    private AppContext contexto;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JTextField txtBuscar;
    private JSpinner spinCantidad;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal;

    public VentanaCatalogo(AppContext contexto) {
        this.contexto = contexto;
        contexto.getCarritoService().iniciarCarrito(contexto.getUsuarioActual().getId());
        construirUI();
        cargarProductos(null);
    }

    private void construirUI() {
        setTitle("🛒 Tienda — " + contexto.getUsuarioActual().getNombre());
        setSize(960, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barra superior
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(Estilos.AZUL_OSCURO);
        barraTop.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        JLabel lblUsuario = new JLabel("👤 " + contexto.getUsuarioActual().getNombre());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        JButton btnHistorial = Estilos.botonVerde("📋 Mis pedidos");
        JButton btnCerrar    = Estilos.botonRojo("Cerrar sesión");
        JPanel botonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botonesTop.setOpaque(false);
        botonesTop.add(btnHistorial);
        botonesTop.add(btnCerrar);
        barraTop.add(lblUsuario, BorderLayout.WEST);
        barraTop.add(botonesTop, BorderLayout.EAST);

        // Split principal
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(530);

        // ---- CATÁLOGO ----
        JPanel panelCatalogo = new JPanel(new BorderLayout(5, 5));
        panelCatalogo.setBorder(BorderFactory.createTitledBorder("📦 Catálogo de productos"));
        panelCatalogo.setBackground(Estilos.FONDO_APP);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panelBuscar.setBackground(Estilos.FONDO_APP);
        txtBuscar = new JTextField(18);
        JButton btnBuscar = Estilos.botonAzul("Buscar");
        JButton btnTodos  = Estilos.botonGris("Ver todos");
        panelBuscar.add(new JLabel("Buscar:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnTodos);
        panelCatalogo.add(panelBuscar, BorderLayout.NORTH);

        String[] colsCatalogo = {"ID", "Nombre", "Tipo", "Precio", "Stock"};
        modeloCatalogo = new DefaultTableModel(colsCatalogo, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCatalogo = new JTable(modeloCatalogo);
        tablaCatalogo.setRowHeight(26);
        tablaCatalogo.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCatalogo.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        panelAgregar.setBackground(Estilos.FONDO_APP);
        spinCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinCantidad.setPreferredSize(new Dimension(60, 30));
        JButton btnAgregar = Estilos.botonVerde("➕ Agregar al carrito");
        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(spinCantidad);
        panelAgregar.add(btnAgregar);
        panelCatalogo.add(panelAgregar, BorderLayout.SOUTH);

        // ---- CARRITO ----
        JPanel panelCarrito = new JPanel(new BorderLayout(5, 5));
        panelCarrito.setBorder(BorderFactory.createTitledBorder("🧺 Mi carrito"));
        panelCarrito.setBackground(Color.WHITE);

        String[] colsCarrito = {"Producto", "Precio", "Cant.", "Subtotal"};
        modeloCarrito = new DefaultTableModel(colsCarrito, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(26);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCarrito.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        lblTotal = new JLabel("Total: $0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 17));
        lblTotal.setForeground(Estilos.AZUL);
        panelInferior.add(lblTotal, BorderLayout.NORTH);

        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 3, 6, 0));
        panelBotonesCarrito.setBackground(Color.WHITE);
        JButton btnEliminar = Estilos.botonRojo("🗑 Eliminar");
        JButton btnVaciar   = Estilos.botonGris("🧹 Vaciar");
        JButton btnPagar    = Estilos.botonAzul("💳 Pagar");
        panelBotonesCarrito.add(btnEliminar);
        panelBotonesCarrito.add(btnVaciar);
        panelBotonesCarrito.add(btnPagar);
        panelInferior.add(panelBotonesCarrito, BorderLayout.SOUTH);
        panelCarrito.add(panelInferior, BorderLayout.SOUTH);

        split.setLeftComponent(panelCatalogo);
        split.setRightComponent(panelCarrito);

        add(barraTop, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        // Acciones
        btnBuscar.addActionListener(e -> cargarProductos(txtBuscar.getText()));
        btnTodos.addActionListener(e -> { txtBuscar.setText(""); cargarProductos(null); });
        txtBuscar.addActionListener(e -> cargarProductos(txtBuscar.getText()));
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        btnEliminar.addActionListener(e -> eliminarDelCarrito());
        btnVaciar.addActionListener(e -> { contexto.getCarritoService().vaciarCarrito(); actualizarCarrito(); });
        btnPagar.addActionListener(e -> abrirPago());
        btnHistorial.addActionListener(e -> new VentanaHistorial(contexto).setVisible(true));
        btnCerrar.addActionListener(e -> cerrarSesion());
    }

    private void cargarProductos(String filtro) {
        modeloCatalogo.setRowCount(0);
        ArrayList<Producto> lista = (filtro == null || filtro.isEmpty())
                ? contexto.getProductoService().getTodosLosProductos()
                : contexto.getProductoService().buscarPorNombre(filtro);
        for (Producto p : lista) {
            modeloCatalogo.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getTipoProducto(),
                String.format("$%,.0f", p.getPrecio()), p.getStock()
            });
        }
    }

    private void agregarAlCarrito() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto del catálogo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idProducto = (int) modeloCatalogo.getValueAt(fila, 0);
        int cantidad   = (int) spinCantidad.getValue();
        try {
            contexto.getCarritoService().agregarProducto(idProducto, cantidad);
            actualizarCarrito();
        } catch (StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un ítem del carrito.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = (String) modeloCarrito.getValueAt(fila, 0);
        for (ItemCarrito item : contexto.getCarritoService().getItems()) {
            if (item.getProducto().getNombre().equals(nombre)) {
                try {
                    contexto.getCarritoService().eliminarProducto(item.getProducto().getId());
                    actualizarCarrito();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
        }
    }

    void actualizarCarrito() {
        modeloCarrito.setRowCount(0);
        for (ItemCarrito item : contexto.getCarritoService().getItems()) {
            modeloCarrito.addRow(new Object[]{
                item.getProducto().getNombre(),
                String.format("$%,.0f", item.getProducto().getPrecio()),
                item.getCantidad(),
                String.format("$%,.0f", item.getSubtotal())
            });
        }
        lblTotal.setText("Total: $" + String.format("%,.0f", contexto.getCarritoService().calcularTotal()));
    }

    private void abrirPago() {
        if (contexto.getCarritoService().estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.",
                    "Carrito vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new VentanaPago(contexto, this).setVisible(true);
    }

    private void cerrarSesion() {
        contexto.getCarritoService().vaciarCarrito();
        dispose();
        new VentanaLogin(contexto.getUsuarioService(), contexto).setVisible(true);
    }
}
