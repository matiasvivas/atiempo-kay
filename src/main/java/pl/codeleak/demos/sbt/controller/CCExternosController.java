package pl.codeleak.demos.sbt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codeleak.demos.sbt.enumeradores.TipoCodigo;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/ccexternos")
public class CCExternosController {

    @Autowired
    UserService userService;
    @Autowired
    CodigosRepository codigosRepository;
    @Autowired
    VentasRepository ventasRepository;
    @Autowired
    ClientesRepository clientesRepository;

    @GetMapping(value = "/")
    public ModelAndView cargadorInicial(Model model) {

        model.addAttribute("codigo", new Cliente());
        model.addAttribute("codigos", codigosRepository.findAll());

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);
        modelAndView.setViewName("codigo/ver");
        return modelAndView;
    }

    @GetMapping(value = "/agregar")
    public String agregar(Model model) {
        model.addAttribute("codigo", new Cliente());
        model.addAttribute("tipos", TipoCodigo.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "codigo/agregar";
    }

    @GetMapping(value = "/mostrar")
    public String mostrar(Model model) {
        model.addAttribute("codigos", codigosRepository.findAll());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "codigo/ver";
    }

    @PostMapping(value = "/eliminar")
    public String eliminar(@ModelAttribute Codigo codigo, RedirectAttributes redirectAttrs) {
        redirectAttrs
            .addFlashAttribute("mensaje", "Eliminado correctamente")
            .addFlashAttribute("clase", "warning");
        codigosRepository.deleteById(codigo.getId());
        return "redirect:/codigos/mostrar";
    }

    @PostMapping(value = "/editar/{id}")
    public String actualizar(@ModelAttribute @Valid Codigo codigo, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            if (codigo.getId() != null) {
                return "codigo/editar";
            }
            return "redirect:/codigos/mostrar";
        }
        Codigo posibleCodigoExistente = codigosRepository.findFirstBySerial(codigo.getSerial());

        if (posibleCodigoExistente != null && !posibleCodigoExistente.getId().equals(codigo.getId())) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un codigo con ese numero de serial")
                .addFlashAttribute("clase", "warning");
            return "redirect:/codigos/agregar";
        }

        codigosRepository.save(codigo);
        redirectAttrs
            .addFlashAttribute("mensaje", "Editado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/codigos/mostrar";
    }

    @GetMapping(value = "/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        model.addAttribute("codigo", codigosRepository.findById(id).orElse(null));
        model.addAttribute("tipos", TipoCodigo.values());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "codigo/editar";
    }

    @PostMapping(value = "/agregar")
    public String guardar(@ModelAttribute @Valid Codigo codigo, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "codigo/agregar";
        }
        if (codigosRepository.findFirstBySerial(codigo.getSerial()) != null) {
            redirectAttrs
                .addFlashAttribute("mensaje", "Ya existe un codigo con ese numero de serial")
                .addFlashAttribute("clase", "warning");
            return "redirect:/codigos/agregar";
        }

        codigosRepository.save(codigo);
        redirectAttrs
            .addFlashAttribute("mensaje", "Agregado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/codigos/agregar";
    }

    @GetMapping(value = "/compras")
    public String mostrarCompras(Model model) {

        Integer cliente = 52;
        model.addAttribute("ventas", ventasRepository.mostrarVentasCuentasCorrientesImpagasPorCliente(cliente));

        List<Cliente> clientesActivosYAprobados = clientesRepository.findClientesActivosYAprobadosCuentaCorrienteOrderByAZ();
        if(clientesActivosYAprobados==null){clientesActivosYAprobados = new ArrayList<>();}
        model.addAttribute("clientesCuentaCorriente", clientesActivosYAprobados);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        model.addAttribute("porcentajeRecargo",10);
        model.addAttribute("porcentajeDescuento",0);

        return "ccexternos/vercompras";
    }
}
