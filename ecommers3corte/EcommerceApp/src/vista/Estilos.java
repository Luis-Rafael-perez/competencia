package vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Utilidad de estilos visuales.
 * Estrategia: fondo claro + texto oscuro + borde de color
 * para garantizar legibilidad en Windows, Mac y Linux.
 */
public class Estilos {

    // Colores de texto (oscuros, siempre visibles)
    public static final Color TEXTO_AZUL    = new Color(10,  80, 160);
    public static final Color TEXTO_VERDE   = new Color(20, 120,  60);
    public static final Color TEXTO_ROJO    = new Color(160,  20,  20);
    public static final Color TEXTO_GRIS    = new Color(60,   75,  90);
    public static final Color TEXTO_NARANJA = new Color(180,  80,   0);

    // Fondos claros para botones
    public static final Color FONDO_AZUL    = new Color(220, 235, 255);
    public static final Color FONDO_VERDE   = new Color(215, 245, 225);
    public static final Color FONDO_ROJO    = new Color(255, 220, 220);
    public static final Color FONDO_GRIS    = new Color(230, 235, 240);
    public static final Color FONDO_NARANJA = new Color(255, 235, 210);

    // Fondo general de la app
    public static final Color FONDO_APP  = new Color(236, 240, 245);
    public static final Color AZUL       = new Color(10,  80, 160); // para títulos
    public static final Color AZUL_OSCURO = new Color(13, 60, 120); // para barras

    /**
     * Crea un botón con fondo claro y texto oscuro del mismo color.
     * type: "azul" | "verde" | "rojo" | "gris" | "naranja"
     */
    public static JButton crearBoton(String texto, String tipo) {
        Color fondo, textoColor, borde;
        switch (tipo) {
            case "verde":
                fondo = FONDO_VERDE; textoColor = TEXTO_VERDE;
                borde = new Color(20, 140, 70); break;
            case "rojo":
                fondo = FONDO_ROJO; textoColor = TEXTO_ROJO;
                borde = new Color(180, 20, 20); break;
            case "gris":
                fondo = FONDO_GRIS; textoColor = TEXTO_GRIS;
                borde = new Color(100, 120, 140); break;
            case "naranja":
                fondo = FONDO_NARANJA; textoColor = TEXTO_NARANJA;
                borde = new Color(200, 100, 0); break;
            default: // azul
                fondo = FONDO_AZUL; textoColor = TEXTO_AZUL;
                borde = new Color(10, 80, 160); break;
        }
        return fabricar(texto, fondo, textoColor, borde);
    }

    // Métodos de conveniencia por color
    public static JButton botonAzul(String texto)    { return crearBoton(texto, "azul"); }
    public static JButton botonVerde(String texto)   { return crearBoton(texto, "verde"); }
    public static JButton botonRojo(String texto)    { return crearBoton(texto, "rojo"); }
    public static JButton botonGris(String texto)    { return crearBoton(texto, "gris"); }
    public static JButton botonNaranja(String texto) { return crearBoton(texto, "naranja"); }

    private static JButton fabricar(String texto, Color fondo, Color textoColor, Color borde) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBorder(new LineBorder(borde, 2, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(
                btn.getPreferredSize().width + 16, 36));
        return btn;
    }

    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(AZUL);
        return lbl;
    }
}
