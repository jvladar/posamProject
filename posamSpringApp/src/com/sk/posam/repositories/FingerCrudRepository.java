package sk.posam.repositories;


import org.springframework.data.repository.CrudRepository;
import sk.posam.domain.Fingerprint;

public interface FingerCrudRepository extends CrudRepository<Fingerprint,Integer> {

}
