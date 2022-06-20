package sk.posam.repositories;


import org.springframework.data.repository.CrudRepository;
import sk.posam.domain.Patient;

public interface PatientCrudRepository extends CrudRepository<Patient,Integer> {
}
