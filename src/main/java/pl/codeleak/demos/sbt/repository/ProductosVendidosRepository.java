package pl.codeleak.demos.sbt.repository;

import org.springframework.data.repository.CrudRepository;
import pl.codeleak.demos.sbt.model.ProductoVendido;

public interface ProductosVendidosRepository extends CrudRepository<ProductoVendido, Integer> {

}
