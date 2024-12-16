package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

        @Autowired
        private UserRepository repository;

        public User registerUser(User user) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
            return repository.save(user);
        }

        public Iterable<User> getUsers() {
                return repository.findAll();
        }

        public User getUser(int id) {
                return repository.findById(id).orElse(null);
        }

        public User loginUser(User user) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                User user_db = repository.findById(user.getUId()).orElse(null);
                try {
                        if (encoder.matches(user.getPassword(), user_db.getPassword())) return user_db;
                        else return null;
                }catch (Exception e) {
                        return null;
                }
        }

        public boolean findUser(String username) {
                return repository.findByUsername(username).isPresent();
        }

        public User updateUser(User user) {
                User user_db = repository.findById(user.getUId()).orElse(null);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
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
