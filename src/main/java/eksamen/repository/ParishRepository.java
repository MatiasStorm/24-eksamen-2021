package eksamen.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eksamen.model.Parish;

public interface ParishRepository extends CrudRepository<Parish, Integer> {}
