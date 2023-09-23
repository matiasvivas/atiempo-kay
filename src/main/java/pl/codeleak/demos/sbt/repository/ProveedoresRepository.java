package pl.codeleak.demos.sbt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.codeleak.demos.sbt.model.Proveedor;

public interface ProveedoresRepository extends CrudRepository<Proveedor, Integer> {
    Proveedor findFirstByNombre(String nombre);
    @Query("from Proveedor p where p.activo is true order by p.nombre")
    List<Proveedor> findAllOrderByAZ();
}
