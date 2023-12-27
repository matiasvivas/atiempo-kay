package pl.codeleak.demos.sbt.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.codeleak.demos.sbt.service.UserService;


@Service
public class LoginServiceImplRest implements LoginServiceRest{

    @Autowired
    private UserService userService;

    public UsuarioResponse validarCredenciales(UsuarioRequest usuarioRequest)  {
        UsuarioResponse usuarioResponse;
        if(usuarioRequest.getUsername().equals("mvivas")){
            usuarioResponse = new UsuarioResponse(true);
        }
        else {
            usuarioResponse = new UsuarioResponse(false);
        }
        return usuarioResponse;
    }
}
