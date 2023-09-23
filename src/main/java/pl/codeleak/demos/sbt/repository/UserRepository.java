package pl.codeleak.demos.sbt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.codeleak.demos.sbt.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    User findUserById(Integer id);
    @Transactional
    @Modifying
    @Query(value = "update users set dias_restantes =:diasRestantes where user_id =:id", nativeQuery = true)
    void updateDiasRestantes(@Param("diasRestantes") Integer diasRestantes, @Param("id") Integer id);
}