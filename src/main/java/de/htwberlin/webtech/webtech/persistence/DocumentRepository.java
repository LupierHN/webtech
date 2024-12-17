package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer>{
}
