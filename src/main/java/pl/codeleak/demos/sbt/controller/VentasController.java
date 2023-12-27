package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

@Controller
@RequestMapping(path = "/ventasOLD")
public class VentasController {
    @Autowired
    VentasRepository ventasRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EgresosCajaRepository egresosCajaRepository;

    @Autowired
    private PagoProveedoresRepository pagoProveedoresRepository;

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

        Date hoy = Utiles.obtenerFechaYHoraActualDate();
        Date ayer = sumarRestarDiasFecha(hoy,-7);
        List<Venta> todasLasVentas = (List<Venta>)ventasRepository.mostrarVentas48hs(ayer);
        try {
            model.addAttribute("ventas", ventasRepository.mostrarVentas48hs(ayer));
        }
        catch(Exception e){
            model.addAttribute("ventas", null);
        }

        //Obtener estadisticas 48hs
        int totalArray = todasLasVentas.size();
        String[] estadisticasVentas = new String[totalArray];

        for(int i = 0; i<todasLasVentas.size();i++){
            Date fecha = todasLasVentas.get(i).getFechaYHora();
            String totalVenta = todasLasVentas.get(i).getTotal().toString();
            String[] totalVentaSplit = totalVenta.split("\\.");
            totalVenta = totalVentaSplit[0];
            estadisticasVentas[i] = fecha+","+totalVenta+";";
        }
        String resultado ="";
        for(int i = 0; i<estadisticasVentas.length;i++){
            resultado=resultado+estadisticasVentas[i].toString();
        }

        //nuevo filtro de estadisticas por dia

        //fin nuevo filtro estadisticas por dia

        //TODO: AGREGAR FECHAHOY FECHAMANIANA model.addAttribute("ventasMontos", ventasRepository.mostrarVentasEstadisticas48hs(ayer));

        model.addAttribute("estadisticasVentas", resultado);

        //TODO: AGREGAR FECHAHOY FECHAMANIANA model.addAttribute("ventasDigitales", ventasRepository.mostrarMontosDigitales48hs(ayer));

        model.addAttribute("salidasCaja", egresosCajaRepository.mostrarTodosLosEgresosSinRevisar());

        Date fechaHoy = hoy;
        Date fechaManiana = sumarRestarDiasFecha(hoy,1);

        Float totalEfectivoHoy = ventasRepository.mostrarTotalEfectivoHoy(fechaHoy, fechaManiana);
        Float totalEgresosHoy = egresosCajaRepository.mostrarTotalEgresosHoy(fechaHoy, fechaManiana);
        if(totalEfectivoHoy==null){totalEfectivoHoy = 0.0f;}
        if(totalEgresosHoy==null){totalEgresosHoy = 0.0f;}
        Float totalEfectivo = totalEfectivoHoy - totalEgresosHoy;

        Float totalDigitalHoy = ventasRepository.mostrarTotalDigitalHoy(fechaHoy, fechaManiana);
        if(totalDigitalHoy==null){totalDigitalHoy = 0.0f;}

