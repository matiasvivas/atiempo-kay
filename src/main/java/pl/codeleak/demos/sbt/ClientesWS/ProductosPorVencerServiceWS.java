package pl.codeleak.demos.sbt.ClientesWS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.service.EmailService;

@RestController
public class ProductosPorVencerServiceWS {
    EmailService emailService;

    @Autowired
    CodigosRepository codigosRepository;

    public ProductosPorVencerServiceWS(EmailService emailService, CodigosRepository codigosRepository){
        this.emailService = emailService;
        this.codigosRepository = codigosRepository;
    }

    public void enviarMailProductosPorVencer10Dias(List<Producto> productosPorVencer) {
        Codigo codigo = codigosRepository.getTokenProductosPorVencer();
        if(codigo!=null&&codigo.getSerial()!=null&&productosPorVencer!=null&&productosPorVencer.size()>0) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setEmail("reemplazar");
            emailRequest.setSubject("Alerta Productos por Vencer");
            emailRequest.setContent(contenidoProductosPorVencer(productosPorVencer));
            emailService.sendProductosPorVencer(codigo.getSerial(), emailRequest);
        }
    }

    private String contenidoProductosPorVencer(List<Producto> productosPorVencer){
        String template = "<p>&nbsp;</p>\n" +
            "<h3 style=\"text-align: center; color: #3f7320;\"><span>Los siguientes productos están por vencer en 10 días.</span></h3>\n" +
            "<p><strong>Se recomienda que se establezcan promociones para estos productos antes de que venzan.</strong></p>\n" +
            "<p>&nbsp;</p>\n" +
            "<table BORDER CELLPADDING=5 CELLSPACING=5>" +
            "<tr><th>Producto</th>\n" +
            "<th>Cantidad</th>\n" +
            "  <th>Fecha de vencimiento</th>\n" +
            "  </tr>";
            for(Producto producto:productosPorVencer){
                template+="<tr><td>"+producto.getNombre()+"</td><td>"+producto.getExistencia()+"</td><td>"+producto.getFechaVencimiento()+"</td>";
            }
            template+="</tr></table>\n" +
            "<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<a target=\"_blank\" rel=\"nofollow noopener\">Reporte generado automáticamente.</a></p>";

        return template;
    }
}
