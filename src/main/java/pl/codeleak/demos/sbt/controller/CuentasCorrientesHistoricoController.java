package pl.codeleak.demos.sbt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
@RequestMapping(path = "/historialcuentascorrientes")
public class CuentasCorrientesHistoricoController {

    @Autowired
    private UserService userService;
    @Autowired
    private VentasRepository ventasRepository;
    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping(value = "/")
    public String mostrarVentas(Model model) {

        model.addAttribute("ventas", ventasRepository.mostrarVentasCuentasCorrientesHistorial());

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

        return "cuentascorrientes/historialcuentascorrientes";
    }
}
