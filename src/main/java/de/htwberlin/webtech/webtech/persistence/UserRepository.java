package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
