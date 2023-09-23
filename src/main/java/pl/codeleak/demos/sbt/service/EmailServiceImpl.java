package pl.codeleak.demos.sbt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.codeleak.demos.sbt.ClientesWS.AbstractClient;
import pl.codeleak.demos.sbt.ClientesWS.EmailRequest;
import pl.codeleak.demos.sbt.ClientesWS.EmailResponse;

@Slf4j
@Service
public class EmailServiceImpl extends AbstractClient implements EmailService {

    public EmailServiceImpl(RestTemplate restTemplate) {
        super(restTemplate);
    }
    @Override
    public EmailResponse sendHappyBirthday(String token, EmailRequest emailRequest) {
        String uri = baseUrl + "/sendHappyBirthday";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = this.buildAuthTokenEmail(token);
        HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(emailRequest, headers);
        ResponseEntity<EmailResponse> response = template.exchange(uri, HttpMethod.POST, requestEntity,
                EmailResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully user creation: {}", response.getBody().getEnviado());
            return response.getBody();
        }
        log.error("Error in user creation - httpStatus was: {}", response.getStatusCode());
        throw new RuntimeException("Error");
    }

    public EmailResponse sendProductosPorVencer(String token, EmailRequest emailRequest) {
        emailRequest.setEmail(emailRecibirAlertaProductosPorVencer);
        String uri = baseUrl + "/sendHappyBirthday";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = this.buildAuthTokenEmail(token);
        HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(emailRequest, headers);
        ResponseEntity<EmailResponse> response = template.exchange(uri, HttpMethod.POST, requestEntity,
            EmailResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully user creation: {}", response.getBody().getEnviado());
            return response.getBody();
        }
        log.error("Error in user creation - httpStatus was: {}", response.getStatusCode());
        throw new RuntimeException("Error");
    }

    public EmailResponse sendProductosBajoStock(String token, EmailRequest emailRequest) {
        emailRequest.setEmail(emailRecibirAlertaProductosBajoStock);
        String uri = baseUrl + "/sendHappyBirthday";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = this.buildAuthTokenEmail(token);
        HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(emailRequest, headers);
        ResponseEntity<EmailResponse> response = template.exchange(uri, HttpMethod.POST, requestEntity,
            EmailResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully user creation: {}", response.getBody().getEnviado());
            return response.getBody();
        }
        log.error("Error in user creation - httpStatus was: {}", response.getStatusCode());
        throw new RuntimeException("Error");
    }

}
