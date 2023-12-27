package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import pl.codeleak.demos.sbt.model.*;
import pl.codeleak.demos.sbt.repository.ClientesRepository;
import pl.codeleak.demos.sbt.repository.CuentasCorrientesRepository;
import pl.codeleak.demos.sbt.repository.VentasRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/cuentascorrientes")
public class CuentasCorrientesController {

    @Autowired
    private UserService userService;
    @Autowired
    VentasRepository ventasRepository;
    @Autowired
    private CuentasCorrientesRepository cuentasCorrientesRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping(value = "/")
    public String mostrarVentas(Model model) {

        model.addAttribute("ventas", ventasRepository.mostrarVentasCuentasCorrientesImpagas());

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

        return "cuentascorrientes/cuentascorrientes";
    }

    @GetMapping(value = "/cancelar")
    public String cancelarCuentasCorrientes(Model model) {
        model.addAttribute("producto", new Producto());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        List<Cliente> clientesActivosYAprobadosCancelar = clientesRepository.findClientesActivosYAprobadosCuentaCorrienteOrderByAZ();
        if(clientesActivosYAprobadosCancelar==null){clientesActivosYAprobadosCancelar = new ArrayList<>();}
        model.addAttribute("clientesCuentaCorriente", clientesActivosYAprobadosCancelar);

        return "cuentascorrientes/cancelarcuentascorrientes";
    }

    @PostMapping(value = "/cancelarcliente")
    public String cancelarCuentaCorrienteCliente(Model model, @ModelAttribute Producto producto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        if(producto.getNombre()!=null&&!producto.getNombre().isEmpty()){
            String spliteado [] = producto.getNombre().split(",");
            Float monto = Float.parseFloat(spliteado[0]);
            Integer cliente = Integer.parseInt(spliteado[1]);

            Float resultado = imputarCuentasACliente(monto,cliente);
        }

        model.addAttribute("mensaje","Cierre de caja agregado correctamente");
        model.addAttribute("clase","success");

        return "cuentascorrientes/cancelarcuentascorrientes";
    }

    private Float imputarCuentasACliente(Float monto, Integer cliente) {
        List <Venta> resultado = cuentasCorrientesRepository.obtenerDeudasDeCliente(cliente);
        Float saldo = cancelarCuentasFIFO(resultado,monto,cliente);
        return saldo;
    }

    private Float cancelarCuentasFIFO(List<Venta> resultado, Float monto, Integer cliente) {
        Venta saldoAFavorAsiento = cuentasCorrientesRepository.obtenerSaldoAFavor(cliente);
        Float montoTemporal = 0.0f;
        if(saldoAFavorAsiento!=null&&saldoAFavorAsiento.getPagoCuentaCorriente()!=null&&saldoAFavorAsiento.getPagoCuentaCorriente()>0) {
            montoTemporal = monto+saldoAFavorAsiento.getPagoCuentaCorriente();
        }
        else{
            montoTemporal = monto;
        }
        if(resultado!=null&&!resultado.isEmpty()&&resultado.size()>0){
            if(resultado.get(0).getClienteCuentaCorriente()==6){
                montoTemporal = montoTemporal/Float.parseFloat("1.10");
            }
            for(int i=0;i<resultado.size();i++){
                if(resultado.get(i).getPagoCuentaCorriente()>0) {
                    if (!resultado.get(i).getUsername().equals("SaldoAFavor")) {
                    if (resultado.get(i).getPagoCuentaCorriente() <= montoTemporal) {
                        Venta ventaTemporal = resultado.get(i);
                        Date hoy = Utiles.obtenerFechaYHoraActualDate();
                        ventaTemporal.setFechaCancelacionCuentaCorriente(hoy);
                        montoTemporal = montoTemporal - ventaTemporal.getPagoCuentaCorriente();
                        cuentasCorrientesRepository.save(ventaTemporal);
                    } else {
                        if (montoTemporal > 0) {
                            Venta ventaTemporal = resultado.get(i);
                            Date hoy = Utiles.obtenerFechaYHoraActualDate();
                            ventaTemporal.setFechaCancelacionCuentaCorriente(hoy);
                            Float saldoSinCancelarTemporal =
                                ventaTemporal.getPagoCuentaCorriente() - montoTemporal;
                            montoTemporal = Float.parseFloat("0");
                            cuentasCorrientesRepository.save(ventaTemporal);
                            Venta asientoCancelacionDeuda = new Venta();
                            asientoCancelacionDeuda.setClienteCuentaCorriente(
                                ventaTemporal.getClienteCuentaCorriente());
                            asientoCancelacionDeuda.setFechaYHora(hoy);//es para marcar la fecha de hoy
                            asientoCancelacionDeuda.setUsername("SaldoDeudor");
                            asientoCancelacionDeuda.setPagoCuentaCorriente(saldoSinCancelarTemporal);
                            cuentasCorrientesRepository.save(asientoCancelacionDeuda);
                        }

                    }
                }
                    else{
                        Date hoy = Utiles.obtenerFechaYHoraActualDate();
                        saldoAFavorAsiento.setFechaCancelacionCuentaCorriente(hoy);
                    }
                }
            }
            if(montoTemporal>0){
                Venta asientoCancelacionDeuda = new Venta();
                asientoCancelacionDeuda.setClienteCuentaCorriente(resultado.get(0).getClienteCuentaCorriente());
                Date hoy = Utiles.obtenerFechaYHoraActualDate();
                asientoCancelacionDeuda.setFechaYHora(hoy);//es para marcar la fecha de hoy
                asientoCancelacionDeuda.setUsername("SaldoAFavor");
                asientoCancelacionDeuda.setPagoCuentaCorriente(montoTemporal);
                cuentasCorrientesRepository.save(asientoCancelacionDeuda);
            }
        }
        return montoTemporal;
    }

}
