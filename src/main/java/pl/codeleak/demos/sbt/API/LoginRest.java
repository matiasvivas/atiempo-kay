package pl.codeleak.demos.sbt.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/loginrest")
public class LoginRest {

    @Autowired
    private LoginServiceRest loginServiceRest;

    @PostMapping(value = "/in")
    @ResponseBody
    public ResponseEntity<Map> in(@RequestBody UsuarioRequest usuarioRequest)  {
        Map respuesta = new HashMap();

        UsuarioResponse usR = loginServiceRest.validarCredenciales(usuarioRequest);

        if(usR!=null) {
            respuesta.put("usuario", usR.getAcceso());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
        else{
            respuesta.put("usuario", null);
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

}
