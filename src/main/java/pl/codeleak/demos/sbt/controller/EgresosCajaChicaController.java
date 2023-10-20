package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.EgresosCaja;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.repository.EgresosCajaRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/egresoscajachica")
public class EgresosCajaChicaController {

    @Autowired
    private UserService userService;

    @Autowired
    EgresosCajaRepository egresosCajaRepository;

    @Autowired
    private CuentasCorrientesRepository cuentasCorrientesRepository;

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

        Date hoy = obtenerFechaYHoraActualDate();
        Date ayer = sumarRestarDiasFecha(hoy,-1);
        String today = "2022-05-30 00:00:00"; //Fecha de inicio de actividades
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        today = sdf.format(ayer); //muestra las ultimas 48hs

        model.addAttribute("egresos", egresosCajaRepository.mostrarEgresosHoyPorUsuario(user.getUserName()));
        model.addAttribute("producto", new Producto());

        return "egresoscajachica/egresos_caja_chica";
    }

    @PostMapping(value = "/salirDinero")
    public String salirDinero(Model model, @ModelAttribute Producto producto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        if(producto.getNombre()!=null&&!producto.getNombre().isEmpty()){
            String spliteado [] = producto.getNombre().split(";");
            Float monto = Float.parseFloat(spliteado[0]);
            String motivo = spliteado[1];
            EgresosCaja egresosCaja = new EgresosCaja();
            Date hoy = obtenerFechaYHoraActualDate();
            egresosCaja.setFechaHoraEgreso(hoy);
            egresosCaja.setUsername(user.getUserName());
            egresosCaja.setMonto(monto);
            egresosCaja.setMotivo(motivo);
            egresosCajaRepository.save(egresosCaja);

        }

        model.addAttribute("mensaje","Quedo registrada su salida de dinero.");
        model.addAttribute("clase","success");

        return "egresoscajachica/egresos_caja_chica";
    }
    public Date sumarRestarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    static Date obtenerFechaYHoraActualDate() {
        TimeZone zonaArgentina = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
        java.util.Calendar calendario = java.util.Calendar.getInstance(zonaArgentina);
        return calendario.getTime();
    }

}
