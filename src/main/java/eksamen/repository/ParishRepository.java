package eksamen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eksamen.model.Parish;

public interface ParishRepository extends CrudRepository<Parish, Long> {
    @Query("SELECT s FROM sogn s WHERE s.parishCode = :parishCode")
    Optional<Parish> findByParishCode(@Param("parishCode") int parishCode);
}
