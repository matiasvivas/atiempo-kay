package pl.codeleak.demos.sbt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.EgresosCaja;

public interface EgresosCajaRepository extends CrudRepository<EgresosCaja,Integer> {

    @Query("FROM EgresosCaja e WHERE e.username = :username AND e.revisado = false")
    List<EgresosCaja> mostrarEgresosHoyPorUsuario(@Param("username") String username);

    @Query("FROM EgresosCaja e WHERE e.revisado = false")
    List<EgresosCaja> mostrarTodosLosEgresosSinRevisar();

    @Query("from EgresosCaja e where e.id =:id")
     EgresosCaja marcarEgresoComoRevisado(@Param("id") Integer id);

    @Query("SELECT SUM(e.monto) FROM EgresosCaja e WHERE e.revisado = false AND e.fechaHoraEgreso BETWEEN :fechaHoy AND :fechaManiana")
    //@Query("SELECT SUM(e.monto) FROM EgresosCaja e WHERE NOT e.revisado AND e.fechaHoraEgreso BETWEEN :fechaHoy AND :fechaManiana")
    //@Query("select sum(e.monto) from EgresosCaja e where e.revisado is not true and e.fechaHoraEgreso >= :fechaHoy and e.fechaHoraEgreso < :fechaManiana")
    Float mostrarTotalEgresosHoy(@Param("fechaHoy") Date fechaHoy, @Param("fechaManiana")Date fechaManiana);
}