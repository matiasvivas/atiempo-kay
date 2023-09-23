package pl.codeleak.demos.sbt.ClientesWS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EmailResponse {
    public HttpStatus status;

    @JsonProperty("enviado")
    public Boolean enviado;
}
