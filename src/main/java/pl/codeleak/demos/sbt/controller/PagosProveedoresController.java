package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.web.servlet.ModelAndView;
import pl.codeleak.demos.sbt.enumeradores.Estado;
import pl.codeleak.demos.sbt.model.CajaFuerte;
import pl.codeleak.demos.sbt.model.EgresosCaja;
import pl.codeleak.demos.sbt.model.PagoProveedores;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.model.Venta;
import pl.codeleak.demos.sbt.repository.EgresosCajaRepository;
import pl.codeleak.demos.sbt.repository.IngresosCajaFuerteRepository;
import pl.codeleak.demos.sbt.repository.PagoProveedoresRepository;
import pl.codeleak.demos.sbt.repository.ProveedoresRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/pagoproveedores")
public class PagosProveedoresController {
    @Autowired
    private UserService userService;

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private EgresosCajaRepository egresosCajaRepository;

    @Autowired
    private PagoProveedoresRepository pagoProveedoresRepository;

    @Autowired
    private IngresosCajaFuerteRepository ingresosCajaFuerteRepository;

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @GetMapping(value = "/")
    public ModelAndView cargadorInicial(Model model) {

        model.addAttribute("producto", new Producto());
        model.addAttribute("proveedores", proveedoresRepository.findAllOrderByAZ());

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);
        modelAndView.setViewName("compras/pagos-proveedores");
        return modelAndView;
    }

    @PostMapping(value = "/cargarpago")
    public String cargarPago(@ModelAttribute Producto producto, Model model) {

        String concatPagoProveedores = producto.getNombre();
        String [] concat = concatPagoProveedores.split(",");
        String codProveedor = concat[2];
        String fechaP = concat[1];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaPago = null;
        try {
            fechaPago = sdf.parse(fechaP);
        }
        catch(Exception e){
            fechaPago = new Date();
            e.getMessage();
        }
        String montoPago = concat[0];

        PagoProveedores pagoProveedores = new PagoProveedores();
        pagoProveedores.setProveedor(codProveedor);
        pagoProveedores.setEstado(Estado.PENDIENTE);
        pagoProveedores.setMontoPedido(Float.parseFloat(montoPago));
        pagoProveedores.setFecha(fechaPago);

        pagoProveedoresRepository.save(pagoProveedores);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        model.addAttribute("mensaje","Agregado correctamente");
        model.addAttribute("clase","success");

        return "compras/pagos-proveedores";
    }

    @PostMapping(value = "/pagar")
    public String pagar(@ModelAttribute PagoProveedores pagoProveedoresRequest, Model model) {

        Integer codProveedor = pagoProveedoresRequest.getId();

        if(codProveedor!=null&&(pagoProveedoresRequest.getMontoEntregaEfectivo()!=null||pagoProveedoresRequest.getMontoEntregaDigital()!=null)) {

            PagoProveedores pagoProveedoresAModificar = pagoProveedoresRepository.obtenerpagoProveedorPorCodigo(codProveedor);
            if (pagoProveedoresAModificar != null) {
                Float montoACancelar = 0.0f;
                Float saldoPendienteEnBase = pagoProveedoresAModificar.getMontoSaldoPendiente();
                Float entregaAcumuladaEfectivo = pagoProveedoresAModificar.getMontoEntregaEfectivo();
                Float entregaAcumuladaDigital = pagoProveedoresAModificar.getMontoEntregaDigital();

                Float montoRequestEfectivo = pagoProveedoresRequest.getMontoEntregaEfectivo();
                Float montoRequestDigital = pagoProveedoresRequest.getMontoEntregaDigital();

                //Evaluacion y cerificacion
                if (saldoPendienteEnBase == null) {
                    saldoPendienteEnBase = 0.0f;
                }
                if (entregaAcumuladaEfectivo == null) {
                    entregaAcumuladaEfectivo = 0.0f;
                }
                if (entregaAcumuladaDigital == null) {
                    entregaAcumuladaDigital = 0.0f;
                }
                if (montoRequestEfectivo == null) {
                    montoRequestEfectivo = 0.0f;
                }
                if (montoRequestDigital == null) {
                    montoRequestDigital = 0.0f;
                }

                if (montoRequestEfectivo != null) {
                    montoACancelar = montoACancelar + montoRequestEfectivo;
                }
                if (montoRequestDigital != null) {
                    montoACancelar = montoACancelar + montoRequestDigital;
                }

                //sumar montoACancelar actual y Montos Acumulados
                Float entregasAcumuladasEfectivoYDigital = entregaAcumuladaEfectivo + entregaAcumuladaDigital;

                if (entregasAcumuladasEfectivoYDigital.compareTo(0.0f) != 0) {
                    montoACancelar = montoACancelar + entregasAcumuladasEfectivoYDigital;
                }

                if (pagoProveedoresAModificar.getMontoPedido().equals(montoACancelar)) {
                    pagoProveedoresAModificar.setEstado(Estado.CANCELADO);
                    pagoProveedoresAModificar.setMontoSaldoPendiente(0.0f);
                    pagoProveedoresAModificar.setMontoEntregaEfectivo(montoRequestEfectivo + entregaAcumuladaEfectivo);
                    pagoProveedoresAModificar.setMontoEntregaDigital(montoRequestDigital + entregaAcumuladaDigital);
                }

                if (pagoProveedoresAModificar.getMontoPedido().compareTo(montoACancelar) > 0) {
                    //Significa que queda saldo pendiente
                    Float saldoPendiente = pagoProveedoresAModificar.getMontoPedido() - montoACancelar;
                    pagoProveedoresAModificar.setMontoSaldoPendiente(saldoPendiente);
                    pagoProveedoresAModificar.setMontoEntregaEfectivo(montoRequestEfectivo + entregaAcumuladaEfectivo);
                    pagoProveedoresAModificar.setMontoEntregaDigital(montoRequestDigital + entregaAcumuladaDigital);
                }

                Float memoriaMontoSacadoCajaFuertePorDescubierto = 0.0f;

                if (pagoProveedoresAModificar.getMontoPedido().compareTo(montoACancelar) < 0) {
                    Float montoSacadoDeCajaFuerte = montoACancelar - pagoProveedoresAModificar.getMontoPedido();
                    EgresosCaja egresosCaja = new EgresosCaja();
                    egresosCaja.setUsername("SalidaCajaSistema");
                    egresosCaja.setRevisado(true);
                    egresosCaja.setMonto(montoSacadoDeCajaFuerte);
                    egresosCaja.setMotivo("Salida de caja fuerte por pago a proveedor: " + pagoProveedoresAModificar.getProveedor() + " ID transaccion: " + pagoProveedoresAModificar.getId());
                    egresosCaja.setEsCajaFuerte(true);
                    Date hoy = new Date();
                    egresosCaja.setFechaHoraEgreso(hoy);

                    egresosCajaRepository.save(egresosCaja);

                    pagoProveedoresAModificar.setEstado(Estado.CANCELADO);
                    pagoProveedoresAModificar.setMontoSaldoPendiente(0.0f);
                    pagoProveedoresAModificar.setMontoEntregaEfectivo(montoRequestEfectivo + entregaAcumuladaEfectivo);
                    pagoProveedoresAModificar.setMontoEntregaDigital(montoRequestDigital + entregaAcumuladaDigital);

                    //quitar el monto sacado de caja fuerte
                    List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
                    Float totalCajaFuerte = 0.0f;
                    if ((cajaFuerte.size() > 0) && (cajaFuerte != null)) {
                        totalCajaFuerte = cajaFuerte.get(0).getMonto();
                    }
                    totalCajaFuerte = totalCajaFuerte - montoSacadoDeCajaFuerte;
                    CajaFuerte updateCajaFuerte = cajaFuerte.get(0);
                    updateCajaFuerte.setFechaEgreso(hoy);
                    updateCajaFuerte.setUsername("SalidaCajaSistema");
                    updateCajaFuerte.setMotivo("Salida de caja fuerte por pago a proveedor: " + pagoProveedoresAModificar.getProveedor() + " por un monto de: " + montoSacadoDeCajaFuerte);
                    updateCajaFuerte.setMonto(totalCajaFuerte);

                    ingresosCajaFuerteRepository.save(updateCajaFuerte);

                    memoriaMontoSacadoCajaFuertePorDescubierto = montoSacadoDeCajaFuerte;

                }

                //sacar dinero de caja fuerte para pago efectivo y/o digital
                List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
                Float totalCajaFuerte = 0.0f;
                if ((cajaFuerte.size() > 0) && (cajaFuerte != null)) {
                    totalCajaFuerte = cajaFuerte.get(0).getMonto();
                }
                model.addAttribute("totalCajaFuerte", totalCajaFuerte);

                totalCajaFuerte = totalCajaFuerte - montoRequestEfectivo;
                if (memoriaMontoSacadoCajaFuertePorDescubierto.compareTo(0.0f) > 0) {
                    totalCajaFuerte = totalCajaFuerte + memoriaMontoSacadoCajaFuertePorDescubierto;
                }

                CajaFuerte updateCajaFuerte = new CajaFuerte();
                if ((cajaFuerte.size() > 0) && (cajaFuerte != null)) {
                    updateCajaFuerte = cajaFuerte.get(0);
                }
                updateCajaFuerte.setFechaEgreso(new Date());
                updateCajaFuerte.setUsername("SalidaCajaSistema");
                updateCajaFuerte.setMotivo("Salida de caja fuerte por pago a proveedor: " + pagoProveedoresAModificar.getProveedor() + " por un monto efectivo de: " + montoRequestEfectivo);
                updateCajaFuerte.setMonto(totalCajaFuerte);

                ingresosCajaFuerteRepository.save(updateCajaFuerte);

            }

            pagoProveedoresRepository.save(pagoProveedoresAModificar);
        }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            model.addAttribute("userName", user.getName() + " " + user.getLastName());

            String role = "";
            for (Role roleT : user.getRoles()) {
                role = roleT.getRole();
            }
            model.addAttribute("role", role);

            Date fechaHoy = new Date();
            Date fechaManiana = sumarRestarDiasFecha(fechaHoy, 1);

            Float totalEfectivoHoy = ventasRepository.mostrarTotalEfectivoHoy(fechaHoy, fechaManiana);
            Float totalEgresosHoy = egresosCajaRepository.mostrarTotalEgresosHoy(fechaHoy, fechaManiana);
            Float totalEfectivo = 0.0f;
            if (totalEfectivoHoy != null && !totalEfectivoHoy.equals(0) && !totalEfectivoHoy.equals("")) {
                totalEfectivo = totalEfectivoHoy;
            }
            if (totalEgresosHoy != null && !totalEgresosHoy.equals(0) && !totalEgresosHoy.equals("")) {
                totalEfectivo = totalEfectivo - totalEgresosHoy;
            }

            Float totalDigitalHoy = ventasRepository.mostrarTotalDigitalHoy(fechaHoy, fechaManiana);
            if (totalDigitalHoy == null || totalDigitalHoy.equals(0) || totalDigitalHoy.equals("")) {
                totalDigitalHoy = 0.0f;
            }

            Float cajaJoha = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana, "Johana");
            Float cajaAlexia = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana, "Alexia");
            Float cajaNicole = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana, "Nicole");
            Float cajaSol = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana, "smartinez");
            Float cajaMati = ventasRepository.obtenerTotalVentaHoyPorUsuario(fechaHoy, fechaManiana, "mvivas");

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


            //se agrega las ventas

            Date hoy2 = new Date();
            Date ayer = sumarRestarDiasFecha(hoy2, -1);
            List<Venta> todasLasVentas = (List<Venta>) ventasRepository.mostrarVentas48hs(ayer);
            try {
                model.addAttribute("ventas", ventasRepository.mostrarVentas48hs(ayer));
            } catch (Exception e) {
                model.addAttribute("ventas", null);
            }

            //Obtener estadisticas 48hs
            int totalArray = todasLasVentas.size();
            String[] estadisticasVentas = new String[totalArray];

            for (int i = 0; i < todasLasVentas.size(); i++) {
                Date fecha = todasLasVentas.get(i).getFechaYHora();
                String totalVenta = todasLasVentas.get(i).getTotal().toString();
                String[] totalVentaSplit = totalVenta.split("\\.");
                totalVenta = totalVentaSplit[0];
                estadisticasVentas[i] = fecha + "," + totalVenta + ";";
            }
            String resultado = "";
            for (int i = 0; i < estadisticasVentas.length; i++) {
                resultado = resultado + estadisticasVentas[i].toString();
            }

            //nuevo filtro de estadisticas por dia

            //fin nuevo filtro estadisticas por dia

            model.addAttribute("ventasMontos", ventasRepository.mostrarVentasEstadisticas48hs(ayer));

            model.addAttribute("estadisticasVentas", resultado);

            model.addAttribute("ventasDigitales", ventasRepository.mostrarMontosDigitales48hs(ayer));

            model.addAttribute("salidasCaja", egresosCajaRepository.mostrarTodosLosEgresosSinRevisar());

            model.addAttribute("totalCajaFuerte", ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte());

            List<CajaFuerte> cajaFuerte = ingresosCajaFuerteRepository.mostrarMontoActualCajaFuerte();
            Float totalCajaFuerte = 0.0f;
            if ((cajaFuerte.size() > 0) && (cajaFuerte != null)) {
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