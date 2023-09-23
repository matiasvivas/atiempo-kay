package pl.codeleak.demos.sbt.service;

import pl.codeleak.demos.sbt.ClientesWS.EmailRequest;
import pl.codeleak.demos.sbt.ClientesWS.EmailResponse;

public interface EmailService {
  EmailResponse sendHappyBirthday(String token, EmailRequest emailRequest);
  EmailResponse sendProductosPorVencer(String token, EmailRequest emailRequest);
  EmailResponse sendProductosBajoStock(String token, EmailRequest emailRequest);
}
