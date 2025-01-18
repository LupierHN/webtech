package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.HistoryElement;
import de.htwberlin.webtech.webtech.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryElementRepository extends CrudRepository<HistoryElement, Integer> {
    List<HistoryElement> findAllByUserOrderByTimestampDesc(User user);
    List<HistoryElement> findAllByDocument(Document document);
}
