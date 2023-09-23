package pl.codeleak.demos.sbt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.codeleak.demos.sbt.model.CajaFuerte;

public interface IngresosCajaFuerteRepository extends CrudRepository<CajaFuerte, Integer> {
    @Query("from CajaFuerte c order by c.id desc")
    List<CajaFuerte> mostrarMontoActualCajaFuerte();

}
