package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
}
