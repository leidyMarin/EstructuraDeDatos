public class Producto {

    String codigo;
    String nombre;
    int precio;         // en pesos
    String categoria;

    public Producto(String codigo, String nombre, int precio, String categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
}
