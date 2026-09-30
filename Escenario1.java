import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Escenario1 {

    public static void main(String[] args) {

        int[] tamanos = {100, 1000, 10000, 50000, 100000};

        for (int t = 0; t < tamanos.length; t++) {
            int n = tamanos[t];
            System.out.println("----- Prueba con " + n + " pacientes -----");

            // Se limpia la memoria antes de medir para que el dato sea mas real
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();

            LinkedHashMap<String, Paciente> pacientes = new LinkedHashMap<>();

            // 1. Registrar pacientes
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                String documento = "DOC" + i;
                if (!pacientes.containsKey(documento)) {
                    Paciente p = new Paciente(documento, "Paciente " + i, (i % 5) + 1, i);
                    pacientes.put(documento, p);
                }
            }
            long fin = System.nanoTime();
            double tiempoRegistrar = (fin - inicio) / 1000000.0;

            long memoriaDespues = runtime.totalMemory() - runtime.freeMemory();
            double memoriaMB = (memoriaDespues - memoriaAntes) / (1024.0 * 1024.0);

            // 2. Buscar 1000 pacientes por documento
            inicio = System.nanoTime();
            int encontrados = 0;
            for (int i = 0; i < 1000; i++) {
                String documento = "DOC" + (i * n / 1000);
                if (pacientes.get(documento) != null) {
                    encontrados++;
                }
            }
            fin = System.nanoTime();
            double tiempoBuscar = (fin - inicio) / 1000000.0;

            // 3. Intentar registrar 1000 pacientes que ya existen
            int rechazados = 0;
            for (int i = 0; i < 1000; i++) {
                String documento = "DOC" + (i * n / 1000);
                if (pacientes.containsKey(documento)) {
                    rechazados++;
                } else {
                    pacientes.put(documento, new Paciente(documento, "Repetido", 1, -1));
                }
            }

            // 4. Recorrer los pacientes en orden de llegada
            inicio = System.nanoTime();
            int contador = 0;
            for (Paciente p : pacientes.values()) {
                contador++;
            }
            fin = System.nanoTime();
            double tiempoRecorrer = (fin - inicio) / 1000000.0;

            // Comparacion: buscar los mismos 1000 pacientes en una LinkedList
            LinkedList<Paciente> lista = new LinkedList<>();
            for (Paciente p : pacientes.values()) {
                lista.add(p);
            }
            inicio = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                String documento = "DOC" + (i * n / 1000);
                for (Paciente p : lista) {
                    if (p.documento.equals(documento)) {
                        break;
                    }
                }
            }
            fin = System.nanoTime();
            double tiempoBuscarLista = (fin - inicio) / 1000000.0;

            System.out.println("Tiempo registrar: " + tiempoRegistrar + " ms");
            System.out.println("Tiempo buscar 1000: " + tiempoBuscar + " ms");
            System.out.println("Duplicados rechazados: " + rechazados);
            System.out.println("Tiempo recorrer en orden: " + tiempoRecorrer + " ms");
            System.out.println("Memoria usada: " + memoriaMB + " MB");
            System.out.println("Tiempo buscar 1000 con LinkedList: " + tiempoBuscarLista + " ms");
            System.out.println();
        }

        // Pacientes graves: cola de prioridad
        System.out.println("----- Atencion por gravedad (PriorityQueue) -----");
        PriorityQueue<Paciente> urgencias = new PriorityQueue<>();
        urgencias.add(new Paciente("101", "Ana", 2, 1));
        urgencias.add(new Paciente("102", "Luis", 5, 2));
        urgencias.add(new Paciente("103", "Marta", 3, 3));
        urgencias.add(new Paciente("104", "Pedro", 5, 4));
        urgencias.add(new Paciente("105", "Sofia", 1, 5));

        while (!urgencias.isEmpty()) {
            Paciente p = urgencias.poll();
            System.out.println("Se atiende a " + p.nombre + " - gravedad " + p.gravedad + " - llego de " + p.ordenLlegada);
        }
    }
}
