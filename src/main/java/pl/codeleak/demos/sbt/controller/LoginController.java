package pl.codeleak.demos.sbt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.codeleak.demos.sbt.enumeradores.TipoCodigo;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.ProductosRepository;
import pl.codeleak.demos.sbt.repository.UserRepository;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private CodigosRepository validarCodigosRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "No es posible elegir este nombre de usuario. Elija otro por favor.");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "El usuario ha sido registrado exitosamente!");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public ModelAndView home(Model model){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        if(user.getDiasRestantes()!=null&&user.getDiasRestantes()>=0) {

            modelAndView.addObject("userName", user.getName() + " " + user.getLastName());
            String role = "";
            for (Role roleT : user.getRoles()) {
                role = roleT.getRole();
            }
            modelAndView.addObject("role", role);

            //corte para clientes externos:
            if(role.equals("CLIENTECC")){
                modelAndView.setViewName("principal");
                return modelAndView;
            }
            //fin corte para clientes externos

            int diasDeAvisoAmarillo = 10;
            int diasDeAvisoRojo = 5;
            Date fechaHoy = new Date();
            Date fechaAmarillo = sumarDiasFecha(fechaHoy, diasDeAvisoAmarillo);
            Date fechaRojo = sumarDiasFecha(fechaHoy, diasDeAvisoRojo);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Producto> productosPorVencerAmarillo =
                productosRepository.obtenerProductosAVencerAmarillo(fechaHoy, fechaAmarillo);
            List<Producto> productosPorVencerRojo =
                productosRepository.obtenerProductosAVencerRojo(fechaHoy, fechaRojo);

            modelAndView.addObject("productosPorVencerAmarillo", productosPorVencerAmarillo);
            modelAndView.addObject("productosPorVencerRojo", productosPorVencerRojo);
            modelAndView.setViewName("ventas/ver_ventas_publico");
        }
        else{
            //Si el usuario no esta activo se le solicitará un código de activación de plan
            modelAndView.addObject("diasRestantes",-1);
            modelAndView.addObject("account", user.getUserName());
            modelAndView.addObject("userName", user.getName() + " " + user.getLastName());
            modelAndView.addObject("mensajeActivacion","Debe ingresar un código de activación para utilizar el sistema.");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }


    @PostMapping(value = "/validar")
    public ModelAndView validarCodigo(User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getEmail());
        //Integer nuevosDiasRestantes = -1;
        String codigoSerial = user.getName();
        Codigo codigoEncontrado = validarCodigosRepository.findFirstBySerial(codigoSerial);
        if(codigoEncontrado!=null
            && codigoEncontrado.getTipo()!= TipoCodigo.DESCUENTO
            && codigoEncontrado.getTipo()!= TipoCodigo.VENTAENVASE
            && codigoEncontrado.getTipo()!= TipoCodigo.COMPRAENVASE
            && codigoEncontrado.getTipo()!= TipoCodigo.RECARGO
            && codigoEncontrado.getFechaActivacion()==null
            && codigoEncontrado.getSerial().equals(codigoSerial)){

            codigoEncontrado.setFechaActivacion(new Date());
            codigoEncontrado.setUserId(userExists.getId());
            validarCodigosRepository.save(codigoEncontrado);

        }

        if (userExists != null &&codigoEncontrado!=null && codigoEncontrado.getDiasRestantes()!=null && codigoEncontrado.getDiasRestantes() >=0) {
            userExists.setDiasRestantes(codigoEncontrado.getDiasRestantes());
            userRepository. updateDiasRestantes(userExists.getDiasRestantes(), userExists.getId());
        }

        if (bindingResult.hasErrors()) {
            bindingResult
                .rejectValue("userName", "error.user",
                    "El usuario ingresado es inexistente o esta inactivo");
            modelAndView.setViewName("login");
        } else {
            modelAndView.addObject("mensajeCodigo","Código de activación inválido.");
            modelAndView.setViewName("login");

        }
        return modelAndView;
    }


    public Date sumarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}
