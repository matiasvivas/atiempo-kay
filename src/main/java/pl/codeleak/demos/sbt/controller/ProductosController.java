package pl.codeleak.demos.sbt.controller;

import java.util.Calendar;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codeleak.demos.sbt.enumeradores.Categoria;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.model.Utiles;
import pl.codeleak.demos.sbt.repository.ProductosRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/productos")
public class ProductosController {
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/agregar")
    public String agregarProducto(Model model) {
        model.addAttribute("producto", new Producto());

        model.addAttribute("categorias", Categoria.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "productos/agregar_producto";
    }

    @GetMapping(value = "/mostrar")
    public String mostrarProductos(Model model) {
        model.addAttribute("productos", productosRepository.findAllOrderByAZ());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "productos/ver_productos";
    }

    @PostMapping(value = "/eliminar")
    public String eliminarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttrs) {
        redirectAttrs
                .addFlashAttribute("mensaje", "Eliminado correctamente")
                .addFlashAttribute("clase", "warning");
        productosRepository.deleteById(producto.getId());
        return "redirect:/productos/mostrar";
    }

    // Se colocó el parámetro ID para eso de los errores, ya sé el id se puede recuperar
    // a través del modelo, pero lo que yo quiero es que se vea la misma URL para regresar la vista con
    // los errores en lugar de hacer un redirect, ya que si hago un redirect, no se muestran los errores del formulario
    // y por eso regreso mejor la vista ;)
    @PostMapping(value = "/editar/{id}")
    public String actualizarProducto(@ModelAttribute @Valid Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            if (producto.getId() != null) {
                return "productos/editar_producto";
            }
            return "redirect:/productos/mostrar";
        }
        Producto posibleProductoExistente = productosRepository.findFirstByCodigo(producto.getCodigo());

        if (posibleProductoExistente != null && !posibleProductoExistente.getId().equals(producto.getId())) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "Ya existe un producto con ese código")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/productos/agregar";
        }

        //se conserva la prioridad y la fecha de vencimiento
        if(producto.getPrioridad()==null){
        producto.setPrioridad(posibleProductoExistente.getPrioridad());
        }
        if(producto.getFechaVencimiento()==null) {
            producto.setFechaVencimiento(posibleProductoExistente.getFechaVencimiento());
        }

        //se conservan los valores de creacion
        producto.setFechaCreacion(posibleProductoExistente.getFechaCreacion());
        producto.setFechaActualizacionPrecio(posibleProductoExistente.getFechaActualizacionPrecio());
        producto.setFechaActualizacionStock(posibleProductoExistente.getFechaActualizacionStock());
        producto.setUsuarioActualizacionStock(posibleProductoExistente.getUsuarioActualizacionStock());

        if(modificacionDeStock(posibleProductoExistente.getExistencia(),producto.getExistencia())){
            producto.setFechaActualizacionStock(Utiles.obtenerFechaYHoraActualDate());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            producto.setUsuarioActualizacionStock(user.getUserName());
        }
        if(modificacionDePrecio(posibleProductoExistente.getPrecio(),producto.getPrecio())){
            producto.setFechaActualizacionPrecio(Utiles.obtenerFechaYHoraActualDate());
        }

        productosRepository.save(producto);
        redirectAttrs
                .addFlashAttribute("mensaje", "Editado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos/mostrar";
    }

    @GetMapping(value = "/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        //model.addAttribute("producto", productosRepository.findById(id).orElse(null));
        model.addAttribute("producto", productosRepository.findFirstById(id));

        model.addAttribute("categorias", Categoria.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "productos/editar_producto";
    }

    @PostMapping(value = "/agregar")
    public String guardarProducto(@ModelAttribute @Valid Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "productos/agregar_producto";
        }
        if (productosRepository.findFirstByCodigo(producto.getCodigo()) != null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "Ya existe un producto con ese código")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/productos/agregar";
        }

        //Se adjunta la fecha de creacion y de usuario que define el stock
        producto.setFechaCreacion(Utiles.obtenerFechaYHoraActualDate());
        producto.setFechaActualizacionStock(Utiles.obtenerFechaYHoraActualDate());
        producto.setFechaActualizacionPrecio(Utiles.obtenerFechaYHoraActualDate());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        producto.setUsuarioActualizacionStock(user.getUserName());

        productosRepository.save(producto);
        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos/agregar";
    }
    private boolean modificacionDePrecio(Float precioActual, Float nuevoPrecio) {
        if(precioActual != null && nuevoPrecio !=null) {
            int valor = precioActual.compareTo(nuevoPrecio);
            if (valor != 0) {
                return true;
            } else { //si es igual a cero no hubo modificacion de ningun tipo en el precio
                return false;
            }
        }
        else{return false;}
    }
    private boolean modificacionDeStock(Float existenciaActual, Float nuevaExistencia) {
        int valor = existenciaActual.compareTo(nuevaExistencia);
        if(valor!=0){
            return true;
        }
        else {return false;}
    }
}
