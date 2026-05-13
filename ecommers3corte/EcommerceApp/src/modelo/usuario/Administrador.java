package modelo.usuario;

/**
 * HERENCIA: Administrador extiende Usuario.
 * Tiene permisos especiales para gestionar el catálogo.
 */
public class Administrador extends Usuario {

    private String codigoAdmin;
    private boolean puedeEliminarProductos;

    public Administrador(int id, String nombre, String email, String password,
                         String codigoAdmin) {
        super(id, nombre, email, password);
        this.codigoAdmin = codigoAdmin;
        this.puedeEliminarProductos = true;
    }

    // POLIMORFISMO: sobrescribe el método abstracto
    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }

    // POLIMORFISMO: personaliza el resumen para admin
    @Override
    public String getResumen() {
        return super.getResumen() + " | Código: " + codigoAdmin;
    }

    // Getters y Setters
    public String getCodigoAdmin() { return codigoAdmin; }
    public void setCodigoAdmin(String codigoAdmin) { this.codigoAdmin = codigoAdmin; }

    public boolean isPuedeEliminarProductos() { return puedeEliminarProductos; }
    public void setPuedeEliminarProductos(boolean puede) {
        this.puedeEliminarProductos = puede;
    }
}
