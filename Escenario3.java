import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Escenario3 {

    public static void main(String[] args) {

        int[] tamanos = {100, 1000, 10000, 100000};

        for (int t = 0; t < tamanos.length; t++) {
            int n = tamanos[t];
            System.out.println("----- Prueba con " + n + " solicitudes -----");

            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();

            LinkedHashMap<Integer, Solicitud> pendientes = new LinkedHashMap<>();

            // 1. Registrar solicitudes
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                Solicitud s = new Solicitud(i, "Usuario" + i, "Origen " + i, "Destino " + i);
                pendientes.put(i, s);
            }
            long fin = System.nanoTime();
            double tiempoRegistrar = (fin - inicio) / 1000000.0;

            long memoriaDespues = runtime.totalMemory() - runtime.freeMemory();
            double memoriaMB = (memoriaDespues - memoriaAntes) / (1024.0 * 1024.0);

            // Las mismas solicitudes en una cola para comparar
            Queue<Solicitud> cola = new LinkedList<>();
            for (Solicitud s : pendientes.values()) {
                cola.add(s);
            }

            // Se cancela el 10% de las solicitudes (maximo 1000)
            int cantidad = n / 10;
            if (cantidad > 1000) {
                cantidad = 1000;
            }
            int salto = n / cantidad;

            // 2. Cancelar solicitudes en el LinkedHashMap
            inicio = System.nanoTime();
            for (int i = 0; i < cantidad; i++) {
                pendientes.remove(i * salto);
            }
            fin = System.nanoTime();
            double tiempoCancelar = (fin - inicio) / 1000000.0;

            // Comparacion: cancelar en la Queue (hay que buscarla primero)
            inicio = System.nanoTime();
            for (int i = 0; i < cantidad; i++) {
                int idCancelar = i * salto;
                for (Solicitud s : cola) {
                    if (s.id == idCancelar) {
                        cola.remove(s);
                        break;
                    }
                }
            }
            fin = System.nanoTime();
            double tiempoCancelarCola = (fin - inicio) / 1000000.0;

            // 3. Atender la mitad de las solicitudes (siempre la mas antigua)
            inicio = System.nanoTime();
            int mitad = pendientes.size() / 2;
            for (int i = 0; i < mitad; i++) {
                int idMasAntigua = -1;
                for (int id : pendientes.keySet()) {
                    idMasAntigua = id;
                    break;    // la primera es la mas antigua
                }
                pendientes.remove(idMasAntigua);
            }
            fin = System.nanoTime();
            double tiempoAtender = (fin - inicio) / 1000000.0;

            // 4. Mostrar solicitudes pendientes
            inicio = System.nanoTime();
            int contador = 0;
            for (Solicitud s : pendientes.values()) {
                contador++;
            }
            fin = System.nanoTime();
            double tiempoMostrar = (fin - inicio) / 1000000.0;

            System.out.println("Tiempo registrar: " + tiempoRegistrar + " ms");
            System.out.println("Solicitudes canceladas: " + cantidad);
            System.out.println("Tiempo cancelar: " + tiempoCancelar + " ms");
            System.out.println("Tiempo atender la mitad: " + tiempoAtender + " ms");
            System.out.println("Tiempo mostrar pendientes: " + tiempoMostrar + " ms");
            System.out.println("Pendientes al final: " + contador);
            System.out.println("Memoria usada: " + memoriaMB + " MB");
            System.out.println("Tiempo cancelar con Queue: " + tiempoCancelarCola + " ms");
            System.out.println();
        }

        // Prueba pequena para ver que funciona
        System.out.println("----- Prueba pequena -----");
        LinkedHashMap<Integer, Solicitud> pendientes = new LinkedHashMap<>();
        pendientes.put(1, new Solicitud(1, "Ana", "Centro", "Aeropuerto"));
        pendientes.put(2, new Solicitud(2, "Luis", "Norte", "Terminal"));
        pendientes.put(3, new Solicitud(3, "Marta", "Sur", "Unicentro"));
        pendientes.put(4, new Solicitud(4, "Pedro", "Estadio", "Centro"));

        pendientes.remove(2);
        System.out.println("Luis cancelo su solicitud");

        int primera = -1;
        for (int id : pendientes.keySet()) {
            primera = id;
            break;
        }
        Solicitud atendida = pendientes.remove(primera);
        System.out.println("Se atiende primero a: " + atendida.usuario);

        System.out.println("Solicitudes pendientes:");
        for (Solicitud s : pendientes.values()) {
            System.out.println(s.id + " - " + s.usuario + " (" + s.origen + " a " + s.destino + ")");
        }
    }
}
