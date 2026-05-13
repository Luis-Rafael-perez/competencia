package persistencia;

import modelo.usuario.*;
import java.util.ArrayList;

/**
 * Persiste y carga usuarios desde un archivo CSV.
 * Formato: id,tipo,nombre,email,password,extra1,extra2
 * MANEJO DE EXCEPCIONES: try/catch en todas las operaciones de archivo.
 */
public class UsuarioRepositorio {

    private static final String ARCHIVO = "usuarios.csv";

    /**
     * Guarda todos los usuarios en el archivo CSV.
     */
    public static void guardar(ArrayList<Usuario> usuarios) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Usuario u : usuarios) {
                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    sb.append(String.join(",",
                        String.valueOf(c.getId()),
                        "CLIENTE",
                        escapar(c.getNombre()),
                        escapar(c.getEmail()),
                        escapar(c.getPassword()),
                        escapar(c.getDireccion()),
                        escapar(c.getTelefono())
                    )).append("\n");
                } else if (u instanceof Administrador) {
                    Administrador a = (Administrador) u;
                    sb.append(String.join(",",
                        String.valueOf(a.getId()),
                        "ADMIN",
                        escapar(a.getNombre()),
                        escapar(a.getEmail()),
                        escapar(a.getPassword()),
                        escapar(a.getCodigoAdmin()),
                        ""
                    )).append("\n");
                }
            }
            GestorArchivos.escribir(ARCHIVO, sb.toString());
            System.out.println("Usuarios guardados: " + usuarios.size());
        } catch (Exception e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Carga los usuarios desde el archivo CSV.
     * Retorna lista vacía si el archivo no existe.
     */
    public static ArrayList<Usuario> cargar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            String contenido = GestorArchivos.leer(ARCHIVO);
            if (contenido == null || contenido.isEmpty()) return usuarios;

            String[] lineas = contenido.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                try {
                    String[] partes = linea.split(",", -1);
                    int id      = Integer.parseInt(partes[0].trim());
                    String tipo = partes[1].trim();
                    String nombre   = partes[2].trim();
                    String email    = partes[3].trim();
                    String password = partes[4].trim();

                    if (tipo.equals("CLIENTE")) {
                        String direccion = partes[5].trim();
                        String telefono  = partes[6].trim();
                        usuarios.add(new Cliente(id, nombre, email,
                                password, direccion, telefono));
                    } else if (tipo.equals("ADMIN")) {
                        String codigoAdmin = partes[5].trim();
                        usuarios.add(new Administrador(id, nombre, email,
                                password, codigoAdmin));
                    }
                } catch (Exception e) {
                    System.err.println("Error al parsear línea de usuario: " + linea);
                }
            }
            System.out.println("Usuarios cargados: " + usuarios.size());
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    // Evita comas dentro de los campos
    private static String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";");
    }
}
