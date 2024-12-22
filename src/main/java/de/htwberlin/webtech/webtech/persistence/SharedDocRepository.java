package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.SharedDoc;
import de.htwberlin.webtech.webtech.model.DocumentUserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedDocRepository extends CrudRepository<SharedDoc, DocumentUserId> {
}