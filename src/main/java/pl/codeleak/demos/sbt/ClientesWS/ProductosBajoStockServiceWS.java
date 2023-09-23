package pl.codeleak.demos.sbt.ClientesWS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.service.EmailService;

@RestController
public class ProductosBajoStockServiceWS {
    EmailService emailService;

    @Autowired
    CodigosRepository codigosRepository;

    public ProductosBajoStockServiceWS(EmailService emailService, CodigosRepository codigosRepository){
        this.emailService = emailService;
        this.codigosRepository = codigosRepository;
    }

    public void enviarMailProductosBajoStockPrioridad1(List<Producto> productosConBajoStock){
        Codigo codigo = codigosRepository.getTokenProductosBajoStockParaMail();
        if(codigo!=null&&codigo.getSerial()!=null&&productosConBajoStock!=null&&productosConBajoStock.size()>0) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setEmail("reemplazar");
            emailRequest.setSubject("Alerta Bajo Stock");
            emailRequest.setContent(contenidoProductosBajoStock(productosConBajoStock));
            emailService.sendProductosBajoStock(codigo.getSerial(), emailRequest);
        }
    }

    private String contenidoProductosBajoStock(List<Producto> productosPorVencer){
        String template = "<p>&nbsp;</p>\n" +
            "<h3 style=\"text-align: center; color: #3f7320;\"><span>Los siguientes productos están con Bajo Stock.</span></h3>\n" +
            "<p><strong>Se recomienda reponer la siguiente mercadería.</strong></p>\n" +
            "<p>&nbsp;</p>\n" +
            "<table BORDER CELLPADDING=5 CELLSPACING=5>" +
            "<tr><th>Producto</th>\n" +
            "<th>Cantidad</th>\n" +
            "  </tr>";
            for(Producto producto:productosPorVencer){
                template+="<tr><td>"+producto.getNombre()+"</td><td>"+producto.getExistencia()+"</td>";
            }
            template+="</tr></table>\n" +
            "<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<a target=\"_blank\" rel=\"nofollow noopener\">Reporte generado automáticamente.</a></p>";

        return template;
    }
}
