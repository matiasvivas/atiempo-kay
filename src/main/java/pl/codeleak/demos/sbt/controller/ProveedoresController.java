package pl.codeleak.demos.sbt.controller;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codeleak.demos.sbt.enumeradores.Categoria;
import pl.codeleak.demos.sbt.model.Proveedor;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.ProveedoresRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/proveedores")
public class ProveedoresController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @GetMapping(value = "/")
    public ModelAndView cargadorInicial(Model model) {

        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("proveedores", proveedoresRepository.findAll());

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);
        modelAndView.setViewName("proveedor/ver");
        return modelAndView;
    }

    @GetMapping(value = "/agregar")
    public String agregar(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("categorias", Categoria.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "proveedor/agregar";
    }

    @GetMapping(value = "/mostrar")
    public String mostrar(Model model) {
        model.addAttribute("proveedores", proveedoresRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "proveedor/ver";
    }

    @PostMapping(value = "/eliminar")
    public String eliminar(@ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttrs) {
        redirectAttrs
            .addFlashAttribute("mensaje", "Eliminado correctamente")
            .addFlashAttribute("clase", "warning");
        proveedoresRepository.deleteById(proveedor.getId());
        return "redirect:/proveedores/mostrar";
    }

    @PostMapping(value = "/editar/{id}")
    public String actualizar(@ModelAttribute @Valid Proveedor proveedor, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            if (proveedor.getId() != null) {
                return "proveedor/editar";
            }
            return "redirect:/proveedores/mostrar";
        }
        Proveedor posibleProveedorExistente = proveedoresRepository.findFirstByNombre(proveedor.getNombre());

        if (posibleProveedorExistente != null && !posibleProveedorExistente.getId().equals(proveedor.getId())) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un proveedor con ese nombre")
                .addFlashAttribute("clase", "warning");
            return "redirect:/proveedores/agregar";
        }

        proveedoresRepository.save(proveedor);
        redirectAttrs
            .addFlashAttribute("mensaje", "Editado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/proveedores/mostrar";
    }

    @GetMapping(value = "/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        model.addAttribute("proveedor", proveedoresRepository.findById(id).orElse(null));
        model.addAttribute("categorias", Categoria.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "proveedor/editar";
    }

    @PostMapping(value = "/agregar")
    public String guardar(@ModelAttribute @Valid Proveedor proveedor, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "proveedor/agregar";
        }
        if (proveedoresRepository.findFirstByNombre(proveedor.getNombre()) != null) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un proveedor con ese nombre")
                .addFlashAttribute("clase", "warning");
            return "redirect:/proveedores/agregar";
        }

        proveedoresRepository.save(proveedor);
        redirectAttrs
            .addFlashAttribute("mensaje", "Agregado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/proveedores/agregar";
    }
}