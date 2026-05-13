package modelo.usuario;

/**
 * Clase abstracta que representa un usuario del sistema.
 * ABSTRACCIÓN: modela el concepto general de usuario.
 * HERENCIA: Cliente y Administrador extienden esta clase.
 */
public abstract class Usuario {

    // ENCAPSULACIÓN: atributos privados
    private int id;
    private String nombre;
    private String email;
    private String password;

    // Constructor
    public Usuario(int id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // POLIMORFISMO: método abstracto que cada subclase sobrescribirá
    public abstract String getRol();

    // Método que puede ser sobrescrito con @Override
    public String getResumen() {
        return "[" + getRol() + "] " + nombre + " — " + email;
    }

    // Getters y Setters (ENCAPSULACIÓN)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return getResumen();
    }
}
