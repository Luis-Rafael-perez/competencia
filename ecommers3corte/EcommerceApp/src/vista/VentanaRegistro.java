package vista;

import servicio.UsuarioService;
import modelo.usuario.Cliente;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistro extends JFrame {

    private UsuarioService usuarioService;
    private AppContext contexto;
    private JFrame ventanaAnterior;
    private JTextField txtNombre, txtEmail, txtDireccion, txtTelefono;
    private JPasswordField txtPassword, txtConfirmar;

    public VentanaRegistro(UsuarioService usuarioService, AppContext contexto, JFrame ventanaAnterior) {
        this.usuarioService  = usuarioService;
        this.contexto        = contexto;
        this.ventanaAnterior = ventanaAnterior;
        construirUI();
    }

    private void construirUI() {
        setTitle("Registro de nuevo cliente");
        setSize(460, 430);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilos.FONDO_APP);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(Estilos.crearTitulo("Crear cuenta"), gbc);

        gbc.gridwidth = 1;
        String[] etiquetas = {"Nombre:", "Email:", "Contraseña:", "Confirmar:", "Dirección:", "Teléfono:"};
        JComponent[] campos = {
            txtNombre    = new JTextField(),
            txtEmail     = new JTextField(),
            txtPassword  = new JPasswordField(),
            txtConfirmar = new JPasswordField(),
            txtDireccion = new JTextField(),
            txtTelefono  = new JTextField()
        };

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1; gbc.weightx = 0.3;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            campos[i].setPreferredSize(new Dimension(200, 30));
            panel.add(campos[i], gbc);
        }

        JButton btnRegistrar = Estilos.botonVerde("Registrarme");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.weightx = 1;
        panel.add(btnRegistrar, gbc);

        JButton btnVolver = Estilos.botonAzul("← Volver al login");
        gbc.gridy = 8;
        panel.add(btnVolver, gbc);

        add(panel);

        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> { ventanaAnterior.setVisible(true); dispose(); });
    }

    private void registrar() {
        String nombre    = txtNombre.getText().trim();
        String email     = txtEmail.getText().trim();
        String password  = new String(txtPassword.getPassword()).trim();
        String confirmar = new String(txtConfirmar.getPassword()).trim();
        String direccion = txtDireccion.getText().trim();
        String telefono  = txtTelefono.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            usuarioService.registrarCliente(nombre, email, password, direccion, telefono);
            contexto.getPersistencia().guardarUsuarios();
            JOptionPane.showMessageDialog(this,
                    "¡Cuenta creada! Ya puedes iniciar sesión.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            ventanaAnterior.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
