package pl.codeleak.demos.sbt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.Venta;

import java.util.Date;

public interface VentasPublicoRepository extends CrudRepository<Venta, Integer> {
    @Query("from Venta v where v.fechaYHora >=:today and v.username =:username")
    Iterable<Venta> mostrarVentasDeHoy(@Param("today") Date today, @Param("username") String username);

    @Query("from Venta v where v.fechaYHora >=:today")
    Iterable<Venta> mostrarVentasDeHoyHistorico(@Param("today") Date today);
}
