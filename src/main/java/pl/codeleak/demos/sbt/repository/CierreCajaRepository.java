package pl.codeleak.demos.sbt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.CierreCaja;

public interface CierreCajaRepository extends CrudRepository <CierreCaja,Integer>{
    @Query("from CierreCaja c where c.username =:userName")
    CierreCaja findLastByUsername(@Param("userName")String username);

    @Query("from CierreCaja order by id desc")
    List<CierreCaja> findLast7Days();
}
