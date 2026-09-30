public class Paciente implements Comparable<Paciente> {

    String documento;
    String nombre;
    int gravedad;      // 1 = leve, 5 = muy grave
    int ordenLlegada;

    public Paciente(String documento, String nombre, int gravedad, int ordenLlegada) {
        this.documento = documento;
        this.nombre = nombre;
        this.gravedad = gravedad;
        this.ordenLlegada = ordenLlegada;
    }

    // Para la cola de prioridad: primero el mas grave,
    // y si tienen la misma gravedad, el que llego primero
    public int compareTo(Paciente otro) {
        if (this.gravedad != otro.gravedad) {
            return otro.gravedad - this.gravedad;
        }
        return this.ordenLlegada - otro.ordenLlegada;
    }
}
