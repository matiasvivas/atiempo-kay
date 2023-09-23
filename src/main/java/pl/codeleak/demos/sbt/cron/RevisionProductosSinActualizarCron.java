package pl.codeleak.demos.sbt.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RevisionProductosSinActualizarCron {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        //@Scheduled(cron="* * * * * *")
        public void doSomething() {
            System.out.println("Hora Actual AA "+dateFormat.format(new Date()));
        }

}
