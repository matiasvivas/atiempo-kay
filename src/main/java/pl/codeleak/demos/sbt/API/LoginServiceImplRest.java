package pl.codeleak.demos.sbt.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.codeleak.demos.sbt.enumeradores.Estado;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.model.ProductoVendido;
import pl.codeleak.demos.sbt.model.Venta;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class LoginServiceImplRest implements LoginServiceRest{

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private CuentasCorrientesRepository cuentasCorrientesRepository;

    public UsuarioResponse validarCredenciales(UsuarioRequest usuarioRequest)  {
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        Cliente cliente = clientesRepository.findFirstByNumeroDocumento(usuarioRequest.getUsername());

        if(cliente!=null&&cliente.getActivo()&&cliente.getNumeroDocumento()!=null&&cliente.getPasswordMobile()!=null){
            if(cliente.getPasswordMobile().equals(usuarioRequest.getPassword())) {
                usuarioResponse.setAcceso(true);
                usuarioResponse.setNombre(cliente.getNombre());
                if(cliente.getCuentaCorriente()) {
                    List<Venta> ctacte = cuentasCorrientesRepository.obtenerDeudasDeCliente(cliente.getId());
                    List<VentaResponse> deudaConvertida = convertirDeudaMobile(ctacte);
                    usuarioResponse.setCtacte(deudaConvertida);
                }
                else{
                    usuarioResponse.setCtacte(null);
                }
            }
            else {
                usuarioResponse.setAcceso(false);
            }
        }
        else {
            usuarioResponse.setAcceso(false);
        }
        return usuarioResponse;
    }

    private List<VentaResponse> convertirDeudaMobile(List<Venta> ctacte) {
        List<VentaResponse> ctacteResponse = new ArrayList<>();
        for(Venta venta:ctacte){
            VentaResponse ventaResponse = new VentaResponse();
            ventaResponse.setFechaHora(venta.getFechaYHora());
            // Acceder a la lista de productos vendidos dentro de cada venta
            Set<ProductoVendido> productosVendidos = venta.getProductos();

            String detalle = "";
            for(ProductoVendido pv:productosVendidos){
                detalle+= pv.getNombre()+" - ";
            }
            ventaResponse.setDetalle(detalle);
            ventaResponse.setMontoPendiente(venta.getPagoCuentaCorriente());
            ctacteResponse.add(ventaResponse);
        }
        return ctacteResponse;
    }
}
