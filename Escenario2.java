import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class Escenario2 {

    public static void main(String[] args) {

        String[] categorias = {"Tecnologia", "Hogar", "Ropa", "Deportes", "Juguetes", "Libros", "Belleza", "Mascotas"};
        int[] tamanos = {100, 1000, 10000, 100000};

        for (int t = 0; t < tamanos.length; t++) {
            int n = tamanos[t];
            System.out.println("----- Prueba con " + n + " productos -----");

            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();

            HashMap<String, Producto> porCodigo = new HashMap<>();
            TreeMap<Integer, LinkedList<Producto>> porPrecio = new TreeMap<>();
            HashMap<String, LinkedList<Producto>> porCategoria = new HashMap<>();
            LinkedList<Producto> lista = new LinkedList<>();

            // 1. Insertar productos (cada uno al inicio de la lista)
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                int precio = (int) (Math.random() * 500000) + 1000;
                String categoria = categorias[i % categorias.length];
                Producto p = new Producto("P" + i, "Producto " + i, precio, categoria);

                porCodigo.put(p.codigo, p);

                if (!porPrecio.containsKey(precio)) {
                    porPrecio.put(precio, new LinkedList<Producto>());
                }
                porPrecio.get(precio).add(p);

                if (!porCategoria.containsKey(categoria)) {
                    porCategoria.put(categoria, new LinkedList<Producto>());
                }
                porCategoria.get(categoria).add(p);

                lista.addFirst(p);
            }
            long fin = System.nanoTime();
            double tiempoInsertar = (fin - inicio) / 1000000.0;

            long memoriaDespues = runtime.totalMemory() - runtime.freeMemory();
            double memoriaMB = (memoriaDespues - memoriaAntes) / (1024.0 * 1024.0);

            // 2. Buscar 1000 productos por codigo
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String codigo = "P" + (i * n / 1000);
                Producto encontrado = porCodigo.get(codigo);
            }
            fin = System.nanoTime();
            double tiempoBuscar = (fin - inicio) / 1000000.0;

            // 3. Mostrar productos ordenados por precio (recorrer el TreeMap)
            inicio = System.nanoTime();
            int contador = 0;
            for (LinkedList<Producto> productosMismoPrecio : porPrecio.values()) {
                for (Producto p : productosMismoPrecio) {
                    contador++;
                }
            }
            fin = System.nanoTime();
            double tiempoOrdenado = (fin - inicio) / 1000000.0;

            // 4. Filtrar por categoria
            inicio = System.nanoTime();
            LinkedList<Producto> hogar = porCategoria.get("Hogar");
            fin = System.nanoTime();
            double tiempoFiltrar = (fin - inicio) / 1000000.0;

            // Comparacion: buscar los mismos 1000 codigos en la LinkedList
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String codigo = "P" + (i * n / 1000);
                for (Producto p : lista) {
                    if (p.codigo.equals(codigo)) {
                        break;
                    }
                }
            }
            fin = System.nanoTime();
            double tiempoBuscarLista = (fin - inicio) / 1000000.0;

            System.out.println("Tiempo insertar: " + tiempoInsertar + " ms");
            System.out.println("Tiempo buscar 1000: " + tiempoBuscar + " ms");
            System.out.println("Tiempo recorrer por precio: " + tiempoOrdenado + " ms");
            System.out.println("Tiempo filtrar categoria: " + tiempoFiltrar + " ms");
            System.out.println("Productos en Hogar: " + hogar.size());
            System.out.println("Memoria usada: " + memoriaMB + " MB");
            System.out.println("Tiempo buscar 1000 con LinkedList: " + tiempoBuscarLista + " ms");

            if (n == 100) {
                System.out.println("Primer producto de la lista (el mas nuevo): " + lista.getFirst().codigo);
                System.out.println("Precio mas barato: " + porPrecio.firstKey());
                System.out.println("Precio mas caro: " + porPrecio.lastKey());
            }
            System.out.println();
        }
    }
}
