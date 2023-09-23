package pl.codeleak.demos.sbt.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.CajaFuerte;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.IngresosCajaFuerteRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/ingresoscajafuerte")
public class IngresosCajaFuerteController {

    @Autowired
    private UserService userService;

    @Autowired
    IngresosCajaFuerteRepository ingresosCajaFuerteRepository;

    @GetMapping(value = "/")
    public String inicializador(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = new Float(0);
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
            totalCajaFuerte = cajaFuerte.get(0).getMonto();
        }

        model.addAttribute("totalCajaFuerte", totalCajaFuerte);

        model.addAttribute("cajaFuerte", new CajaFuerte());

        return "ventas/ingresos-caja-fuerte";
    }

    @PostMapping(value = "/ingresarDinero")
    public String ingresarDinero(Model model, @ModelAttribute CajaFuerte cajaFuerteRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        if(cajaFuerteRequest.getMonto()!=null){

            //total caja fuerte actual
            List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
            Float totalCajaFuerte = new Float(0);
            Float montoActual = cajaFuerteRequest.getMonto();
            if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
                if(cajaFuerte.get(0).getMonto()!=null){
                    totalCajaFuerte=cajaFuerte.get(0).getMonto();
                }
                totalCajaFuerte = totalCajaFuerte+montoActual;
            }

            //Analisis de primer caja fuerte
            if(cajaFuerte.size()==0){
                totalCajaFuerte=cajaFuerteRequest.getMonto();
            }

            CajaFuerte ingresoCajaFuerte = new CajaFuerte();
            Date hoy = new Date();
            ingresoCajaFuerte.setFechaIngreso(hoy);
            ingresoCajaFuerte.setMonto(totalCajaFuerte);
            ingresoCajaFuerte.setUsername(user.getUserName());
            ingresoCajaFuerte.setMotivo(cajaFuerteRequest.getMotivo());

            ingresosCajaFuerteRepository.save(ingresoCajaFuerte);

        }

        model.addAttribute("mensaje","Quedo registrado su ingreso a Caja Fuerte.");
        model.addAttribute("clase","success");

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = new Float(0);
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
            totalCajaFuerte = cajaFuerte.get(0).getMonto();
        }
        model.addAttribute("totalCajaFuerte", totalCajaFuerte);

        model.addAttribute("cajaFuerte", new CajaFuerte());


        return "ventas/ingresos-caja-fuerte";
    }
}
