package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

        @Autowired
        private UserRepository repository;

        public User registerUser(User user) {
            return repository.save(user);
        }

        public Iterable<User> getUsers() {
                return repository.findAll();
        }

        public User getUser(int id) {
                return repository.findById(id).orElse(null);
        }

        public boolean findUser(String username) {
                return repository.findByUsername(username).isPresent();
        }

        public User updateUser(User user) {
                return repository.save(user);
        }

        public boolean deleteUser(int id) {
                if (repository.existsById(id)) {
                        repository.deleteById(id);
                        return true;
                }
                return false;
        }

}
