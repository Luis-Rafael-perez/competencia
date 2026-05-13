package persistencia;

import modelo.pedido.Pedido;
import modelo.carrito.ItemCarrito;
import modelo.pago.*;
import modelo.producto.*;
import java.util.ArrayList;

/**
 * Persiste y carga pedidos desde un archivo CSV.
 * Guarda los datos esenciales del pedido (sin reconstruir objetos completos).
 * Formato: id,idCliente,total,estado,metodoPago,fecha,direccion,items
 * items: prodId:nombre:precio:cantidad|prodId:nombre:precio:cantidad
 */
public class PedidoRepositorio {

    private static final String ARCHIVO = "pedidos.csv";

    public static void guardar(ArrayList<Pedido> pedidos) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Pedido pedido : pedidos) {
                // Serializar ítems
                StringBuilder items = new StringBuilder();
                for (ItemCarrito item : pedido.getItems()) {
                    if (items.length() > 0) items.append("|");
                    items.append(item.getProducto().getId())
                         .append(":").append(escapar(item.getProducto().getNombre()))
                         .append(":").append(item.getProducto().getPrecio())
                         .append(":").append(item.getCantidad());
                }

                sb.append(String.join(",",
                    String.valueOf(pedido.getId()),
                    String.valueOf(pedido.getIdCliente()),
                    String.valueOf(pedido.getTotal()),
                    pedido.getEstado().name(),
                    pedido.getMetodoPago().getMetodo(),
                    pedido.getFechaFormateada().replace(",", ";"),
                    escapar(pedido.getDireccionEntrega()),
                    items.toString()
                )).append("\n");
            }
            GestorArchivos.escribir(ARCHIVO, sb.toString());
            System.out.println("Pedidos guardados: " + pedidos.size());
        } catch (Exception e) {
            System.err.println("Error al guardar pedidos: " + e.getMessage());
        }
    }

    public static ArrayList<Pedido> cargar(ProductoRepositorio repo) {
        // Los pedidos guardados se reconstruyen de forma simplificada
        // para el historial — no necesitan el objeto Producto completo
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try {
            String contenido = GestorArchivos.leer(ARCHIVO);
            if (contenido == null || contenido.isEmpty()) return pedidos;

            String[] lineas = contenido.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                try {
                    String[] p = linea.split(",", -1);
                    int id          = Integer.parseInt(p[0].trim());
                    int idCliente   = Integer.parseInt(p[1].trim());
                    double total    = Double.parseDouble(p[2].trim());
                    Pedido.EstadoPedido estado =
                            Pedido.EstadoPedido.valueOf(p[3].trim());
                    String metodo   = p[4].trim();
                    String direccion = p[6].trim();
                    String itemsStr = p[7].trim();

                    // Reconstruir ítems del pedido
                    ArrayList<ItemCarrito> items = new ArrayList<>();
                    if (!itemsStr.isEmpty()) {
                        for (String itemStr : itemsStr.split("\\|")) {
                            String[] partes = itemStr.split(":");
                            int prodId    = Integer.parseInt(partes[0]);
                            String nombre = partes[1];
                            double precio = Double.parseDouble(partes[2]);
                            int cantidad  = Integer.parseInt(partes[3]);
                            // Producto simplificado para mostrar en historial
                            ProductoFisico prod = new ProductoFisico(
                                    prodId, nombre, "", precio, 0, "", 0, "");
                            items.add(new ItemCarrito(prod, cantidad));
                        }
                    }

                    // Reconstruir método de pago simplificado
                    Pago pago = metodo.equals("TARJETA")
                            ? new PagoTarjeta("0000000000001234", "Guardado", "00/00", "000")
                            : new PagoEfectivo(total);

                    Pedido pedido = new Pedido(id, idCliente, items, pago, direccion);
                    pedido.setEstado(estado);
                    pedidos.add(pedido);

                } catch (Exception e) {
                    System.err.println("Error al parsear línea de pedido: " + linea);
                }
            }
            System.out.println("Pedidos cargados: " + pedidos.size());
        } catch (Exception e) {
            System.err.println("Error al cargar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    private static String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";");
    }
}
