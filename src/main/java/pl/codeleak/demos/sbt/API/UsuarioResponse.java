package pl.codeleak.demos.sbt.API;

import pl.codeleak.demos.sbt.model.Venta;

import java.util.List;

public class UsuarioResponse {
    private boolean acceso;
    private String nombre;
    private List<VentaResponse> ctacte;

    private List<ProductoResponse> productosPriv;
    public UsuarioResponse(){}
    public UsuarioResponse(boolean acceso, String nombre, List<VentaResponse> ctacte, List<ProductoResponse> productosPriv) {
        this.acceso = acceso;
        this.nombre = nombre;
        this.ctacte = ctacte;
        this.productosPriv = productosPriv;
    }
    public boolean getAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<VentaResponse> getCtacte() {
        return ctacte;
    }

    public void setCtacte(List<VentaResponse> ctacte) {
        this.ctacte = ctacte;
    }

    public List<ProductoResponse> getProductosPriv() {
        return productosPriv;
    }

    public void setProductosPriv(List<ProductoResponse> productosPriv) {
        this.productosPriv = productosPriv;
    }
}
