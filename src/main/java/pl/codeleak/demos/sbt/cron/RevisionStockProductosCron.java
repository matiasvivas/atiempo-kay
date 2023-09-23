package pl.codeleak.demos.sbt.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.codeleak.demos.sbt.ClientesWS.ProductosBajoStockServiceWS;
import pl.codeleak.demos.sbt.model.Producto;
import pl.codeleak.demos.sbt.repository.ProductosRepository;

@Component
public class RevisionStockProductosCron {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ProductosBajoStockServiceWS productosBajoStockServiceWS;
    @Autowired
    ProductosRepository productosRepository;

        @Scheduled(cron="* * 2 * * *")
        public void doSomething() {
            System.out.println("Revision de Stock de Productos <5 con prioridad 1 todos los días 2am: "+dateFormat.format(new Date()));

        List<Producto> productosConBajoStock = productosRepository.obtenerProductosConBajoStockPrioridad1ParaMail();
        if(productosConBajoStock!=null&&productosConBajoStock.size()>0){
            productosBajoStockServiceWS.enviarMailProductosBajoStockPrioridad1(productosConBajoStock);
        }

        }
}
