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

    @Autowired
    private ProductosServiceRest productosServiceRest;

    @PostMapping(value = "/in")
    @ResponseBody
    public ResponseEntity<Map> in(@RequestBody UsuarioRequest usuarioRequest)  {
        Map respuesta = new HashMap();

        UsuarioResponse usR = loginServiceRest.validarCredenciales(usuarioRequest);

        if(usR!=null) {
            respuesta.put("usuario", usR.getAcceso());
            respuesta.put("nombre",usR.getNombre());
            respuesta.put("ctacte",usR.getCtacte());
            respuesta.put("productosPriv",usR.getProductosPriv());
            respuesta.put("token",usR.getToken());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
        else{
            respuesta.put("usuario", null);
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/upd")
    @ResponseBody
    public ResponseEntity<Map> upd(@RequestBody ProductoRequest productoRequest)  {
        Map respuesta = new HashMap();
        boolean resp = productosServiceRest.actualizarProducto(productoRequest);
        if(resp) {
            respuesta.put("resp", true);
            respuesta.put("msj","Producto actualizad!");
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
        else{
            respuesta.put("resp", null);
            respuesta.put("msj", "No se pudo actualizar");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

}
