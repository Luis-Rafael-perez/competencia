package modelo.pedido;

import modelo.carrito.ItemCarrito;
import modelo.pago.Pago;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Representa un pedido confirmado por el cliente.
 * COLECCIONES: guarda los ítems comprados con ArrayList.
 */
public class Pedido {

    // Enum de estados del pedido
    public enum EstadoPedido {
        PENDIENTE, PAGADO, EN_ENVIO, ENTREGADO, CANCELADO
    }

    // ENCAPSULACIÓN: atributos privados
    private int id;
    private int idCliente;
    private ArrayList<ItemCarrito> items;
    private double total;
    private EstadoPedido estado;
    private Pago metodoPago;
    private LocalDateTime fechaCreacion;
    private String direccionEntrega;

    public Pedido(int id, int idCliente, ArrayList<ItemCarrito> items,
                  Pago metodoPago, String direccionEntrega) {
        this.id = id;
        this.idCliente = idCliente;
        this.items = new ArrayList<>(items); // copia defensiva
        this.metodoPago = metodoPago;
        this.direccionEntrega = direccionEntrega;
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        double suma = 0;
        for (ItemCarrito item : items) {
            suma += item.getSubtotal();
        }
        return suma;
    }

    public String getFechaFormateada() {
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public ArrayList<ItemCarrito> getItems() { return items; }
    public double getTotal() { return total; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public Pago getMetodoPago() { return metodoPago; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getDireccionEntrega() { return direccionEntrega; }

    @Override
    public String toString() {
        return String.format("Pedido #%d | %s | Total: $%.2f | Estado: %s",
            id, getFechaFormateada(), total, estado);
    }
}
