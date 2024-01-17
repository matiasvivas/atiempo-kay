package pl.codeleak.demos.sbt.API;

public class ProductoResponse {

    private String nombre;
    private String codigo;
    private Float precio;
    private Float stock; //existencia

    public ProductoResponse() {
    }

    public ProductoResponse(String nombre, String codigo, Float precio, Float stock) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getStock() {
        return stock;
    }

    public void setStock(Float stock) {
        this.stock = stock;
    }
}
