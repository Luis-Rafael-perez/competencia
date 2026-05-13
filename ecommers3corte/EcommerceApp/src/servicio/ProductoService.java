package servicio;

import modelo.producto.*;
import java.util.ArrayList;

/**
 * Gestiona el catálogo de productos: creación, consulta, modificación y eliminación.
 * COLECCIONES: usa ArrayList para el inventario.
 */
public class ProductoService {

    private ArrayList<Producto> productos;
    private int contadorId;

    public ProductoService() {
        this.productos = new ArrayList<>();
        this.contadorId = 1;
        crearDatosDePrueba();
    }

    private void crearDatosDePrueba() {
        productos.add(new ProductoFisico(contadorId++, "Laptop HP 15",
                "Procesador i5, 8GB RAM, 256GB SSD",
                2500000, 10, "Tecnología", 1.8, "35x25x2cm"));

        productos.add(new ProductoFisico(contadorId++, "Mouse Inalámbrico",
                "Mouse ergonómico 2.4GHz",
                85000, 50, "Tecnología", 0.1, "12x7x4cm"));

        productos.add(new ProductoFisico(contadorId++, "Teclado Mecánico",
                "Teclado RGB switches azules",
                320000, 20, "Tecnología", 0.8, "44x14x4cm"));

        productos.add(new ProductoDigital(contadorId++, "Curso Java POO",
                "Aprende los 4 pilares de POO desde cero",
                50000, "Educación",
                "https://tienda.com/descargas/java-poo.zip", "ZIP", 850.5));

        productos.add(new ProductoDigital(contadorId++, "Pack Wallpapers 4K",
                "500 fondos de pantalla en alta resolución",
                15000, "Diseño",
                "https://tienda.com/descargas/wallpapers.zip", "ZIP", 1200.0));
    }

    /**
     * Agrega un nuevo producto físico al catálogo.
     * MANEJO DE EXCEPCIONES: valida datos antes de agregar.
     */
    public ProductoFisico agregarProductoFisico(String nombre, String descripcion,
            double precio, int stock, String categoria,
            double peso, String dimensiones) throws Exception {
        try {
            validarDatosProducto(nombre, precio, stock);
            ProductoFisico p = new ProductoFisico(contadorId++, nombre, descripcion,
                    precio, stock, categoria, peso, dimensiones);
            productos.add(p);
            return p;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Agrega un nuevo producto digital al catálogo.
     */
    public ProductoDigital agregarProductoDigital(String nombre, String descripcion,
            double precio, String categoria, String url,
            String formato, double tamano) throws Exception {
        try {
            validarDatosProducto(nombre, precio, 1);
            ProductoDigital p = new ProductoDigital(contadorId++, nombre, descripcion,
                    precio, categoria, url, formato, tamano);
            productos.add(p);
            return p;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Modifica los datos básicos de un producto existente.
     */
    public boolean modificarProducto(int id, String nombre, String descripcion,
                                     double precio, int stock) throws Exception {
        try {
            validarDatosProducto(nombre, precio, stock);
            Producto p = buscarPorId(id);
            if (p == null) throw new Exception("Producto no encontrado con ID: " + id);
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setPrecio(precio);
            p.setStock(stock);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Elimina un producto del catálogo.
     */
    public boolean eliminarProducto(int id) throws Exception {
        Producto p = buscarPorId(id);
        if (p == null) throw new Exception("Producto no encontrado con ID: " + id);
        return productos.removeIf(prod -> prod.getId() == id);
    }

    /**
     * Busca productos por nombre (búsqueda parcial, sin mayúsculas).
     */
    public ArrayList<Producto> buscarPorNombre(String nombre) {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Filtra productos por categoría.
     */
    public ArrayList<Producto> filtrarPorCategoria(String categoria) {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public ArrayList<Producto> getTodosLosProductos() {
        return productos;
    }

    private void validarDatosProducto(String nombre, double precio, int stock)
            throws Exception {
        if (nombre == null || nombre.trim().isEmpty())
            throw new Exception("El nombre del producto no puede estar vacío.");
        if (precio <= 0)
            throw new Exception("El precio debe ser mayor a cero.");
        if (stock < 0)
            throw new Exception("El stock no puede ser negativo.");
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
