package sk.posam.repositories;


import sk.posam.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserCrudRepository extends CrudRepository<User,Integer> {

}
