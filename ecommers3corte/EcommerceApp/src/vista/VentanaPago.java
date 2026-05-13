package vista;

import modelo.pago.*;
import modelo.pedido.Pedido;
import modelo.usuario.Cliente;

import javax.swing.*;
import java.awt.*;

public class VentanaPago extends JFrame {

    private AppContext contexto;
    private VentanaCatalogo ventanaCatalogo;
    private JRadioButton rbTarjeta, rbEfectivo;
    private JPanel panelTarjeta, panelEfectivo;
    private JTextField txtNumTarjeta, txtTitular, txtFecha, txtDireccion;
    private JPasswordField txtCvv;
    private JTextField txtEfectivo;

    public VentanaPago(AppContext contexto, VentanaCatalogo ventanaCatalogo) {
        this.contexto        = contexto;
        this.ventanaCatalogo = ventanaCatalogo;
        construirUI();
    }

    private void construirUI() {
        setTitle("Confirmar pago");
        setSize(440, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Estilos.FONDO_APP);

        double total = contexto.getCarritoService().calcularTotal();
        JLabel lblTotal = new JLabel("Total a pagar: $" + String.format("%,.0f", total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Estilos.AZUL);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTotal);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblMetodo = new JLabel("Método de pago:");
        lblMetodo.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lblMetodo);

        rbTarjeta  = new JRadioButton("💳 Tarjeta de crédito/débito", true);
        rbEfectivo = new JRadioButton("💵 Efectivo");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbTarjeta);
        grupo.add(rbEfectivo);
        rbTarjeta.setBackground(Estilos.FONDO_APP);
        rbEfectivo.setBackground(Estilos.FONDO_APP);
        panel.add(rbTarjeta);
        panel.add(rbEfectivo);
        panel.add(Box.createVerticalStrut(10));

        panelTarjeta = new JPanel(new GridLayout(4, 2, 8, 8));
        panelTarjeta.setBackground(Estilos.FONDO_APP);
        panelTarjeta.setBorder(BorderFactory.createTitledBorder("Datos de tarjeta"));
        txtNumTarjeta = new JTextField();
        txtTitular    = new JTextField();
        txtFecha      = new JTextField("MM/AA");
        txtCvv        = new JPasswordField();
        panelTarjeta.add(new JLabel("Número:"));     panelTarjeta.add(txtNumTarjeta);
        panelTarjeta.add(new JLabel("Titular:"));    panelTarjeta.add(txtTitular);
        panelTarjeta.add(new JLabel("Vencimiento:")); panelTarjeta.add(txtFecha);
        panelTarjeta.add(new JLabel("CVV:"));        panelTarjeta.add(txtCvv);
        panel.add(panelTarjeta);

        panelEfectivo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEfectivo.setBackground(Estilos.FONDO_APP);
        panelEfectivo.setBorder(BorderFactory.createTitledBorder("Monto entregado"));
        txtEfectivo = new JTextField(10);
        panelEfectivo.add(new JLabel("$"));
        panelEfectivo.add(txtEfectivo);
        panelEfectivo.setVisible(false);
        panel.add(panelEfectivo);
        panel.add(Box.createVerticalStrut(10));

        JPanel panelDir = new JPanel(new BorderLayout(5, 0));
        panelDir.setBackground(Estilos.FONDO_APP);
        panelDir.add(new JLabel("Dirección de entrega:"), BorderLayout.WEST);
        txtDireccion = new JTextField();
        if (contexto.getUsuarioActual() instanceof Cliente)
            txtDireccion.setText(((Cliente) contexto.getUsuarioActual()).getDireccion());
        panelDir.add(txtDireccion, BorderLayout.CENTER);
        panel.add(panelDir);
        panel.add(Box.createVerticalStrut(15));

        JButton btnConfirmar = Estilos.botonVerde("✅ Confirmar pedido");
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(btnConfirmar);

        add(new JScrollPane(panel));

        rbTarjeta.addActionListener(e -> { panelTarjeta.setVisible(true); panelEfectivo.setVisible(false); pack(); });
        rbEfectivo.addActionListener(e -> { panelTarjeta.setVisible(false); panelEfectivo.setVisible(true); pack(); });
        btnConfirmar.addActionListener(e -> confirmarPedido());
    }

    private void confirmarPedido() {
        String direccion = txtDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa una dirección de entrega.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Pago pago;
            if (rbTarjeta.isSelected()) {
                String num     = txtNumTarjeta.getText().trim();
                String titular = txtTitular.getText().trim();
                String fecha   = txtFecha.getText().trim();
                String cvv     = new String(txtCvv.getPassword()).trim();
                if (num.isEmpty() || titular.isEmpty() || cvv.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Completa los datos de la tarjeta.",
                            "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                pago = new PagoTarjeta(num, titular, fecha, cvv);
            } else {
                double monto;
                try {
                    monto = Double.parseDouble(txtEfectivo.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingresa un monto válido.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pago = new PagoEfectivo(monto);
            }

            Pedido pedido = contexto.getPedidoService().confirmarPedido(
                    (Cliente) contexto.getUsuarioActual(),
                    contexto.getCarritoService().getCarritoActual(),
                    pago, direccion);

            contexto.getPersistencia().guardarTodo();
            contexto.getCarritoService().vaciarCarrito();
            ventanaCatalogo.actualizarCarrito();

            JOptionPane.showMessageDialog(this,
                "✅ ¡Pedido #" + pedido.getId() + " confirmado!\n" +
                "Total: $" + String.format("%,.0f", pedido.getTotal()) + "\n" +
                "Método: " + pago.getMetodo(),
                "Pedido exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error al procesar pedido", JOptionPane.ERROR_MESSAGE);
        }
    }
}
