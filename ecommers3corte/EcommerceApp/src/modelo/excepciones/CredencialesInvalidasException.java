package modelo.excepciones;

/**
 * Se lanza cuando un usuario intenta iniciar sesión con credenciales incorrectas.
 */
public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
