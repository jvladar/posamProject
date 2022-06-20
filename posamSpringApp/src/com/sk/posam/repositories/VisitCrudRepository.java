package sk.posam.repositories;

import sk.posam.domain.Visit;
import org.springframework.data.repository.CrudRepository;


public interface VisitCrudRepository extends CrudRepository<Visit,Integer> {
}
