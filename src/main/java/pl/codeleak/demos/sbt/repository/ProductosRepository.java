package pl.codeleak.demos.sbt.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.Producto;

public interface ProductosRepository extends CrudRepository<Producto, Integer> {
    Producto findFirstByCodigo(String codigo);
    @Query("from Producto p where p.fechaVencimiento is not null and p.existencia >1 and p.fechaVencimiento between :fechaHoy and :fecha")
    List<Producto> obtenerProductosAVencerAmarillo(@Param("fechaHoy") Date fechaHoy, @Param("fecha") Date fecha);
    @Query("from Producto p where p.fechaVencimiento is not null and p.existencia >1 and p.fechaVencimiento between :fechaHoy and :fecha")
    List<Producto> obtenerProductosAVencerRojo(@Param("fechaHoy") Date fechaHoy,@Param("fecha") Date fecha);
    @Query("from Producto p where p.existencia <=5 and p.prioridad is not null order by p.prioridad")
    List<Producto> obtenerProductosConBajoStock();

    @Query("from Producto p where p.nombre like '%Coca Cola Retornable%' and p.existencia<=3 or p.nombre like '%Sprite Retornable%' and p.existencia <=3")
    List<Producto> obtenerProductosParaCocaColaMartes();

    @Query("from Producto p where p.existencia <=3 and p.nombre like '%Coca Cola Descartable%' or p.nombre like '%Sprite Descartable%' and p.existencia <=3 or p.nombre like '%Monster%' and p.existencia <=3 or p.nombre like '%Aquarius%' and p.existencia <=3 order by p.nombre")
    List<Producto> obtenerProductosParaCocaColaViernes();

    @Query("from Producto p where p.nombre like '%Cigarrillo%' and p.existencia <5 order by p.nombre")
    List<Producto> obtenerProductosParaCigarrillos();

    @Query("from Producto p where p.nombre not like '%ENVASE%' order by p.nombre")
    List<Producto> findAllOrderByAZ();

    @Query("from Producto p where p.existencia <=5 and p.prioridad = 1 order by p.nombre")
    List<Producto> obtenerProductosConBajoStockPrioridad1ParaMail();
    @Query("from Producto p where p.existencia >0 order by p.nombre")
    List<Producto> obtenerProductosVendibles();
    Producto findFirstById(int codigo);
}
