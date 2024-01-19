package pl.codeleak.demos.sbt.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.codeleak.demos.sbt.APIToken.TokenGenerator;
import pl.codeleak.demos.sbt.enumeradores.Estado;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.ProductoVendido;
import pl.codeleak.demos.sbt.model.Venta;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.repository.ProductosRepository;
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

    @Autowired
    private ProductosRepository productosRepository;

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
                //productos privados
                if(cliente.getNumeroDocumento().equals("35441563")||cliente.getNumeroDocumento().equals("40406688")){
                    List<Producto> productosP = productosRepository.obtenerProductosPrivadosMobile();
                    List<ProductoResponse> productosPrivadosConvertidos = convertirProductosMobile(productosP);
                    usuarioResponse.setProductosPriv(productosPrivadosConvertidos);
                    //se agrega el token para validar los update
                    usuarioResponse.setToken(generarToken(cliente.getNumeroDocumento()));
                }
                else{
                    usuarioResponse.setProductosPriv(null);
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

    private String generarToken(String s) {
        String secretKey = s+"kayadmin";
        TokenGenerator tokenGenerator = new TokenGenerator(secretKey);
        String generatedToken = tokenGenerator.generateToken();
        return generatedToken;
    }

    private List<ProductoResponse> convertirProductosMobile(List<Producto> productosP) {
        List<ProductoResponse> productosPResponse = new ArrayList<>();
        for(Producto producto:productosP){
            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(producto.getCodigo());
            productoResponse.setPrecio(producto.getPrecio());
            productoResponse.setStock(producto.getExistencia());
            productoResponse.setNombre(producto.getNombre());
            productosPResponse.add(productoResponse);
        }
        return productosPResponse;
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
