package persistencia;

import modelo.producto.*;
import java.util.ArrayList;

/**
 * Persiste y carga productos desde un archivo CSV.
 * Formato físico:  id,FISICO,nombre,descripcion,precio,stock,categoria,peso,dimensiones
 * Formato digital: id,DIGITAL,nombre,descripcion,precio,stock,categoria,url,formato,tamano
 */
public class ProductoRepositorio {

    private static final String ARCHIVO = "productos.csv";

    public static void guardar(ArrayList<Producto> productos) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Producto p : productos) {
                if (p instanceof ProductoFisico) {
                    ProductoFisico pf = (ProductoFisico) p;
                    sb.append(String.join(",",
                        String.valueOf(pf.getId()),
                        "FISICO",
                        escapar(pf.getNombre()),
                        escapar(pf.getDescripcion()),
                        String.valueOf(pf.getPrecio()),
                        String.valueOf(pf.getStock()),
                        escapar(pf.getCategoria()),
                        String.valueOf(pf.getPesoKg()),
                        escapar(pf.getDimensiones())
                    )).append("\n");
                } else if (p instanceof ProductoDigital) {
                    ProductoDigital pd = (ProductoDigital) p;
                    sb.append(String.join(",",
                        String.valueOf(pd.getId()),
                        "DIGITAL",
                        escapar(pd.getNombre()),
                        escapar(pd.getDescripcion()),
                        String.valueOf(pd.getPrecio()),
                        "0",
                        escapar(pd.getCategoria()),
                        escapar(pd.getUrlDescarga()),
                        escapar(pd.getFormatoArchivo()),
                        String.valueOf(pd.getTamanoMB())
                    )).append("\n");
                }
            }
            GestorArchivos.escribir(ARCHIVO, sb.toString());
            System.out.println("Productos guardados: " + productos.size());
        } catch (Exception e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }

    public static ArrayList<Producto> cargar() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            String contenido = GestorArchivos.leer(ARCHIVO);
            if (contenido == null || contenido.isEmpty()) return productos;

            String[] lineas = contenido.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                try {
                    String[] p = linea.split(",", -1);
                    int id       = Integer.parseInt(p[0].trim());
                    String tipo  = p[1].trim();
                    String nombre = p[2].trim();
                    String desc  = p[3].trim();
                    double precio = Double.parseDouble(p[4].trim());
                    int stock    = Integer.parseInt(p[5].trim());
                    String cat   = p[6].trim();

                    if (tipo.equals("FISICO")) {
                        double peso = Double.parseDouble(p[7].trim());
                        String dim  = p[8].trim();
                        productos.add(new ProductoFisico(id, nombre, desc,
                                precio, stock, cat, peso, dim));
                    } else if (tipo.equals("DIGITAL")) {
                        String url     = p[7].trim();
                        String formato = p[8].trim();
                        double tamano  = Double.parseDouble(p[9].trim());
                        productos.add(new ProductoDigital(id, nombre, desc,
                                precio, cat, url, formato, tamano));
                    }
                } catch (Exception e) {
                    System.err.println("Error al parsear línea de producto: " + linea);
                }
            }
            System.out.println("Productos cargados: " + productos.size());
        } catch (Exception e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
        return productos;
    }

    private static String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";");
    }
}
