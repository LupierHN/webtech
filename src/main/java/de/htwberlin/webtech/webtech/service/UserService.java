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

        /**
         * Register a new user
         *
         * @param user User
         * @return User
         */
        public User registerUser(User user) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
            return repository.save(user);
        }

        /**
         * Get all users
         *
         * @return Iterable<User>
         */
        public Iterable<User> getUsers() {
                return repository.findAll();
        }

        /**
         * Get a user by id
         *
         * @param id int
         * @return User
         */
        public User getUser(int id) {
                return repository.findById(id).orElse(null);
        }

        /**
         * Get a user by username
         *
         * @param username String
         * @return User
         */
        public User getUserByUsername(String username) {return repository.findByUsername(username).orElse(null);}

        /**
         * Login a user
         *
         * @param user User with email and password necessary
         * @return User
         */
        public User loginUser(User user) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                User user_db;
                if (user.getEmail() != null) {
                        user_db = repository.findByEmail(user.getEmail()).orElse(null);
                } else if (user.getUsername() != null) {
                        user_db = repository.findByUsername(user.getUsername()).orElse(null);
                }else return null;
                try {
                        assert user_db != null;
                        if (encoder.matches(user.getPassword(), user_db.getPassword())) return user_db;
                        else return null;
                }catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                }
        }

        /**
         * Check if a user exists by username
         *
         * @param username String
         * @return Boolean exists
         */
        public boolean findUser(String username) {
                return repository.findByUsername(username).isPresent();
        }

        /**
         * Check if a user exists by email
         *
         * @param email String
         * @return Boolean exists
         */
        public boolean findUserE(String email) {return repository.findByEmail(email).isPresent();}

        /**
         * Update a users:
         * password
         * - email
         * - username
         * - first name
         * - last name
         *
         * @param user User
         * @return User updated user
         */
        public User updateUser(User user) {
                User user_db = repository.findById(user.getUId()).orElse(null);
                if (user_db == null) return null;
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user_db.setPassword(encoder.encode(user.getPassword()));
                user_db.setEmail(user.getEmail());
                user_db.setUsername(user.getUsername());
                user_db.setFirstName(user.getFirstName());
                user_db.setLastName(user.getLastName());
                return repository.save(user_db);
        }

        /**
         * Update a users shared documents
         *
         * @param user User
         */
        public void updateUserSharedDocuments(User user) {
                User user_db = repository.findById(user.getUId()).orElse(null);
                if (user_db == null) return;
                user_db.setSharedDocuments(user.getSharedDocuments());
        }


        /**
         * Delete a user
         *
         * @param id int
         * @return Boolean
         */
        public boolean deleteUser(int id) {
                if (repository.existsById(id)) {
                        repository.deleteById(id);
                        return true;
                }
                return false;
        }

}
