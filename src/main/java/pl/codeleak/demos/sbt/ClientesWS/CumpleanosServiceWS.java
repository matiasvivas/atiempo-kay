package pl.codeleak.demos.sbt.ClientesWS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.service.EmailService;

@RestController
public class CumpleanosServiceWS {
    EmailService emailService;

    @Autowired
    CodigosRepository codigosRepository;

    public CumpleanosServiceWS(EmailService emailService, CodigosRepository codigosRepository){
        this.emailService = emailService;
        this.codigosRepository = codigosRepository;
    }

    public void enviarMailFelizCumpleanos(Cliente cliente){
        Codigo codigo = codigosRepository.getTokenCumpleanos();
        if(codigo!=null&&codigo.getSerial()!=null&&cliente.getEmail()!=null) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setEmail(cliente.getEmail());
            emailRequest.setSubject("Feliz cumpleaños " + cliente.getNombre() + "!!!");
            emailRequest.setContent(contenidoDefaultCumple(cliente.getNombre()));
            emailService.sendHappyBirthday(codigo.getSerial(), emailRequest);
        }
    }

    private String contenidoDefaultCumple(String nombreCliente){
        String template = "<p>&nbsp;</p>\n" +
            "<h3 style=\"text-align: center; color: #3f7320;\"><span>Feliz cumplea&ntilde;os "+nombreCliente+"!!!</span></h3>\n" +
            "<p><strong>Te deseamos que en tu d&iacute;a la pases muy lindo y que se cumplan todos tus deseos! </strong></p>\n" +
            "<p>Gracias por ser nuestro cliente especial!</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<a target=\"_blank\" rel=\"nofollow noopener\">Saludos!</a></p>";

        return template;
    }
}
