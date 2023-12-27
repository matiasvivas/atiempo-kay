package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.CierreCajaRepository;
import pl.codeleak.demos.sbt.repository.IngresosCajaFuerteRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/cierrecajapublico")
public class CierreCajaController {

    @Autowired
    private UserService userService;

    @Autowired
    private CierreCajaRepository cierreCajaRepository;

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private IngresosCajaFuerteRepository ingresosCajaFuerteRepository;

    //Agregar dinero real al cierre de caja
    @GetMapping(value = "/agregar")
    public String agregarBilletes(Model model, @ModelAttribute(value = "cierreCaja") CierreCaja cierreCaja) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());
        model.addAttribute("mensaje","Cierre de caja agregado correctamente");
        model.addAttribute("clase","success");

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date hoy= Utiles.obtenerFechaYHoraActualDate();
        String timestamp = sdf.format(hoy);
        cierreCaja.setFechaYHora(timestamp);
        cierreCaja.setUsername(user.getUserName());
        cierreCajaRepository.save(cierreCaja);

        //agregar el monto de cierre de caja sin la apertura a Caja Fuerte

        Float montoCierreCajaRequest = 0.0f;;
        montoCierreCajaRequest = cierreCaja.getMonto();
        if(montoCierreCajaRequest!=null){
            montoCierreCajaRequest=montoCierreCajaRequest - 3000.0f;;
        }
        if(montoCierreCajaRequest.compareTo(0.0f)<0){
            montoCierreCajaRequest = 0.0f;;
        }

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = 0.0f;
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
            totalCajaFuerte = cajaFuerte.get(0).getMonto();
        }
        totalCajaFuerte = totalCajaFuerte+montoCierreCajaRequest;
        CajaFuerte updateCajaFuerte = new CajaFuerte();
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){updateCajaFuerte=cajaFuerte.get(0);}
        updateCajaFuerte.setFechaEgreso(Utiles.obtenerFechaYHoraActualDate());
        updateCajaFuerte.setFechaEgreso(hoy);
        updateCajaFuerte.setUsername("IngresoCajaSistema");
        updateCajaFuerte.setMotivo("Ingreso a caja fuerte por Cierre de Caja de usuario: "+cierreCaja.getUsername()+ " por un monto sin apertura de: "+montoCierreCajaRequest+" con fecha: "+cierreCaja.getFechaYHora());
        updateCajaFuerte.setMonto(totalCajaFuerte);

        ingresosCajaFuerteRepository.save(updateCajaFuerte);
        model.addAttribute("cierreCaja", new CierreCaja());

        return "cierrecaja/cierre_caja_publico";
    }

    //Mostrar ultimo cierre de caja
    @GetMapping(value = "/mostrar")
    public String mostrarCierreCaja(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());
        CierreCaja cierreCaja = null;
        try {
            cierreCaja = cierreCajaRepository.findLastByUsername(user.getUserName());
        }
        catch(Exception e){
            cierreCaja = new CierreCaja();
        }
        if(cierreCaja!=null) {
            cierreCaja.setMonto(0.0f);
            model.addAttribute("cierreCaja", cierreCaja);
        }
        else{
            cierreCaja = new CierreCaja();
            cierreCaja.setMonto(0.0f);
            model.addAttribute("cierreCaja", cierreCaja);
        }

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "cierrecaja/cierre_caja_publico";
    }

    //Mostrar ultimo cierre de caja
    @GetMapping(value = "/mostrar7dias")
    public String mostrarCierreCajas7dias(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());
        List<CierreCaja> cierreCaja = cierreCajaRepository.findLast7Days();
        List<CierreCaja> ultimos6CierreCaja = new ArrayList<>();
        if(cierreCaja.size()>0){
            for(int i=0;i<=6;i++){
                CierreCaja cierre = cierreCaja.get(i);
                ultimos6CierreCaja.add(cierre);
            }
        }

        model.addAttribute("cierreCaja",ultimos6CierreCaja);

        Date hoy = Utiles.obtenerFechaYHoraActualDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date ayer = sumarRestarDiasFecha(hoy,-7);
        List<Object> ventasPorCajas = ventasRepository.obtenerVentasPorCaja(ayer);
        List<Object> ultimas6VentasPorCajas = new ArrayList<>();
        if(ventasPorCajas!=null&&ventasPorCajas.size()>=14) {
            if (ventasPorCajas != null && !ventasPorCajas.isEmpty()) {
                for (int i = 0; i < 6; i++) {
                    ultimas6VentasPorCajas.add(ventasPorCajas.get(i));
                }
            }
        }
        if(ventasPorCajas!=null&&ventasPorCajas.size()<14){
            for (int i = 0; i < ventasPorCajas.size(); i++) {
                ultimas6VentasPorCajas.add(ventasPorCajas.get(i));
            }
        }
        //USAR GROUP BY DE FECHA

        model.addAttribute("ventasPorCajas",ultimas6VentasPorCajas);

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        return "cierrecaja/control_de_caja";
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}