        Float cajaJoha = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Johana");
        Float cajaAlexia = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Alexia");
        Float cajaNicole = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Nicole");
        Float cajaSol = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"smartinez");
        Float cajaMati = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"mvivas");

        if(cajaJoha==null){cajaJoha = 0.0f;}
        if(cajaAlexia==null){cajaAlexia = 0.0f;}
        if(cajaNicole==null){cajaNicole = 0.0f;}
        if(cajaSol==null){cajaSol = 0.0f;}
        if(cajaMati==null){cajaMati = 0.0f;}

        model.addAttribute("totalEfectivo", totalEfectivo);
        model.addAttribute("totalDigital", totalDigitalHoy);
        model.addAttribute("cajaJoha", cajaJoha);
        model.addAttribute("cajaAlexia", cajaAlexia);
        model.addAttribute("cajaNicole", cajaNicole);
        model.addAttribute("cajaSol", cajaSol);
        model.addAttribute("cajaMati", cajaMati);

        PagoProveedores pagoProveedores = new PagoProveedores();
        model.addAttribute("pagoProveedores", pagoProveedores);

        List<PagoProveedores> proveedoresProgramados = pagoProveedoresRepository.mostrarProveedoresProgramadosPENDIENTES();
        model.addAttribute("proveedoresPendientes", proveedoresProgramados);

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = 0.0f;
        if((cajaFuerte.size()>0)&&(cajaFuerte!=null)){
            totalCajaFuerte = cajaFuerte.get(0).getMonto();
        }

        model.addAttribute("totalCajaFuerte", totalCajaFuerte);

        return "ventas/ver_ventas";
    }

    @PostMapping(value="marcarEgresoComoRevisado")
    public String marcarEgresoComoRevisado(Model model, Integer id){


        EgresosCaja eRevisar = egresosCajaRepository.marcarEgresoComoRevisado(id);
        if(eRevisar!=null){
            eRevisar.setRevisado(true);
            egresosCajaRepository.save(eRevisar);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        Date hoy = Utiles.obtenerFechaYHoraActualDate();
        Date ayer = sumarRestarDiasFecha(hoy,-1);
        List<Venta> todasLasVentas = (List<Venta>)ventasRepository.mostrarVentas48hs(ayer);
        try {
            model.addAttribute("ventas", ventasRepository.mostrarVentas48hs(ayer));
        }
        catch(Exception e){
            model.addAttribute("ventas", null);
        }

        //Obtener estadisticas 48hs
        int totalArray = todasLasVentas.size();
        String[] estadisticasVentas = new String[totalArray];

        for(int i = 0; i<todasLasVentas.size();i++){
            Date fecha = todasLasVentas.get(i).getFechaYHora();
            String totalVenta = todasLasVentas.get(i).getTotal().toString();
            String[] totalVentaSplit = totalVenta.split("\\.");
            totalVenta = totalVentaSplit[0];
            estadisticasVentas[i] = fecha+","+totalVenta+";";
        }
        String resultado ="";
        for(int i = 0; i<estadisticasVentas.length;i++){
            resultado=resultado+estadisticasVentas[i].toString();
        }

        //nuevo filtro de estadisticas por dia

        //fin nuevo filtro estadisticas por dia

        //TODO: AGREGAR FECHAHOY FECHAMANIANA model.addAttribute("ventasMontos", ventasRepository.mostrarVentasEstadisticas48hs(ayer));

        model.addAttribute("estadisticasVentas", resultado);

        //TODO: AGREGAR FECHAHOY FECHAMANIANA model.addAttribute("ventasDigitales", ventasRepository.mostrarMontosDigitales48hs(ayer));

        model.addAttribute("salidasCaja", egresosCajaRepository.mostrarTodosLosEgresosSinRevisar());

        Date fechaHoy = hoy;
        Date fechaManiana = sumarRestarDiasFecha(hoy,1);

        Float totalEfectivoHoy = ventasRepository.mostrarTotalEfectivoHoy(fechaHoy, fechaManiana);
        Float totalEgresosHoy = egresosCajaRepository.mostrarTotalEgresosHoy(fechaHoy, fechaManiana);
        if(totalEfectivoHoy==null){totalEfectivoHoy = 0.0f;}
        if(totalEgresosHoy==null){totalEgresosHoy = 0.0f;}
        Float totalEfectivo = totalEfectivoHoy - totalEgresosHoy;

        Float totalDigitalHoy = ventasRepository.mostrarTotalDigitalHoy(fechaHoy, fechaManiana);
        if(totalDigitalHoy==null){totalDigitalHoy = 0.0f;}

        Float cajaJoha = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Johana");
        Float cajaAlexia = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Alexia");
        Float cajaNicole = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"Nicole");
        Float cajaSol = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"smartinez");
        Float cajaMati = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana,"mvivas");

        if(cajaJoha==null){cajaJoha = 0.0f;}
        if(cajaAlexia==null){cajaAlexia = 0.0f;}
        if(cajaNicole==null){cajaNicole = 0.0f;}
        if(cajaSol==null){cajaSol = 0.0f;}
        if(cajaMati==null){cajaMati = 0.0f;}

        model.addAttribute("totalEfectivo", totalEfectivo);
        model.addAttribute("totalDigital", totalDigitalHoy);
        model.addAttribute("cajaJoha", cajaJoha);
        model.addAttribute("cajaAlexia", cajaAlexia);
        model.addAttribute("cajaNicole", cajaNicole);
        model.addAttribute("cajaSol", cajaSol);
        model.addAttribute("cajaMati", cajaMati);

        PagoProveedores pagoProveedores = new PagoProveedores();
        model.addAttribute("pagoProveedores", pagoProveedores);

        List<PagoProveedores> proveedoresProgramados = pagoProveedoresRepository.mostrarProveedoresProgramadosPENDIENTES();
        model.addAttribute("proveedoresPendientes", proveedoresProgramados);

        List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
        Float totalCajaFuerte = 0.0f;;
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
}
