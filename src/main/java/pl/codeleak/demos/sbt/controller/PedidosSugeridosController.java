package pl.codeleak.demos.sbt.controller;

import java.util.ArrayList;
import java.util.Calendar;
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
@RequestMapping(path = "/pedidossugeridos")
public class PedidosSugeridosController {
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

        List<Producto> productosParaCocaColaMartes = productosRepository.obtenerProductosParaCocaColaMartes();
        List<Producto> productosParaCocaColaViernes = productosRepository.obtenerProductosParaCocaColaViernes();
        List<Producto> productosParaCigarrillos = productosRepository.obtenerProductosParaCigarrillos();

        modelAndView.addObject("productosParaCocaColaMartes", productosConRecomendaciones(productosParaCocaColaMartes));
        modelAndView.addObject("productosParaCocaColaViernes", productosConRecomendaciones(productosParaCocaColaViernes));
        modelAndView.addObject("productosParaCigarrillos", productosConRecomendaciones(productosParaCigarrillos));

        modelAndView.setViewName("ventas/pedidos-sugeridos");
        return modelAndView;
    }

    private List<Producto> productosConRecomendaciones(List<Producto> productos){
        List<Producto> listaDeProductosConRecomendaciones = new ArrayList<>();
        for(Producto p : productos){
            p.setCodigo(reglaDeNegocioRecomendaciones(p.getCodigo(),p.getExistencia()));
            listaDeProductosConRecomendaciones.add(p);
        }
        return listaDeProductosConRecomendaciones;
    }

    private String reglaDeNegocioRecomendaciones(String codigo, Float existenciaActual){
        switch (codigo){
            case "7790895005374":{ //Coca Cola retornable vidrio 1,25L
                if(existenciaActual<=2){return "1 Cajon";}
                else {return "No pedir";}
            }
            case "7790895641237":{ //Agua Saborizada Aquarius Naranja x1,5Lt
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895640490":{ //Agua Saborizada Aquarius Pomelo x1,5Lt
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895008498":{ //Aquarius Limonada 1,5
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895008504":{ //Aquarius limonada 2,25 L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895640483":{ //Aquarius Manzana 1.5L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895003325":{ //Aquarius naranja 2,25L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895640476":{ //Aquarius Pera 1.5L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895003295":{ //Aquarius pera 2,25L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895003288":{ //Aquarius pomelo 2,25L
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "7790895068096":{ //Coca Cola Descartable sin azucar 2,5
                if(existenciaActual<=2){return "1 Pack";}
                else {return "No pedir";}
            }
            case "1007084701235":{ //Monster Negro
                if(existenciaActual<=2){return "1 Pack";}
                else {return "No pedir";}
            }
            case "070847032861":{ //Monster White
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "1007084703703":{ //Monster Naranja
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "1007084703536":{ //Monster Verde
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "1 Pack";}
                else {return "No pedir";}
            }
            case "1007084703123":{ //Monster Watermelon
                if(existenciaActual<=2){return "1 Pack";}
                else {return "No pedir";}
            }
            case "77953513":{ //Cigarrillo Chesterfield comun 20
                if(existenciaActual<=3){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77953483":{ //Cigarrillo Chesterfield de 10 comun
                if(existenciaActual<=3){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77976994":{ //Cigarrillo Lucky 10 Convertible
                if(existenciaActual<=3){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77977038":{ //Cigarrillo Lucky 20 Convertible
                if(existenciaActual<=4){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77979667":{ //Cigarrillo Marlboro Box de 20 Garden Fusion
                if(existenciaActual<=1&&obetenerDiaDelMesReglaDeNegocio()<=15){return "Medio Cartón";}
                else {return "No pedir";}
            }
            case "77968401":{ //Cigarrillo Marlboro Box de 20 Summer Fusion
                if(existenciaActual<=1&&obetenerDiaDelMesReglaDeNegocio()<=15){return "Medio Cartón";}
                else {return "No pedir";}
            }
            case "77971913":{ //Cigarrillo Philip Convertible 10
                if(existenciaActual<=4){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77912954":{ //Cigarrillo Phillip Morris Box 20
                if(existenciaActual<=4){return "Un Cartón";}
                else {return "No pedir";}
            }
            case "77977120":{ //Cigarrillo Rothmans comun 20
                if(existenciaActual<=2&&obetenerDiaDelMesReglaDeNegocio()<=15){return "Un Cartón";}
                else {return "No pedir";}
            }

            default: return "Sin recomendación";
        }
    }

    private int obetenerDiaDelMesReglaDeNegocio(){
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

}
