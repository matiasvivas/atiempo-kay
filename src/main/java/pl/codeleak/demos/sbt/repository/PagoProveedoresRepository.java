package pl.codeleak.demos.sbt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.PagoProveedores;

public interface PagoProveedoresRepository extends CrudRepository<PagoProveedores,Integer> {

    @Query("SELECT id as id, proveedor as proveedor, TO_CHAR(fecha, 'DD-MM') as fecha2, montoPedido as monto, montoSaldoPendiente as montoPendiente FROM PagoProveedores WHERE estado = 0 ORDER BY fecha")
    List<PagoProveedores> mostrarProveedoresProgramadosPENDIENTES();
    @Query("from PagoProveedores where id =:codProveedor")
    PagoProveedores obtenerpagoProveedorPorCodigo(@Param("codProveedor") Integer codProveedor);
}
