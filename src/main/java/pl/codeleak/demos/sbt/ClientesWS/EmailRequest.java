package pl.codeleak.demos.sbt.ClientesWS;

import lombok.Data;

@Data
public class EmailRequest {
    private String email;
    private String content;
    private String subject;
}
