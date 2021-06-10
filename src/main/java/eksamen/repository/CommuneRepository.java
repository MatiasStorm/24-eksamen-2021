package eksamen.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eksamen.model.Commune;

public interface CommuneRepository extends CrudRepository<Commune, Integer> {

}
