package pl.codeleak.demos.sbt.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.codeleak.demos.sbt.model.Codigo;
import pl.codeleak.demos.sbt.model.User;
import pl.codeleak.demos.sbt.model.Utiles;
import pl.codeleak.demos.sbt.repository.CodigosRepository;
import pl.codeleak.demos.sbt.repository.UserRepository;

@Component
public class RevisionCodigosCron {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private CodigosRepository codigosRepository;

    @Autowired
    private UserRepository userRepository;

        @Scheduled(cron="* * 6 * * *")
        public void doSomething() {
            System.out.println("Revisión 6am códigos días restantes: "+dateFormat.format(Utiles.obtenerFechaYHoraActualDate()));
            List<Codigo> codigosActivacionVigentes = codigosRepository.obtenerCodigosActivacionVigentes();
            for(Codigo codigo:codigosActivacionVigentes){
                codigosRepository.restar1diaCodigosActivacion(codigo.getDiasRestantes()-1,codigo.getId());
                User userExists = userRepository.findUserById(codigo.getUserId());
                if(userExists.getDiasRestantes()!=null) {
                    userExists.setDiasRestantes(codigo.getDiasRestantes() - 1);
                    userRepository.updateDiasRestantes(userExists.getDiasRestantes(), userExists.getId());
                }

            }
        }

    @Scheduled(cron="* * * 1 * *")
    public void cleanOldCodes() {
        System.out.println("Eliminación de códigos antiguos de Activación: "+dateFormat.format(Utiles.obtenerFechaYHoraActualDate()));
        List<Codigo> codigosActivacionAntiguosYaUtilizados = codigosRepository.obtenerCodigosDeActivacionYaUtilizados();
        for(Codigo codigo:codigosActivacionAntiguosYaUtilizados){
            codigosRepository.delete(codigo);
            User userExists = userRepository.findUserById(codigo.getUserId());
            userExists.setDiasRestantes(null);
            userRepository. updateDiasRestantes(userExists.getDiasRestantes(), userExists.getId());

        }
    }

}
