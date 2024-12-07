package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer>{
}
