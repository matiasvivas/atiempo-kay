package pl.codeleak.demos.sbt.API;

import com.mysql.cj.xdevapi.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.codeleak.demos.sbt.APIToken.TokenGenerator;
import pl.codeleak.demos.sbt.APIToken.TokenValidator;
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.repository.ProductosRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ProductosServiceImplRest implements ProductosServiceRest{

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private ProductosRepository productosRepository;

    public boolean actualizarProducto(ProductoRequest productoRequest) {
        //el username es el numero de documento del cliente
        boolean isValid = this.validarToken(productoRequest.getUsername(), productoRequest.getToken());
        Cliente cliente = clientesRepository.findFirstByNumeroDocumento(productoRequest.getUsername());
        if (cliente != null && cliente.getActivo() && cliente.getNumeroDocumento() != null && isValid) {
            //permite actualizar el producto
            Producto productoBase = productosRepository.findFirstByCodigo(productoRequest.getCodigo());
            if (productoBase != null) {
                  //Se adjunta la fecha de creacion y de usuario que define el stock
                productoBase.setFechaActualizacionStock(Utiles.obtenerFechaYHoraActualDate());
                productoBase.setFechaActualizacionPrecio(Utiles.obtenerFechaYHoraActualDate());
                productoBase.setUsuarioActualizacionStock("MOBILE");
                if (productoRequest.getStockNuevo() != null) {
                    productoBase.setExistencia(productoRequest.getStockNuevo());
                }
                if (productoRequest.getPrecioNuevo() != null) {
                    productoBase.setPrecio(productoRequest.getPrecioNuevo());
                }
                productosRepository.save(productoBase);
                return true;
            }
            else{return false;}
        }
        else{return false;}
    }
    private boolean validarToken(String s, String generatedToken) {
        String secretKey = s+"kayadmin";
        String receivedToken = generatedToken;

        TokenValidator tokenValidator = new TokenValidator(secretKey);
        if (tokenValidator.validateToken(generatedToken, receivedToken)) {
            System.out.println("Token válido");
            return true;
        } else {
            System.out.println("Token inválido");
            return false;
        }
    }
}
