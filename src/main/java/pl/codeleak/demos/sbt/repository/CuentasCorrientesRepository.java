package pl.codeleak.demos.sbt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import pl.codeleak.demos.sbt.model.Venta;

@Service
public interface CuentasCorrientesRepository extends CrudRepository<Venta, Integer> {

    @Query("from Venta v where v.clienteCuentaCorriente is not null and v.pagoCuentaCorriente is not null and v.fechaCancelacionCuentaCorriente is null and v.clienteCuentaCorriente =:cliente")
    List<Venta> obtenerDeudasDeCliente(@Param("cliente") Integer cliente);

    @Query("from Venta v where v.clienteCuentaCorriente is not null and v.pagoCuentaCorriente is not null and v.fechaCancelacionCuentaCorriente is null and v.clienteCuentaCorriente =:cliente and v.username='SaldoAFavor'")
    Venta obtenerSaldoAFavor(@Param("cliente") Integer cliente);

}
