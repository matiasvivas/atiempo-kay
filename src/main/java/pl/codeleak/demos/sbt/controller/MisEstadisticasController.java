package pl.codeleak.demos.sbt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.EgresosCajaRepository;
import pl.codeleak.demos.sbt.repository.IngresosCajaFuerteRepository;
import pl.codeleak.demos.sbt.repository.PagoProveedoresRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/ventas")
public class MisEstadisticasController {

    @Autowired
    VentasRepository ventasRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EgresosCajaRepository egresosCajaRepository;
    @Autowired
    private IngresosCajaFuerteRepository ingresosCajaFuerteRepository;

    @GetMapping(value = "/")
    public String mostrarVentasEstadisticas48hs(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        Date fechaHoy = new Date();
        Date fechaManiana = sumarRestarDiasFecha(fechaHoy,1);
        Date fechaHoyCero = this.cerificarFecha(fechaHoy);
        Date fechaManianaCero = this.cerificarFecha(fechaManiana);
        Float totalEfectivoHoy = ventasRepository.mostrarTotalEfectivoHoy(fechaHoyCero, fechaManianaCero);
        Float totalEgresosHoy = egresosCajaRepository.mostrarTotalEgresosHoy(fechaHoyCero, fechaManianaCero);
        if(totalEfectivoHoy==null){totalEfectivoHoy = 0.0f;}
        if(totalEgresosHoy==null){totalEgresosHoy = 0.0f;}
        Float totalEfectivo = totalEfectivoHoy - totalEgresosHoy;

        Float totalDigitalHoy = ventasRepository.mostrarTotalDigitalHoy(fechaHoyCero, fechaManianaCero);
        if(totalDigitalHoy==null){totalDigitalHoy = 0.0f;}

        model.addAttribute("totalEfectivo", totalEfectivo);
        model.addAttribute("totalDigital", totalDigitalHoy);

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = 0.0f;
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
            totalCajaFuerte = cajaFuerte.get(0).getMonto();
        }

        model.addAttribute("totalCajaFuerte", totalCajaFuerte);

        return "ventas/ver_ventas";
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    public Date cerificarFecha (Date recibida){
        // Convierte la fecha actual a un objeto Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recibida);

        // Establece la hora, minuto y segundo en cero
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Obtiene la fecha con la hora 00:00:00
        Date fechaConHoraCero = calendar.getTime();

        return fechaConHoraCero;
    }
}
