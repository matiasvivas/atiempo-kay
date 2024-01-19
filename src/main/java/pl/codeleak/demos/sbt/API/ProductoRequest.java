package pl.codeleak.demos.sbt.API;

public class ProductoRequest {
    String codigo;
    Float precioNuevo;
    Float stockNuevo;
    String token;
    String username;
    public ProductoRequest() {
    }
    public ProductoRequest(String codigo, Float precioNuevo, Float stockNuevo, String token, String username) {
        this.codigo = codigo;
        this.precioNuevo = precioNuevo;
        this.stockNuevo = stockNuevo;
        this.token = token;
        this.username = username;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Float getPrecioNuevo() {
        return precioNuevo;
    }
    public void setPrecioNuevo(Float precioNuevo) {
        this.precioNuevo = precioNuevo;
    }
    public Float getStockNuevo() {
        return stockNuevo;
    }
    public void setStockNuevo(Float stockNuevo) {
        this.stockNuevo = stockNuevo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
