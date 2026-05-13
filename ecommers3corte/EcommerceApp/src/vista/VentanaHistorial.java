package vista;

import modelo.pedido.Pedido;
import modelo.carrito.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaHistorial extends JFrame {

    private AppContext contexto;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JTextArea txtDetalle;

    public VentanaHistorial(AppContext contexto) {
        this.contexto = contexto;
        construirUI();
        cargarHistorial();
    }

    private void construirUI() {
        setTitle("📋 Mis pedidos");
        setSize(650, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(220);

        String[] cols = {"#Pedido", "Fecha", "Total", "Método", "Estado"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setRowHeight(26);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Pedidos realizados"));
        panelSuperior.add(new JScrollPane(tablaPedidos));

        txtDetalle = new JTextArea("Selecciona un pedido para ver el detalle.");
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Detalle del pedido"));
        panelInferior.add(new JScrollPane(txtDetalle));

        split.setTopComponent(panelSuperior);
        split.setBottomComponent(panelInferior);
        add(split);

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalle();
        });
    }

    private void cargarHistorial() {
        modeloPedidos.setRowCount(0);
        ArrayList<Pedido> historial = contexto.getPedidoService()
                .getHistorialCliente(contexto.getUsuarioActual().getId());
        if (historial.isEmpty()) {
            txtDetalle.setText("Aún no tienes pedidos realizados.");
            return;
        }
        for (Pedido p : historial) {
            modeloPedidos.addRow(new Object[]{
                "#" + p.getId(), p.getFechaFormateada(),
                "$" + String.format("%,.0f", p.getTotal()),
                p.getMetodoPago().getMetodo(), p.getEstado().name()
            });
        }
    }

    private void mostrarDetalle() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) return;
        ArrayList<Pedido> historial = contexto.getPedidoService()
                .getHistorialCliente(contexto.getUsuarioActual().getId());
        Pedido pedido = historial.get(fila);

        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(pedido.getId()).append(" — ").append(pedido.getFechaFormateada()).append("\n");
        sb.append("Estado: ").append(pedido.getEstado().name()).append("\n");
        sb.append("Dirección: ").append(pedido.getDireccionEntrega()).append("\n");
        sb.append("Pago: ").append(pedido.getMetodoPago().getDescripcionPago()).append("\n");
        sb.append("─────────────────────────────────\n");
        for (ItemCarrito item : pedido.getItems()) {
            sb.append(String.format("  • %-25s x%d  $%,.0f%n",
                item.getProducto().getNombre(), item.getCantidad(), item.getSubtotal()));
        }
        sb.append("─────────────────────────────────\n");
        sb.append(String.format("TOTAL: $%,.0f%n", pedido.getTotal()));
        txtDetalle.setText(sb.toString());
    }
}
