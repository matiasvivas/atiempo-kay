package pl.codeleak.demos.sbt.cron;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.codeleak.demos.sbt.ClientesWS.ProductosPorVencerServiceWS;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.repository.ProductosRepository;

@Component
public class RevisionProductosPorVencerCron {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ProductosPorVencerServiceWS productosPorVencerServiceWS;

    @Autowired
    ProductosRepository productosRepository;

        @Scheduled(cron="* * 8 * * *")
        public void doSomething() {
            System.out.println("Revision de vencimientos de productos 8am "+dateFormat.format(new Date()));

            Date fechaHoy = new Date();
            int diasDeAvisoAmarillo = 10;
            Date fechaAmarillo = sumarDiasFecha(fechaHoy, diasDeAvisoAmarillo);
            List<Producto> productosPorVencer = productosRepository.obtenerProductosAVencerAmarillo(fechaHoy, fechaAmarillo);
            if(productosPorVencer!=null&&productosPorVencer.size()>0){
                productosPorVencerServiceWS.enviarMailProductosPorVencer10Dias(productosPorVencer);
            }
        }

    public Date sumarDiasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}
