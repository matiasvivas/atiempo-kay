package pl.codeleak.demos.sbt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.codeleak.demos.sbt.model.Codigo;

public interface CodigosRepository extends CrudRepository<Codigo,Integer> {
    Codigo findFirstBySerial(String serial);

    @Query(value= "from Codigo where fechaActivacion is not null and diasRestantes is not null and diasRestantes >=0 and tipo =4 or tipo=5 or tipo=6")
    List<Codigo> obtenerCodigosActivacionVigentes();
    @Transactional
    @Modifying
    @Query(value = "update codigo set dias_restantes =:diasRestantes where id =:id", nativeQuery = true)
    void restar1diaCodigosActivacion(@Param("diasRestantes")Integer diasRestantes, @Param("id") Integer id);

    @Query(value= "from Codigo where fechaActivacion is not null and diasRestantes =-1 and tipo =4 or tipo=5 or tipo=6")
    List<Codigo> obtenerCodigosDeActivacionYaUtilizados();
    @Query(value= "from Codigo where concepto='Token WS Cumpleanos' and tipo =7")
    Codigo getTokenCumpleanos();

    @Query(value= "from Codigo where concepto='Token WS Productos Por Vencer' and tipo =7")
    Codigo getTokenProductosPorVencer();
    @Query(value= "from Codigo where concepto='Token WS Productos Bajo Stock' and tipo =7")
    Codigo getTokenProductosBajoStockParaMail();
}
