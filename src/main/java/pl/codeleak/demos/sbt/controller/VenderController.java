package pl.codeleak.demos.sbt.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.ProductosRepository;
import pl.codeleak.demos.sbt.repository.ProductosVendidosRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/vender")
public class VenderController {
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private VentasRepository ventasRepository;
    @Autowired
    private ProductosVendidosRepository productosVendidosRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private ClientesRepository clientesRepository;

    @PostMapping(value = "/quitar/{indice}")
    public String quitarDelCarrito(@ModelAttribute Producto producto, @PathVariable int indice, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            carrito.remove(indice);
            this.guardarCarrito(carrito, request);
        }

        //agregado monto pollo
        if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
            String montoPR = producto.getPrecio().toString();
            String[] montoPRVector = montoPR.split("\\.");
            montoPR = montoPRVector[0];
            redirectAttrs.addFlashAttribute("precioBackend", montoPR);
        }
        //fin agregado monto pollo

        //agregado monto fiambre
        if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
            String montoFR = producto.getExistencia().toString();
            String[] montoFRVector = montoFR.split("\\.");
            montoFR = montoFRVector[0];
            redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
        }
        //fin agregado monto fiambre

        //agregado monto otros
        if(producto.getNombre()!=null&&producto.getNombre()!="") {
            String montoOR = producto.getNombre().toString();
            String[] montoORVector = montoOR.split("\\.");
            montoOR = montoORVector[0];
            redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
        }
        //fin agregado monto otros

        return "redirect:/vender/";
    }

    @PostMapping(value = "/aumentarUnProducto/{indice}")
    public String aumentarUnProducto(@ModelAttribute Producto producto, @PathVariable int indice, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            Producto productoBuscadoPorCodigo = productosRepository.findFirstByCodigo(carrito.get(indice).getCodigo());
            boolean superaStock = false;
            for (ProductoParaVender productoParaVenderActual : carrito) {
                if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                    if(productoParaVenderActual.getCantidad()>=productoParaVenderActual.getExistencia())
                    {superaStock = true;
                        break;}
                    else{
                        productoParaVenderActual.aumentarCantidad();
                    }
                    break;
                }
            }

            if (superaStock) {

                //agregado monto pollo
                if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
                    String montoPR = producto.getPrecio().toString();
                    String[] montoPRVector = montoPR.split("\\.");
                    montoPR = montoPRVector[0];
                    redirectAttrs.addFlashAttribute("precioBackend", montoPR);
                }
                //fin agregado monto pollo

                //agregado monto fiambre
                if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
                    String montoFR = producto.getExistencia().toString();
                    String[] montoFRVector = montoFR.split("\\.");
                    montoFR = montoFRVector[0];
                    redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
                }
                //fin agregado monto fiambre

                //agregado monto otros
                if(producto.getNombre()!=null&&producto.getNombre()!="") {
                    String montoOR = producto.getNombre().toString();
                    String[] montoORVector = montoOR.split("\\.");
                    montoOR = montoORVector[0];
                    redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
                }
                //fin agregado monto otros

                redirectAttrs
                        .addFlashAttribute("mensaje", "El producto con el código " + productoBuscadoPorCodigo.getCodigo() + " supera la cantidad disponible")
                        .addFlashAttribute("clase", "warning");
                return "redirect:/vender/";
            }

            this.guardarCarrito(carrito, request);
        }

        //agregado monto pollo
        if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
            String montoPR = producto.getPrecio().toString();
            String[] montoPRVector = montoPR.split("\\.");
            montoPR = montoPRVector[0];
            redirectAttrs.addFlashAttribute("precioBackend", montoPR);
        }
        //fin agregado monto pollo

        //agregado monto fiambre
        if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
            String montoFR = producto.getExistencia().toString();
            String[] montoFRVector = montoFR.split("\\.");
            montoFR = montoFRVector[0];
            redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
        }
        //fin agregado monto fiambre

        //agregado monto otros
        if(producto.getNombre()!=null&&producto.getNombre()!="") {
            String montoOR = producto.getNombre().toString();
            String[] montoORVector = montoOR.split("\\.");
            montoOR = montoORVector[0];
            redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
        }
        //fin agregado monto otros

        return "redirect:/vender/";
    }

    @PostMapping(value = "/disminuirUnProducto/{indice}")
    public String disminuirUnProducto(@ModelAttribute Producto producto, @PathVariable int indice, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            Producto productoBuscadoPorCodigo = productosRepository.findFirstByCodigo(carrito.get(indice).getCodigo());
            boolean minimoPermitido = false;
            for (ProductoParaVender productoParaVenderActual : carrito) {
                if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                    if(productoParaVenderActual.getCantidad()<=1)
                    {minimoPermitido = true;
                        break;}
                    else{
                        productoParaVenderActual.disminuirCantidad();
                    }
                    break;
                }
            }

            if (minimoPermitido) {

                //agregado monto pollo
                if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
                    String montoPR = producto.getPrecio().toString();
                    String[] montoPRVector = montoPR.split("\\.");
                    montoPR = montoPRVector[0];
                    redirectAttrs.addFlashAttribute("precioBackend", montoPR);
                }
                //fin agregado monto pollo

                //agregado monto fiambre
                if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
                    String montoFR = producto.getExistencia().toString();
                    String[] montoFRVector = montoFR.split("\\.");
                    montoFR = montoFRVector[0];
                    redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
                }
                //fin agregado monto fiambre

                //agregado monto otros
                if(producto.getNombre()!=null&&producto.getNombre()!="") {
                    String montoOR = producto.getNombre().toString();
                    String[] montoORVector = montoOR.split("\\.");
                    montoOR = montoORVector[0];
                    redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
                }
                //fin agregado monto otros

                redirectAttrs
                        .addFlashAttribute("mensaje", "No puede vender cero producto.")
                        .addFlashAttribute("clase", "warning");
                return "redirect:/vender/";
            }

            this.guardarCarrito(carrito, request);
        }

        //agregado monto pollo
        if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
            String montoPR = producto.getPrecio().toString();
            String[] montoPRVector = montoPR.split("\\.");
            montoPR = montoPRVector[0];
            redirectAttrs.addFlashAttribute("precioBackend", montoPR);
        }
        //fin agregado monto pollo

        //agregado monto fiambre
        if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
            String montoFR = producto.getExistencia().toString();
            String[] montoFRVector = montoFR.split("\\.");
            montoFR = montoFRVector[0];
            redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
        }
        //fin agregado monto fiambre

        //agregado monto otros
        if(producto.getNombre()!=null&&producto.getNombre()!="") {
            String montoOR = producto.getNombre().toString();
            String[] montoORVector = montoOR.split("\\.");
            montoOR = montoORVector[0];
            redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
        }
        //fin agregado monto otros

        return "redirect:/vender/";
    }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new ArrayList<>(), request);
    }

    @GetMapping(value = "/limpiar")
    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        this.limpiarCarrito(request);
        redirectAttrs
                .addFlashAttribute("mensaje", "Venta cancelada")
                .addFlashAttribute("clase", "info");
        return "redirect:/vender/";
    }

    @PostMapping(value = "/terminar")
    public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        // Si no hay carrito o está vacío, regresamos inmediatamente
        if (carrito == null || carrito.size() <= 0) {
            return "redirect:/vender/";
        }

        Venta v = ventasRepository.save(new Venta());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        v.setUsername(user.getUserName());

        // Recorrer el carrito
        for (ProductoParaVender productoParaVender : carrito) {
            // Obtener el producto fresco desde la base de datos
            //Producto p = productosRepository.findById(productoParaVender.getId()).orElse(null);
            Producto p = productosRepository.findFirstById(productoParaVender.getId());
            if (p == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le restamos existencia
            p.restarExistencia(productoParaVender.getCantidad());
            // Lo guardamos con la existencia ya restada
            productosRepository.save(p);
            // Creamos un nuevo producto que será el que se guarda junto con la venta
            ProductoVendido productoVendido = new ProductoVendido(productoParaVender.getCantidad(), productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
            // Y lo guardamos
            productosVendidosRepository.save(productoVendido);

            ventasRepository.updateFechaYHora(this.obtenerFechaYHoraARGENTINA(),v.getId());
        }

        // Al final limpiamos el carrito
        this.limpiarCarrito(request);
        // e indicamos una venta exitosa
        redirectAttrs
                .addFlashAttribute("mensaje", "Venta realizada correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/vender/";
    }

    @GetMapping(value = "/")
    public String interfazVender(Model model, HttpServletRequest request) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("listaDeProductos", productosRepository.obtenerProductosVendibles());
        float total = 0;
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        for (ProductoParaVender p: carrito) total += p.getTotal();
        model.addAttribute("total", total);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        List<Cliente> clientesActivosYAprobados = clientesRepository.findClientesActivosYAprobadosCuentaCorrienteOrderByAZ();
        if(clientesActivosYAprobados==null){clientesActivosYAprobados = new ArrayList<>();}
        model.addAttribute("clientesCuentaCorriente", clientesActivosYAprobados);

        return "vender/vender";
    }

    private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoParaVender> carrito = (ArrayList<ProductoParaVender>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    @PostMapping(value = "/agregar")
    public String agregarAlCarrito(@ModelAttribute Producto producto,
                                   HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        Producto productoBuscadoPorCodigo = productosRepository.findFirstByCodigo(producto.getCodigo());
        if (productoBuscadoPorCodigo == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/vender/";
        }

        boolean superaStock = false;
        for (ProductoParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                if(productoParaVenderActual.getCantidad()>=productoParaVenderActual.getExistencia())
                {superaStock = true;
                break;}
            }
        }
        if (superaStock) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " supera la cantidad disponible")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/vender/";
        }

        if (productoBuscadoPorCodigo.sinExistencia()) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto está agotado")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/vender/";
        }

        boolean encontrado = false;
        for (ProductoParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carrito.add(new ProductoParaVender(productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getPrecio(), productoBuscadoPorCodigo.getExistencia(), productoBuscadoPorCodigo.getId(), 1f));
        }
        this.guardarCarrito(carrito, request);
        return "redirect:/vender/";
    }

    @PostMapping(value = "/agregarProductos")
    public String agregarAlCarritoProductos(@ModelAttribute Producto producto,
                                   HttpServletRequest request, RedirectAttributes redirectAttrs) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        Producto productoBuscadoPorCodigo = productosRepository.findFirstByCodigo(producto.getCodigo());
        if (productoBuscadoPorCodigo == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " no existe")
                    .addFlashAttribute("clase", "warning");
            //agregado monto pollo
            if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
                String montoPR = producto.getPrecio().toString();
                String[] montoPRVector = montoPR.split("\\.");
                montoPR = montoPRVector[0];
                redirectAttrs.addFlashAttribute("precioBackend", montoPR);
            }
            //fin agregado monto pollo

            //agregado monto fiambre
            if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
                String montoFR = producto.getExistencia().toString();
                String[] montoFRVector = montoFR.split("\\.");
                montoFR = montoFRVector[0];
                redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
            }
            //fin agregado monto fiambre

            //agregado monto otros
            if(producto.getNombre()!=null&&producto.getNombre()!="") {
                String montoOR = producto.getNombre().toString();
                String[] montoORVector = montoOR.split("\\.");
                montoOR = montoORVector[0];
                redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
            }
            //fin agregado monto otros
            return "redirect:/vender/";
        }

        boolean superaStock = false;
        for (ProductoParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                if(productoParaVenderActual.getCantidad()>=productoParaVenderActual.getExistencia())
                {superaStock = true;
                    break;}
            }
        }
        if (superaStock) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " supera la cantidad disponible")
                    .addFlashAttribute("clase", "warning");
            //agregado monto pollo
            if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
                String montoPR = producto.getPrecio().toString();
                String[] montoPRVector = montoPR.split("\\.");
                montoPR = montoPRVector[0];
                redirectAttrs.addFlashAttribute("precioBackend", montoPR);
            }
            //fin agregado monto pollo

            //agregado monto fiambre
            if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
                String montoFR = producto.getExistencia().toString();
                String[] montoFRVector = montoFR.split("\\.");
                montoFR = montoFRVector[0];
                redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
            }
            //fin agregado monto fiambre

            //agregado monto otros
            if(producto.getNombre()!=null&&producto.getNombre()!="") {
                String montoOR = producto.getNombre().toString();
                String[] montoORVector = montoOR.split("\\.");
                montoOR = montoORVector[0];
                redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
            }
            //fin agregado monto otros
            return "redirect:/vender/";
        }


        if (productoBuscadoPorCodigo.sinExistencia()) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El producto está agotado")
                    .addFlashAttribute("clase", "warning");
            //agregado monto pollo
            if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
                String montoPR = producto.getPrecio().toString();
                String[] montoPRVector = montoPR.split("\\.");
                montoPR = montoPRVector[0];
                redirectAttrs.addFlashAttribute("precioBackend", montoPR);
            }
            //fin agregado monto pollo

            //agregado monto fiambre
            if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
                String montoFR = producto.getExistencia().toString();
                String[] montoFRVector = montoFR.split("\\.");
                montoFR = montoFRVector[0];
                redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
            }
            //fin agregado monto fiambre

            //agregado monto otros
            if(producto.getNombre()!=null&&producto.getNombre()!="") {
                String montoOR = producto.getNombre().toString();
                String[] montoORVector = montoOR.split("\\.");
                montoOR = montoORVector[0];
                redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
            }
            //fin agregado monto otros
            return "redirect:/vender/";
        }

        boolean encontrado = false;
        for (ProductoParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            if(validarValeDevolucionEnvase(productoBuscadoPorCodigo.getCodigo())){
            productoBuscadoPorCodigo.setPrecio(Float.parseFloat("-100"));
            }
            if(validarValeDevolucionEnvase200(productoBuscadoPorCodigo.getCodigo())){
                productoBuscadoPorCodigo.setPrecio(Float.parseFloat("-200"));
            }
            carrito.add(new ProductoParaVender(productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getPrecio(), productoBuscadoPorCodigo.getExistencia(), productoBuscadoPorCodigo.getId(), 1f));
        }
        this.guardarCarrito(carrito, request);
        //agregado monto pollo
        if(producto.getPrecio()!=null&&producto.getPrecio()>0) {
            String montoPR = producto.getPrecio().toString();
            String[] montoPRVector = montoPR.split("\\.");
            montoPR = montoPRVector[0];
            redirectAttrs.addFlashAttribute("precioBackend", montoPR);
        }
        //fin agregado monto pollo

        //agregado monto fiambre
        if(producto.getExistencia()!=null&&producto.getExistencia()>0) {
            String montoFR = producto.getExistencia().toString();
            String[] montoFRVector = montoFR.split("\\.");
            montoFR = montoFRVector[0];
            redirectAttrs.addFlashAttribute("existenciaBackend", montoFR);
        }
        //fin agregado monto fiambre

        //agregado monto otros
        if(producto.getNombre()!=null&&producto.getNombre()!="") {
            String montoOR = producto.getNombre().toString();
            String[] montoORVector = montoOR.split("\\.");
            montoOR = montoORVector[0];
            redirectAttrs.addFlashAttribute("nombreBackend", montoOR);
        }
        //fin agregado monto otros
        return "redirect:/vender/";
    }

    @PostMapping(value = "/cobrar/{precio}")
    public String cobrarVenta(@ModelAttribute(value = "producto") Producto producto,
                              @PathVariable String precio,Model model, HttpServletRequest request, RedirectAttributes redirectAttrs) {

        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        // Si no hay carrito o está vacío y ademas no hay ni monto pollo, ni monto
        // fiambre ni otros montos, regresamos inmediatamente
        if ((carrito == null || carrito.size() <= 0)
                && (producto.getCodigo()==null||producto.getCodigo().equals(0)||producto.getCodigo().equals("")) //Otros montos
                && (producto.getPrecio()==null||producto.getPrecio()<=0) //Monto Pollo
                && (producto.getExistencia()==null||producto.getExistencia()<=0) //Monto Fiambre
        )
        {
            return "redirect:/vender/";
        }

        // Si no hay carrito o está vacío pero hay monto fiambre, monto pollo o otros montos,
        // realizamos solo la operación de estos montos
        if ((carrito == null || carrito.size() <= 0)
                && (producto.getPrecio()!=null) //Monto Pollo
                && (producto.getExistencia()!=null) //Monto Fiambre
                && (producto.getCodigo()!=null&&producto.getCodigo()!="") //Otros montos
        )
        {
            Venta v = ventasRepository.save(new Venta());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            v.setUsername(user.getUserName());
            if(producto.getNombre()!=null&&!producto.getNombre().isEmpty()) {
                String mediosDePagoSplit[] = producto.getNombre().split(";");
                if(mediosDePagoSplit[0]!=null&&!mediosDePagoSplit[0].isEmpty()) {
                    v.setPagoEfectivo(Float.parseFloat(mediosDePagoSplit[0]));
                }
                if(mediosDePagoSplit[1]!=null&&!mediosDePagoSplit[1].isEmpty()){
                    v.setPagoDigital(Float.parseFloat(mediosDePagoSplit[1]));
                }
                if(mediosDePagoSplit[2]!=null){
                    String splitCuentaCorriente[] = mediosDePagoSplit[2].split(",");
                    if(splitCuentaCorriente[0]!=null&&!splitCuentaCorriente[0].isEmpty()) {
                        v.setPagoCuentaCorriente(Float.parseFloat(splitCuentaCorriente[0]));
                    }
                    if(splitCuentaCorriente[1]!=null&&!splitCuentaCorriente[1].isEmpty()&&!splitCuentaCorriente[1].equals("Seleccione el cliente")) {
                        v.setClienteCuentaCorriente(Integer.parseInt(splitCuentaCorriente[1]));
                    }
                }
            }

            if(producto.getPrecio()!=null) {
                //El codigo por defecto del pollo es 9999 siempre internamente aunque no este en stock
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),producto.getPrecio(),"Polleria","9999",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            if(producto.getExistencia()!=null) {
                //El codigo por defecto de fiambres y quesos es 8888 siempre internamente aunque no este en stock
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),producto.getExistencia(),"Fiambres y quesos","8888",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            if(producto.getCodigo()!=null&&producto.getCodigo()!="") {
                //El codigo por defecto de otras ventas es 7777 siempre internamente aunque no este en stock ya que pueden ser cargas virtuales, sube, etc
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),Float.parseFloat(producto.getCodigo()),"Otras ventas","7777",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            // e indicamos una venta exitosa
            redirectAttrs
                    .addFlashAttribute("mensaje", "Venta realizada de manera correcta")
                    .addFlashAttribute("clase", "success");
            return "redirect:/vender/";

        }


        if (carrito != null && carrito.size() >= 0)
        {
            Venta v = ventasRepository.save(new Venta());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            v.setUsername(user.getUserName());

            if(producto.getNombre()!=null&&!producto.getNombre().isEmpty()) {
                String mediosDePagoSplit[] = producto.getNombre().split(";");
                if(mediosDePagoSplit[0]!=null&&!mediosDePagoSplit[0].isEmpty()) {
                    v.setPagoEfectivo(Float.parseFloat(mediosDePagoSplit[0]));
                }
                if(mediosDePagoSplit[1]!=null&&!mediosDePagoSplit[1].isEmpty()){
                    v.setPagoDigital(Float.parseFloat(mediosDePagoSplit[1]));
                }
                if(mediosDePagoSplit[2]!=null&&!mediosDePagoSplit[2].isEmpty()){
                    String splitCuentaCorriente[] = mediosDePagoSplit[2].split(",");
                    if(splitCuentaCorriente[0]!=null&&!splitCuentaCorriente[0].isEmpty()) {
                        v.setPagoCuentaCorriente(Float.parseFloat(splitCuentaCorriente[0]));
                    }
                    if(splitCuentaCorriente[1]!=null&&!splitCuentaCorriente[1].isEmpty()&&!splitCuentaCorriente[1].equals("Seleccione el cliente")) {
                        v.setClienteCuentaCorriente(Integer.parseInt(splitCuentaCorriente[1]));
                    }
                }
            }

            // Recorrer el carrito
            for (ProductoParaVender productoParaVender : carrito) {
                // Obtener el producto fresco desde la base de datos
                //Producto p = productosRepository.findById(productoParaVender.getId()).orElse(null);
                Producto p = productosRepository.findFirstById(productoParaVender.getId());
                if (p == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
                // Le restamos existencia
                p.restarExistencia(productoParaVender.getCantidad());


                //Si el producto es una devolución de envase le anulamos la resta de la existencia y le sumamos uno al STOCK
                if(validarValeDevolucionEnvase(p.getCodigo())) {
                    String codigoMellizo = sumarAlMellizoCerveza(p.getCodigo());
                    Producto pMellizo = productosRepository.findFirstByCodigo(codigoMellizo);
                    pMellizo.setExistencia(Float.parseFloat("1"));
                    productosRepository.save(pMellizo);
                }
                if(validarValeDevolucionEnvase200(p.getCodigo())) {
                    String codigoMellizo = sumarAlMellizoCerveza(p.getCodigo());
                    Producto pMellizo = productosRepository.findFirstByCodigo(codigoMellizo);
                    pMellizo.setExistencia(Float.parseFloat("1"));
                    productosRepository.save(pMellizo);
                }
                if(validarValeCobroEntregoEnvase(p.getCodigo())) {
                    String codigoMellizo = restarAlMellizoCerveza(p.getCodigo());
                    Producto pMellizo = productosRepository.findFirstByCodigo(codigoMellizo);
                    pMellizo.setExistencia(Float.parseFloat("1"));
                    productosRepository.save(pMellizo);
                }
                if(validarValeCobroEntregoEnvase200(p.getCodigo())) {
                    String codigoMellizo = restarAlMellizoCerveza(p.getCodigo());
                    Producto pMellizo = productosRepository.findFirstByCodigo(codigoMellizo);
                    pMellizo.setExistencia(Float.parseFloat("1"));
                    productosRepository.save(pMellizo);
                }


                // Lo guardamos con la existencia ya restada
                productosRepository.save(p);
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(productoParaVender.getCantidad(), productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }

            // Al final limpiamos el carrito
            this.limpiarCarrito(request);


            if(producto.getPrecio()!=null) {
                //El codigo por defecto del pollo es 9999 siempre internamente aunque no este en stock
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),producto.getPrecio(),"Polleria","9999",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            if(producto.getExistencia()!=null) {
                //El codigo por defecto de fiambres y quesos es 8888 siempre internamente aunque no este en stock
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),producto.getExistencia(),"Fiambres y quesos","8888",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            if(producto.getCodigo()!=null&&!producto.getCodigo().equals(0)&&!producto.getCodigo().equals("")) {
                //El codigo por defecto de otras ventas es 7777 siempre internamente aunque no este en stock ya que pueden ser cargas virtuales, sube, etc
                // Creamos un nuevo producto que será el que se guarda junto con la venta
                ProductoVendido productoVendido = new ProductoVendido(Float.parseFloat("1"),Float.parseFloat(producto.getCodigo()),"Otras ventas","7777",v);
                // Y lo guardamos
                productosVendidosRepository.save(productoVendido);
            }
            // e indicamos una venta exitosa
            redirectAttrs
                    .addFlashAttribute("mensaje", "Venta realizada correctamente")
                    .addFlashAttribute("clase", "success");
            //return "redirect:/vender/";


        }

        // e indicamos las gracias
        redirectAttrs
                .addFlashAttribute("mensaje", "Muchas Gracias!")
                .addFlashAttribute("clase", "success");
        return "redirect:/vender/";
    }

    private String sumarAlMellizoCerveza(String codigo) {
        String resultado ="";
        if(validarValeDevolucionEnvase(codigo)){
            String sub = codigo.substring(6);
            String subAnalisis = codigo.substring(0,6);
            if(subAnalisis.equals("101020")){
                resultado = "101010"+sub;
            }
            if(subAnalisis.equals("201020")){
                resultado = "201010"+sub;
            }
        }
        if(validarValeDevolucionEnvase200(codigo)){
            String sub = codigo.substring(6);
            String subAnalisis = codigo.substring(0,6);
            if(subAnalisis.equals("101020")){
                resultado = "101010"+sub;
            }
            if(subAnalisis.equals("201020")){
                resultado = "201010"+sub;
            }
        }
        return resultado;
    }

    private String restarAlMellizoCerveza(String codigo) {
        String resultado ="";
        if(validarValeCobroEntregoEnvase(codigo)){
            String sub = codigo.substring(6);
            String subAnalisis = codigo.substring(0,6);
            if(subAnalisis.equals("101010")){
                resultado = "101020"+sub;
            }
            if(subAnalisis.equals("201010")){
                resultado = "201020"+sub;
            }

        }
        if(validarValeCobroEntregoEnvase200(codigo)){
            String sub = codigo.substring(6);
            String subAnalisis = codigo.substring(0,6);
            if(subAnalisis.equals("101010")){
                resultado = "101020"+sub;
            }
            if(subAnalisis.equals("201010")){
                resultado = "201020"+sub;
            }

        }
        return resultado;
    }

    Boolean validarValeCobroEntregoEnvase(String codigoProducto){
        boolean resultado = false;

        switch (codigoProducto){
            //Entrego envases de cerveza 17/06/2022 ingresa dinero
            case "101010295654835013": resultado = true;
            case "101010487632122306": resultado = true;
            case "101010386232104849": resultado = true;
            case "101010378495504291": resultado = true;
            case "101010354749205621": resultado = true;
            case "101010946415658303": resultado = true;
            case "101010382646212306": resultado = true;
            case "101010823018364823": resultado = true;
            case "101010325342382073": resultado = true;
            case "101010328761032980": resultado = true;

            //Entrego envases de gaseosa 17/06/2022 ingresa dinero
            case "201010295324835013": resultado = true;
            case "201010462947631206": resultado = true;
            case "201010323242348649": resultado = true;
            case "201010378146304231": resultado = true;
            case "201010342491324621": resultado = true;
            case "201010946415656040": resultado = true;
            case "201010382621754193": resultado = true;
            case "201010823023249763": resultado = true;
            case "201010325315485493": resultado = true;
            case "201010322648557390": resultado = true;

        }
        return resultado;
    }

    Boolean validarValeCobroEntregoEnvase200(String codigoProducto){
        boolean resultado = false;

        switch (codigoProducto){
                //Entrego envases de cerveza 22/07/2022 ingresa dinero (segunda tanda de 10)
            case "101010294659256013": resultado = true;
            case "101010411184692306": resultado = true;
            case "101010999673804849": resultado = true;
            case "101010867017404291": resultado = true;
            case "101010067846305621": resultado = true;
            case "101010183749258303": resultado = true;
            case "101010899573312306": resultado = true;
            case "101010885732964823": resultado = true;
            case "101010266479382073": resultado = true;
            case "101010783951532980": resultado = true;

                //Entrego envases de cerveza 11/11/2022 ingresa dinero (tercera tanda de 10)
            case "101010295555256013": resultado = true;
            case "101010414444692306": resultado = true;
            case "101010933333804849": resultado = true;
            case "101010822222404291": resultado = true;
            case "101010011111305621": resultado = true;
            case "101010100000258303": resultado = true;
            case "101010899999312306": resultado = true;
            case "101010888888964823": resultado = true;
            case "101010277777382073": resultado = true;
            case "101010666666532980": resultado = true;

                //Entrego envases de gaseosa 22/07/2022 ingresa dinero (segunda tanda de 10)
            case "201010774637283913": resultado = true;
            case "201010867411836406": resultado = true;
            case "201010126408436349": resultado = true;
            case "201010869974521831": resultado = true;
            case "201010154692758521": resultado = true;
            case "201010067412382340": resultado = true;
            case "201010124516342893": resultado = true;
            case "201010955204603263": resultado = true;
            case "201010951806136493": resultado = true;
            case "201010916315793990": resultado = true;

                //Entrego envases de gaseosa 11/11/2022 ingresa dinero (tercera tanda de 10)
            case "201010111111183913": resultado = true;
            case "201010222222236406": resultado = true;
            case "201010333333336349": resultado = true;
            case "201010444444441831": resultado = true;
            case "201010655555558521": resultado = true;
            case "201010766666662340": resultado = true;
            case "201010757677742893": resultado = true;
            case "201010888888603263": resultado = true;
            case "201010999799996493": resultado = true;
            case "201010067689087990": resultado = true;
        }
        return resultado;
    }

    Boolean validarValeDevolucionEnvase(String codigoProducto){
        boolean resultado = false;

        switch (codigoProducto){
            //Recibo envases de cerveza 17/06/2022 sale dinero
            case "101020295654835013": resultado = true;
            case "101020487632122306": resultado = true;
            case "101020386232104849": resultado = true;
            case "101020378495504291": resultado = true;
            case "101020354749205621": resultado = true;
            case "101020946415658303": resultado = true;
            case "101020382646212306": resultado = true;
            case "101020823018364823": resultado = true;
            case "101020325342382073": resultado = true;
            case "101020328761032980": resultado = true;

                //Recibo envases de gaseosa 17/06/2022 sale dinero
            case "201020295324835013": resultado = true;
            case "201020462947631206": resultado = true;
            case "201020323242348649": resultado = true;
            case "201020378146304231": resultado = true;
            case "201020342491324621": resultado = true;
            case "201020946415656040": resultado = true;
            case "201020382621754193": resultado = true;
            case "201020823023249763": resultado = true;
            case "201020325315485493": resultado = true;
            case "201020322648557390": resultado = true;

        }
        return resultado;
    }

    Boolean validarValeDevolucionEnvase200(String codigoProducto){
        boolean resultado = false;

        switch (codigoProducto){

                //Recibo envases de cerveza 22/07/2022 sale dinero (segunda tanda de 10)
            case "101020294659256013": resultado = true;
            case "101020411184692306": resultado = true;
            case "101020999673804849": resultado = true;
            case "101020867017404291": resultado = true;
            case "101020067846305621": resultado = true;
            case "101020183749258303": resultado = true;
            case "101020899573312306": resultado = true;
            case "101020885732964823": resultado = true;
            case "101020266479382073": resultado = true;
            case "101020783951532980": resultado = true;

                //Recibo envases de cerveza 11/11/2022 sale dinero (tercera tanda de 10)
            case "101020295555256013": resultado = true;
            case "101020414444692306": resultado = true;
            case "101020933333804849": resultado = true;
            case "101020822222404291": resultado = true;
            case "101020011111305621": resultado = true;
            case "101020100000258303": resultado = true;
            case "101020899999312306": resultado = true;
            case "101020888888964823": resultado = true;
            case "101020277777382073": resultado = true;
            case "101020666666532980": resultado = true;

                //Recibo envases de gaseosa 22/07/2022 sale dinero (segunda tanda de 10)
            case "201020774637283913": resultado = true;
            case "201020867411836406": resultado = true;
            case "201020126408436349": resultado = true;
            case "201020869974521831": resultado = true;
            case "201020154692758521": resultado = true;
            case "201020067412382340": resultado = true;
            case "201020124516342893": resultado = true;
            case "201020955204603263": resultado = true;
            case "201020951806136493": resultado = true;
            case "201020916315793990": resultado = true;

                //Recibo envases de gaseosa 11/11/2022 sale dinero (tercera tanda de 10)
            case "201020111111183913": resultado = true;
            case "201020222222236406": resultado = true;
            case "201020333333336349": resultado = true;
            case "201020444444441831": resultado = true;
            case "201020655555558521": resultado = true;
            case "201020766666662340": resultado = true;
            case "201020757677742893": resultado = true;
            case "201020888888603263": resultado = true;
            case "201020999799996493": resultado = true;
            case "201020067689087990": resultado = true;
        }
        return resultado;
    }

    Date obtenerFechaYHoraARGENTINA() {
        ZonedDateTime fechaHoraBuenosAires = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        Date fechaYHora = Date.from(fechaHoraBuenosAires.toInstant());
        return fechaYHora;
    }
}
