package modelo.usuario;

import modelo.pedido.Pedido;
import java.util.ArrayList;

/**
 * HERENCIA: Cliente extiende Usuario.
 * Representa un comprador en el sistema.
 */
public class Cliente extends Usuario {

    private String direccion;
    private String telefono;
    // COLECCIONES: historial de pedidos del cliente
    private ArrayList<Pedido> historialPedidos;

    public Cliente(int id, String nombre, String email, String password,
                   String direccion, String telefono) {
        super(id, nombre, email, password); // llama al constructor padre
        this.direccion = direccion;
        this.telefono = telefono;
        this.historialPedidos = new ArrayList<>();
    }

    // POLIMORFISMO: sobrescribe el método abstracto
    @Override
    public String getRol() {
        return "CLIENTE";
    }

    // POLIMORFISMO: sobrescribe getResumen con información adicional
    @Override
    public String getResumen() {
        return super.getResumen() + " | Tel: " + telefono;
    }

    public void agregarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    // Getters y Setters
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public ArrayList<Pedido> getHistorialPedidos() { return historialPedidos; }
}
