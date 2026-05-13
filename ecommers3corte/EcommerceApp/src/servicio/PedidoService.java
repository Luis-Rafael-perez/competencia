package servicio;

import modelo.carrito.Carrito;
import modelo.carrito.ItemCarrito;
import modelo.pago.Pago;
import modelo.pedido.Pedido;
import modelo.producto.Producto;
import modelo.usuario.Cliente;
import java.util.ArrayList;

/**
 * Gestiona el flujo completo de un pedido:
 * carrito confirmado → pago → generación del pedido → actualización de stock.
 * COLECCIONES: mantiene el historial global de pedidos.
 */
public class PedidoService {

    // COLECCIONES: todos los pedidos del sistema
    private ArrayList<Pedido> pedidos;
    private int contadorId;

    public PedidoService() {
        this.pedidos = new ArrayList<>();
        this.contadorId = 1001;
    }

    /**
     * Confirma el carrito, procesa el pago y genera el pedido.
     * MANEJO DE EXCEPCIONES: valida carrito, pago y stock antes de confirmar.
     */
    public Pedido confirmarPedido(Cliente cliente, Carrito carrito,
                                  Pago metodoPago, String direccionEntrega) throws Exception {
        try {
            // Validaciones previas
            if (carrito == null || carrito.estaVacio())
                throw new Exception("El carrito está vacío. Agrega productos antes de confirmar.");

            if (direccionEntrega == null || direccionEntrega.trim().isEmpty())
                throw new Exception("Debes ingresar una dirección de entrega.");

            double total = carrito.calcularTotal();

            // Procesar pago
            boolean pagoCorrecto = metodoPago.procesarPago(total);
            if (!pagoCorrecto)
                throw new Exception("El pago no pudo procesarse. Verifica los datos.");

            // Generar el pedido
            Pedido nuevoPedido = new Pedido(contadorId++, cliente.getId(),
                    carrito.getItems(), metodoPago, direccionEntrega);

            // Actualizar stock de cada producto comprado
            for (ItemCarrito item : carrito.getItems()) {
                Producto p = item.getProducto();
                p.reducirStock(item.getCantidad());
            }

            // Cambiar estado a PAGADO
            nuevoPedido.setEstado(Pedido.EstadoPedido.PAGADO);

            // Agregar al historial del cliente y al sistema
            cliente.agregarPedido(nuevoPedido);
            pedidos.add(nuevoPedido);

            return nuevoPedido;

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Retorna el historial de pedidos de un cliente específico.
     */
    public ArrayList<Pedido> getHistorialCliente(int idCliente) {
        ArrayList<Pedido> historial = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getIdCliente() == idCliente) {
                historial.add(p);
            }
        }
        return historial;
    }

    /**
     * Cambia el estado de un pedido (usado por el administrador).
     */
    public boolean actualizarEstado(int idPedido, Pedido.EstadoPedido nuevoEstado)
            throws Exception {
        for (Pedido p : pedidos) {
            if (p.getId() == idPedido) {
                p.setEstado(nuevoEstado);
                return true;
            }
        }
        throw new Exception("Pedido no encontrado con ID: " + idPedido);
    }

    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public ArrayList<Pedido> getTodosLosPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
