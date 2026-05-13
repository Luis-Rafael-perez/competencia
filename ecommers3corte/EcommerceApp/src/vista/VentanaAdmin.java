package vista;

import modelo.producto.*;
import modelo.pedido.Pedido;
import modelo.usuario.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAdmin extends JFrame {

    private AppContext contexto;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloUsuarios;

    public VentanaAdmin(AppContext contexto) {
        this.contexto = contexto;
        construirUI();
        cargarProductos();
        cargarPedidos();
        cargarUsuarios();
    }

    private void construirUI() {
        setTitle("⚙ Panel de Administración — " + contexto.getUsuarioActual().getNombre());
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(33, 47, 61));
        barraTop.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JLabel lblAdmin = new JLabel("⚙ Panel de Administración");
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 15));
        JButton btnCerrar = Estilos.botonRojo("Cerrar sesión");
        barraTop.add(lblAdmin, BorderLayout.WEST);
        barraTop.add(btnCerrar, BorderLayout.EAST);
        add(barraTop, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("📦 Productos",  construirTabProductos());
        tabs.addTab("🧾 Pedidos",    construirTabPedidos());
        tabs.addTab("👥 Usuarios",   construirTabUsuarios());
        add(tabs, BorderLayout.CENTER);

        btnCerrar.addActionListener(e -> {
            dispose();
            new VentanaLogin(contexto.getUsuarioService(), contexto).setVisible(true);
        });
    }

    // ─── TAB PRODUCTOS ───────────────────────────────────────────────────────
    private JPanel construirTabProductos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Nombre", "Tipo", "Precio", "Stock", "Categoría"};
        modeloProductos = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(26);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(40);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        JButton btnAgregar   = Estilos.botonVerde("➕ Agregar producto");
        JButton btnEditar    = Estilos.botonAzul("✏ Editar");
        JButton btnEliminar  = Estilos.botonRojo("🗑 Eliminar");
        JButton btnRefrescar = Estilos.botonGris("🔄 Refrescar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e   -> dialogoAgregarProducto());
        btnEditar.addActionListener(e    -> dialogoEditarProducto());
        btnEliminar.addActionListener(e  -> eliminarProducto());
        btnRefrescar.addActionListener(e -> cargarProductos());
        return panel;
    }

    // ─── TAB PEDIDOS ─────────────────────────────────────────────────────────
    private JPanel construirTabPedidos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"#Pedido", "Cliente ID", "Total", "Método", "Estado", "Fecha"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setRowHeight(26);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        JButton btnActualizar = Estilos.botonAzul("🔄 Cambiar estado");
        JButton btnRefrescar  = Estilos.botonGris("🔄 Refrescar");
        panelBotones.add(btnActualizar);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cambiarEstadoPedido());
        btnRefrescar.addActionListener(e  -> cargarPedidos());
        return panel;
    }

    // ─── TAB USUARIOS ────────────────────────────────────────────────────────
    private JPanel construirTabUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Nombre", "Email", "Rol", "Teléfono / Código"};
        modeloUsuarios = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setRowHeight(26);
        tablaUsuarios.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        JButton btnEditar    = Estilos.botonAzul("✏ Modificar cliente");
        JButton btnEliminar  = Estilos.botonRojo("🗑 Eliminar cliente");
        JButton btnRefrescar = Estilos.botonGris("🔄 Refrescar");
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnEditar.addActionListener(e    -> dialogoEditarUsuario());
        btnEliminar.addActionListener(e  -> eliminarUsuario());
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        return panel;
    }

    // ─── CARGAR DATOS ────────────────────────────────────────────────────────
    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : contexto.getProductoService().getTodosLosProductos()) {
            modeloProductos.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getTipoProducto(),
                String.format("$%,.0f", p.getPrecio()), p.getStock(), p.getCategoria()
            });
        }
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        for (Pedido p : contexto.getPedidoService().getTodosLosPedidos()) {
            modeloPedidos.addRow(new Object[]{
                "#" + p.getId(), p.getIdCliente(),
                String.format("$%,.0f", p.getTotal()),
                p.getMetodoPago().getMetodo(),
                p.getEstado().name(), p.getFechaFormateada()
            });
        }
    }

    private void cargarUsuarios() {
        modeloUsuarios.setRowCount(0);
        for (Usuario u : contexto.getUsuarioService().getTodosLosUsuarios()) {
            String extra = "";
            if (u instanceof Cliente)       extra = ((Cliente) u).getTelefono();
            else if (u instanceof Administrador) extra = ((Administrador) u).getCodigoAdmin();
            modeloUsuarios.addRow(new Object[]{
                u.getId(), u.getNombre(), u.getEmail(), u.getRol(), extra
            });
        }
    }

    // ─── ACCIONES PRODUCTOS ──────────────────────────────────────────────────
    private void dialogoAgregarProducto() {
        JTextField nombre = new JTextField();
        JTextField desc   = new JTextField();
        JTextField precio = new JTextField();
        JTextField stock  = new JTextField();
        JTextField cat    = new JTextField();
        JTextField peso   = new JTextField("0.5");
        JTextField dim    = new JTextField("N/A");

        int r = JOptionPane.showConfirmDialog(this,
            new Object[]{"Nombre:", nombre, "Descripción:", desc, "Precio:", precio,
                         "Stock:", stock, "Categoría:", cat, "Peso (kg):", peso, "Dimensiones:", dim},
            "Agregar producto físico", JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) {
            try {
                contexto.getProductoService().agregarProductoFisico(
                    nombre.getText(), desc.getText(),
                    Double.parseDouble(precio.getText()),
                    Integer.parseInt(stock.getText()),
                    cat.getText(),
                    Double.parseDouble(peso.getText()),
                    dim.getText());
                contexto.getPersistencia().guardarProductos();
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dialogoEditarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        int id = (int) modeloProductos.getValueAt(fila, 0);
        Producto p = contexto.getProductoService().buscarPorId(id);
        if (p == null) return;

        JTextField nombre = new JTextField(p.getNombre());
        JTextField desc   = new JTextField(p.getDescripcion());
        JTextField precio = new JTextField(String.valueOf(p.getPrecio()));
        JTextField stock  = new JTextField(String.valueOf(p.getStock()));

        int r = JOptionPane.showConfirmDialog(this,
            new Object[]{"Nombre:", nombre, "Descripción:", desc, "Precio:", precio, "Stock:", stock},
            "Editar producto", JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) {
            try {
                contexto.getProductoService().modificarProducto(id,
                    nombre.getText(), desc.getText(),
                    Double.parseDouble(precio.getText()),
                    Integer.parseInt(stock.getText()));
                contexto.getPersistencia().guardarProductos();
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        int id = (int) modeloProductos.getValueAt(fila, 0);
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar este producto?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                contexto.getProductoService().eliminarProducto(id);
                contexto.getPersistencia().guardarProductos();
                cargarProductos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─── ACCIONES USUARIOS ───────────────────────────────────────────────────
    private void dialogoEditarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para modificar.");
            return;
        }
        int id  = (int) modeloUsuarios.getValueAt(fila, 0);
        String rol = (String) modeloUsuarios.getValueAt(fila, 3);

        if (!"CLIENTE".equals(rol)) {
            JOptionPane.showMessageDialog(this,
                "Solo se pueden modificar clientes, no administradores.",
                "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = (Cliente) contexto.getUsuarioService().buscarPorId(id);
        if (c == null) return;

        JTextField nombre    = new JTextField(c.getNombre());
        JTextField email     = new JTextField(c.getEmail());
        JTextField direccion = new JTextField(c.getDireccion());
        JTextField telefono  = new JTextField(c.getTelefono());

        int r = JOptionPane.showConfirmDialog(this,
            new Object[]{
                "Nombre:",    nombre,
                "Email:",     email,
                "Dirección:", direccion,
                "Teléfono:",  telefono
            },
            "Modificar cliente #" + id, JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) {
            try {
                contexto.getUsuarioService().modificarCliente(
                    id,
                    nombre.getText(),
                    email.getText(),
                    direccion.getText(),
                    telefono.getText());
                contexto.getPersistencia().guardarUsuarios();
                cargarUsuarios();
                JOptionPane.showMessageDialog(this,
                    "Cliente actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.");
            return;
        }
        int id  = (int) modeloUsuarios.getValueAt(fila, 0);
        String nombre = (String) modeloUsuarios.getValueAt(fila, 1);
        String rol    = (String) modeloUsuarios.getValueAt(fila, 3);

        if (!"CLIENTE".equals(rol)) {
            JOptionPane.showMessageDialog(this,
                "No se puede eliminar un administrador.",
                "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this,
            "¿Seguro que quieres eliminar al cliente \"" + nombre + "\"?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (ok == JOptionPane.YES_OPTION) {
            try {
                contexto.getUsuarioService().eliminarCliente(id);
                contexto.getPersistencia().guardarUsuarios();
                cargarUsuarios();
                JOptionPane.showMessageDialog(this,
                    "Cliente eliminado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─── ACCIONES PEDIDOS ────────────────────────────────────────────────────
    private void cambiarEstadoPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un pedido."); return; }
        int idPedido = Integer.parseInt(
                ((String) modeloPedidos.getValueAt(fila, 0)).replace("#", ""));

        Pedido.EstadoPedido[] estados = Pedido.EstadoPedido.values();
        Pedido.EstadoPedido nuevo = (Pedido.EstadoPedido) JOptionPane.showInputDialog(
                this, "Selecciona el nuevo estado:", "Cambiar estado",
                JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);

        if (nuevo != null) {
            try {
                contexto.getPedidoService().actualizarEstado(idPedido, nuevo);
                contexto.getPersistencia().guardarPedidos();
                cargarPedidos();
                JOptionPane.showMessageDialog(this, "Estado actualizado: " + nuevo.name());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
