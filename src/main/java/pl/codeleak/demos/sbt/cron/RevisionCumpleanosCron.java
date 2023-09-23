package pl.codeleak.demos.sbt.cron;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.codeleak.demos.sbt.ClientesWS.CumpleanosServiceWS;
import pl.codeleak.demos.sbt.model.Cliente;
import pl.codeleak.demos.sbt.repository.ClientesRepository;

@Component
public class RevisionCumpleanosCron {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    ClientesRepository clientesRepository;
    @Autowired
    CumpleanosServiceWS cumpleanosServiceWS;

        @Scheduled(cron="* * 7 * * *")
        public void checkBirthdays() throws ParseException {
            System.out.println("Revisión de cumpleaños de Clientes 7am: " + dateFormat.format(new Date()));
            SimpleDateFormat dateFormatCumple = new SimpleDateFormat("1970-MM-dd 03:00:00");
            Date hoy = new Date();
            String cumpleHoy = dateFormatCumple.format(hoy);
            cumpleHoy = cumpleHoy.substring(5, 10);
            cumpleHoy = "1970-" + cumpleHoy + " 03:00:00";
            Date fechaFormateada = dateFormatCumple.parse(cumpleHoy);
            List<Cliente> clientesCumplenAnosHoy = clientesRepository.obtenerClientesCumplenAnosHoy(fechaFormateada);
            for (Cliente clienteCumpleHoy : clientesCumplenAnosHoy) {
                cumpleanosServiceWS.enviarMailFelizCumpleanos(clienteCumpleHoy);
            }
        }
    }
