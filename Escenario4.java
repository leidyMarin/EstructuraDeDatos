import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class Escenario4 {

    public static void main(String[] args) {

        int[] tamanos = {100, 1000, 10000, 100000};

        for (int t = 0; t < tamanos.length; t++) {
            int n = tamanos[t];
            System.out.println("----- Prueba con " + n + " productos -----");

            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();

            HashMap<String, ProductoE4> porCodigo = new HashMap<>();
            TreeMap<Integer, LinkedList<ProductoE4>> porPrecio = new TreeMap<>();

            // 1. Insertar productos
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                int precio = (int) (Math.random() * 2000000) + 5000;
                ProductoE4 p = new ProductoE4("SKU" + i, "Articulo " + i, precio);

                porCodigo.put(p.codigo, p);

                if (!porPrecio.containsKey(precio)) {
                    porPrecio.put(precio, new LinkedList<ProductoE4>());
                }
                porPrecio.get(precio).add(p);
            }
            long fin = System.nanoTime();
            double tiempoInsertar = (fin - inicio) / 1000000.0;

            long memoriaDespues = runtime.totalMemory() - runtime.freeMemory();
            double memoriaMB = (memoriaDespues - memoriaAntes) / (1024.0 * 1024.0);

            // 2. Buscar 1000 productos por codigo con HashMap
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String codigo = "SKU" + (i * n / 1000);
                ProductoE4 encontrado = porCodigo.get(codigo);
            }
            fin = System.nanoTime();
            double tiempoBuscar = (fin - inicio) / 1000000.0;

            // 3. Mostrar productos ordenados por precio
            inicio = System.nanoTime();
            int contador = 0;
            for (LinkedList<ProductoE4> productosMismoPrecio : porPrecio.values()) {
                for (ProductoE4 p : productosMismoPrecio) {
                    contador++;
                }
            }
            fin = System.nanoTime();
            double tiempoMostrar = (fin - inicio) / 1000000.0;

            // Comparacion 1: buscar con TreeMap por codigo
            TreeMap<String, ProductoE4> arbol = new TreeMap<>();
            for (ProductoE4 p : porCodigo.values()) {
                arbol.put(p.codigo, p);
            }
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String codigo = "SKU" + (i * n / 1000);
                ProductoE4 encontrado = arbol.get(codigo);
            }
            fin = System.nanoTime();
            double tiempoBuscarArbol = (fin - inicio) / 1000000.0;

            // Comparacion 2: buscar con LinkedList
            LinkedList<ProductoE4> lista = new LinkedList<>();
            for (ProductoE4 p : porCodigo.values()) {
                lista.add(p);
            }
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String codigo = "SKU" + (i * n / 1000);
                for (ProductoE4 p : lista) {
                    if (p.codigo.equals(codigo)) {
                        break;
                    }
                }
            }
            fin = System.nanoTime();
            double tiempoBuscarLista = (fin - inicio) / 1000000.0;

            System.out.println("Tiempo insertar: " + tiempoInsertar + " ms");
            System.out.println("Tiempo buscar 1000 HashMap: " + tiempoBuscar + " ms");
            System.out.println("Tiempo mostrar por precio: " + tiempoMostrar + " ms");
            System.out.println("Memoria usada: " + memoriaMB + " MB");
            System.out.println("Tiempo buscar 1000 TreeMap: " + tiempoBuscarArbol + " ms");
            System.out.println("Tiempo buscar 1000 LinkedList: " + tiempoBuscarLista + " ms");
            System.out.println();
        }
    }
}
