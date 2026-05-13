package vista;

import servicio.UsuarioService;
import modelo.usuario.*;
import modelo.excepciones.CredencialesInvalidasException;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private UsuarioService usuarioService;
    private AppContext contexto;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public VentanaLogin(UsuarioService usuarioService, AppContext contexto) {
        this.usuarioService = usuarioService;
        this.contexto = contexto;
        construirUI();
    }

    private void construirUI() {
        setTitle("E-Commerce — Iniciar Sesión");
        setSize(420, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilos.FONDO_APP);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(Estilos.crearTitulo("🛒 Mi Tienda Online"), gbc);

        JLabel sub = new JLabel("Inicia sesión para continuar", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(sub, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0; gbc.weightx = 0.35;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(txtEmail, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.35;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 32));
        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(txtPassword, gbc);

        JButton btnLogin = Estilos.botonAzul("Iniciar Sesión");
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        panel.add(btnLogin, gbc);

        JButton btnRegistrar = Estilos.botonAzul("¿No tienes cuenta? Regístrate");
        gbc.gridy = 5;
        panel.add(btnRegistrar, gbc);

        add(panel);

        btnLogin.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> abrirRegistro());
        txtPassword.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String email    = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Usuario usuario = usuarioService.login(email, password);
            contexto.setUsuarioActual(usuario);
            dispose();
            if (usuario instanceof Administrador)
                new VentanaAdmin(contexto).setVisible(true);
            else
                new VentanaCatalogo(contexto).setVisible(true);
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de acceso", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    private void abrirRegistro() {
        new VentanaRegistro(usuarioService, contexto, this).setVisible(true);
        setVisible(false);
    }
}
