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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.model.Role;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.repository.ProductosRepository;
import pl.codeleak.demos.sbt.service.UserService;

@Controller
@RequestMapping(path = "/notificacionespublico")
public class NotificacionesController {
    @Autowired
    ProductosRepository productosRepository;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public ModelAndView notificaciones(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("userName", user.getName() + " " + user.getLastName());

        String role = "";
        for (Role roleT : user.getRoles()){
            role = roleT.getRole();
        }
        model.addAttribute("role", role);

        int diasDeAvisoAmarillo = 10;
        int diasDeAvisoRojo = 5;
        Date fechaHoy = new Date();
        Date fechaAmarillo = sumarDiasFecha(fechaHoy,diasDeAvisoAmarillo);
        Date fechaRojo = sumarDiasFecha(fechaHoy, diasDeAvisoRojo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Producto>
            productosPorVencerAmarillo = productosRepository.obtenerProductosAVencerAmarillo(fechaHoy,fechaAmarillo);
        List<Producto> productosPorVencerRojo = productosRepository.obtenerProductosAVencerRojo(fechaHoy,fechaRojo);

        List<Producto> productosConBajoStock = productosRepository.obtenerProductosConBajoStock();
        modelAndView.addObject("productosConBajoStock", productosConBajoStock);

        modelAndView.addObject("productosPorVencerAmarillo", productosPorVencerAmarillo);
        if(productosPorVencerAmarillo.isEmpty())
        {modelAndView.addObject("productosPorVencerRojo", productosPorVencerRojo);}
        modelAndView.setViewName("ventas/notificaciones_publico");
        return modelAndView;
    }
    public Date sumarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

}
