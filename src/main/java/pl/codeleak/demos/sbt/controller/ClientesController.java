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
import pl.codeleak.demos.sbt.enumeradores.Estado;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/clientes")
public class ClientesController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping(value = "/")
    public ModelAndView cargadorInicial(Model model) {

        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clientesRepository.findAll());

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);
        modelAndView.setViewName("cliente/ver");
        return modelAndView;
    }

    @GetMapping(value = "/agregar")
    public String agregar(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("estados", Estado.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "cliente/agregar";
    }

    @GetMapping(value = "/mostrar")
    public String mostrar(Model model) {
        model.addAttribute("clientes", clientesRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "cliente/ver";
    }

    @PostMapping(value = "/eliminar")
    public String eliminar(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttrs) {
        redirectAttrs
            .addFlashAttribute("mensaje", "Eliminado correctamente")
            .addFlashAttribute("clase", "warning");
        clientesRepository.deleteById(cliente.getId());
        return "redirect:/clientes/mostrar";
    }

    @PostMapping(value = "/editar/{id}")
    public String actualizar(@ModelAttribute @Valid Cliente cliente, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            if (cliente.getId() != null) {
                return "cliente/editar";
            }
            return "redirect:/clientes/mostrar";
        }
        Cliente posibleClienteExistente = clientesRepository.findFirstByNumeroDocumento(cliente.getNumeroDocumento());

        if (posibleClienteExistente != null && !posibleClienteExistente.getId().equals(cliente.getId())) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un cliente con ese numero de documento")
                .addFlashAttribute("clase", "warning");
            return "redirect:/clientes/agregar";
        }

        clientesRepository.save(cliente);
        redirectAttrs
            .addFlashAttribute("mensaje", "Editado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/clientes/mostrar";
    }

    @GetMapping(value = "/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        model.addAttribute("cliente", clientesRepository.findById(id).orElse(null));
        model.addAttribute("estados", Estado.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "cliente/editar";
    }

    @PostMapping(value = "/agregar")
    public String guardar(@ModelAttribute @Valid Cliente cliente, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "cliente/agregar";
        }
        if (clientesRepository.findFirstByNumeroDocumento(cliente.getNumeroDocumento()) != null) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un cliente con ese numero de documento")
                .addFlashAttribute("clase", "warning");
            return "redirect:/clientes/agregar";
        }

        clientesRepository.save(cliente);
        redirectAttrs
            .addFlashAttribute("mensaje", "Agregado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/clientes/agregar";
    }
}
