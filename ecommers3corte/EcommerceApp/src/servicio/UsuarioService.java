package servicio;

import modelo.usuario.*;
import modelo.excepciones.CredencialesInvalidasException;
import java.util.ArrayList;

/**
 * Gestiona el registro, login y administración de usuarios.
 * COLECCIONES: mantiene la lista de usuarios en memoria.
 */
public class UsuarioService {

    // COLECCIONES: lista de todos los usuarios registrados
    private ArrayList<Usuario> usuarios;
    private int contadorId;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.contadorId = 1;
        crearDatosDePrueba();
    }

    // Datos iniciales para probar sin base de datos aún
    private void crearDatosDePrueba() {
        usuarios.add(new Administrador(contadorId++, "Admin Principal",
                "admin@tienda.com", "admin123", "ADM-001"));
        usuarios.add(new Cliente(contadorId++, "Juan Pérez",
                "juan@email.com", "1234", "Calle 5 #10-20", "3001112233"));
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * MANEJO DE EXCEPCIONES: valida que el email no esté duplicado.
     */
    public Cliente registrarCliente(String nombre, String email, String password,
                                    String direccion, String telefono) throws Exception {
        try {
            if (emailExiste(email)) {
                throw new Exception("Ya existe un usuario con ese email: " + email);
            }
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("El nombre no puede estar vacío.");
            }
            if (password == null || password.length() < 4) {
                throw new Exception("La contraseña debe tener al menos 4 caracteres.");
            }

            Cliente nuevo = new Cliente(contadorId++, nombre, email,
                                        password, direccion, telefono);
            usuarios.add(nuevo);
            return nuevo;

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Inicia sesión y retorna el usuario autenticado.
     * POLIMORFISMO: retorna Usuario (puede ser Cliente o Administrador).
     */
    public Usuario login(String email, String password)
            throws CredencialesInvalidasException {
        try {
            for (Usuario u : usuarios) {
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    return u;
                }
            }
            throw new CredencialesInvalidasException(
                    "Email o contraseña incorrectos.");
        } catch (CredencialesInvalidasException e) {
            throw e;
        }
    }

    /**
     * Busca un usuario por su ID.
     */
    public Usuario buscarPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    /**
     * Retorna solo los clientes (filtra administradores).
     */
    public ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Cliente) {
                clientes.add((Cliente) u);
            }
        }
        return clientes;
    }

    public ArrayList<Usuario> getTodosLosUsuarios() {
        return usuarios;
    }

    /**
     * Modifica los datos de un cliente existente.
     * MANEJO DE EXCEPCIONES: valida que el usuario exista y sea cliente.
     */
    public boolean modificarCliente(int id, String nombre, String email,
                                    String direccion, String telefono) throws Exception {
        Usuario u = buscarPorId(id);
        if (u == null) throw new Exception("Usuario no encontrado con ID: " + id);
        if (!(u instanceof Cliente)) throw new Exception("Solo se pueden modificar clientes.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new Exception("El nombre no puede estar vacío.");

        // Verificar email duplicado solo si cambió
        if (!u.getEmail().equalsIgnoreCase(email) && emailExiste(email))
            throw new Exception("Ya existe un usuario con ese email: " + email);

        Cliente c = (Cliente) u;
        c.setNombre(nombre);
        c.setEmail(email);
        c.setDireccion(direccion);
        c.setTelefono(telefono);
        return true;
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * No permite eliminar administradores.
     */
    public boolean eliminarCliente(int id) throws Exception {
        Usuario u = buscarPorId(id);
        if (u == null) throw new Exception("Usuario no encontrado con ID: " + id);
        if (u instanceof Administrador)
            throw new Exception("No se puede eliminar un administrador.");
        return usuarios.removeIf(usr -> usr.getId() == id);
    }

    private boolean emailExiste(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
