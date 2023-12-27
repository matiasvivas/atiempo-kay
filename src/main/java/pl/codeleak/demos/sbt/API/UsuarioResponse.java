package pl.codeleak.demos.sbt.API;

public class UsuarioResponse {
    private boolean acceso;
    public UsuarioResponse(){}
    public UsuarioResponse(boolean acceso) {
        this.acceso = acceso;
    }

    public boolean getAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }
}
