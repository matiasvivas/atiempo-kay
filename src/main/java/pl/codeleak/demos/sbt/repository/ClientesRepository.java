package pl.codeleak.demos.sbt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.Cliente;

public interface ClientesRepository extends CrudRepository<Cliente, Integer> {
    Cliente findFirstByNumeroDocumento(String numeroDocumento);

    @Query("from Cliente c where c.activo is true order by c.nombre")
    List<Cliente> findAllOrderByAZ();
    @Query("from Cliente c where c.activo is true and c.cuentaCorriente is true and c.estado =2 order by c.nombre")
    List<Cliente> findClientesActivosYAprobadosCuentaCorrienteOrderByAZ();

    @Query("from Cliente c where c.activo is true and c.cuentaCorriente is true and c.estado =0 order by c.nombre")
    List<Cliente> findAllPendientesCuentaCorrienteOrderByAZ();
    @Query("from Cliente c where c.activo is true and c.fechaCumpleanos=:cumple")
    List<Cliente> obtenerClientesCumplenAnosHoy(@Param("cumple") Date cumple);
}
