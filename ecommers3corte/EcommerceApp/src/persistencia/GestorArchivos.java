package persistencia;

import java.io.*;
import java.nio.file.*;

/**
 * Utilidad base para leer y escribir archivos de texto.
 * MANEJO DE EXCEPCIONES: todos los métodos usan try/catch.
 */
public class GestorArchivos {

    // Carpeta donde se guardarán los datos
    private static final String CARPETA_DATOS = "datos/";

    /**
     * Crea la carpeta de datos si no existe.
     */
    public static void inicializar() {
        try {
            File carpeta = new File(CARPETA_DATOS);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
                System.out.println("Carpeta de datos creada: " + carpeta.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error al crear carpeta de datos: " + e.getMessage());
        }
    }

    /**
     * Escribe contenido de texto en un archivo.
     */
    public static void escribir(String nombreArchivo, String contenido) {
        try {
            inicializar();
            FileWriter fw = new FileWriter(CARPETA_DATOS + nombreArchivo);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (IOException e) {
            System.err.println("Error al escribir archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    /**
     * Lee el contenido completo de un archivo como texto.
     * Retorna null si el archivo no existe.
     */
    public static String leer(String nombreArchivo) {
        try {
            File archivo = new File(CARPETA_DATOS + nombreArchivo);
            if (!archivo.exists()) return null;

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            br.close();
            return sb.toString().trim();

        } catch (IOException e) {
            System.err.println("Error al leer archivo " + nombreArchivo + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si un archivo de datos existe.
     */
    public static boolean existe(String nombreArchivo) {
        return new File(CARPETA_DATOS + nombreArchivo).exists();
    }

    public static String getCarpetaDatos() {
        return CARPETA_DATOS;
    }
}
