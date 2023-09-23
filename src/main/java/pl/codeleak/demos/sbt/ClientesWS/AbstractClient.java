package pl.codeleak.demos.sbt.ClientesWS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractClient {

    @Value("${webservices.email.base-url}")
    protected String baseUrl;

    @Value("${email.recibir.alerta.prod-x-vencer}")
    protected String emailRecibirAlertaProductosPorVencer;

    @Value("${email.recibir.alerta.bajo-stock}")
    protected String emailRecibirAlertaProductosBajoStock;

    protected final  RestTemplate restTemplate;

    protected AbstractClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected HttpHeaders buildAuthToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    protected HttpHeaders buildAuthTokenEmail(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-token", token);
        return headers;
    }
}
