package pl.codeleak.demos.sbt.model;

public class ProductoParaVender extends Producto {
    private Float cantidad;

    public ProductoParaVender(String nombre, String codigo, Float precio, Float existencia, Integer id, Float cantidad) {
        super(nombre, codigo, precio, existencia, id);
        this.cantidad = cantidad;
    }

    public ProductoParaVender(String nombre, String codigo, Float precio, Float existencia, Float cantidad) {
        super(nombre, codigo, precio, existencia);
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }

    public void disminuirCantidad() {
        this.cantidad--;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public Float getTotal() {
        Float precio = this.getPrecio();
        if (precio != null) {
            return precio * this.cantidad;
        } else {
            return 0.0f; // O cualquier valor por defecto que desees en caso de que el precio sea nulo
        }
    }
}
