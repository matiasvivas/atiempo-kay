package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.VentasPublicoRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/ventaspublico")
public class VentasPublicoController {
    @Autowired
    VentasPublicoRepository ventasPublicoRepository;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String mostrarVentas(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        Date hoy = new Date();
        Date ayer = sumarRestarDiasFecha(hoy,-1);
        try {
            model.addAttribute("ventas", ventasPublicoRepository.mostrarVentasDeHoy(ayer, user.getUserName()));
        }
        catch(Exception e){
            model.addAttribute("ventas", null);
        }
        return "ventas/ver_ventas_publico";
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}
