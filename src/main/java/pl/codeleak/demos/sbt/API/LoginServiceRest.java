package pl.codeleak.demos.sbt.API;

public interface LoginServiceRest {
    UsuarioResponse validarCredenciales(UsuarioRequest usuarioRequest);
}
