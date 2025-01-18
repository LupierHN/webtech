package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.HistoryElement;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.HistoryElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HistoryElementService {

    @Autowired
    private HistoryElementRepository repository;

    /**
     * Add a new history element
     * - the oldes history element will be deleted if the history element list is longer than 5
     *
     * @param user     User the history element belongs to
     * @param document Document the history element
     * @return HistoryElement the new history element
     */
    public HistoryElement addHistoryElement(User user, Document document) {
        List<HistoryElement> knownHistoryElements = repository.findAllByDocumentAndUser(document, user);
        if (knownHistoryElements != null && !knownHistoryElements.isEmpty()) {
            return knownHistoryElements.getFirst();
        }
        HistoryElement historyElement = new HistoryElement();
        historyElement.setUser(user);
        historyElement.setDocument(document);
        historyElement.setTimestamp(new Date());
        List<HistoryElement> historyElements = repository.findAllByUserOrderByTimestampDesc(user);
        if (historyElements.size() >= 5) {
            repository.delete(historyElements.getLast());
        }
        return repository.save(historyElement);
    }

    /**
     * Get all history elements of a user
     *
     * @param user User
     * @return List<HistoryElement>
     */
    public List<HistoryElement> getHistoryElements(User user) {
        return repository.findAllByUserOrderByTimestampDesc(user);
    }

    /**
     * Delete all history elements of a user
     *
     * @param user User
     */
    public void deleteHistoryElements(User user) {
        List<HistoryElement> historyElements = repository.findAllByUserOrderByTimestampDesc(user);
        repository.deleteAll(historyElements);
    }

    /**
     * Delete a history element by document
     *
     * @param document Document
     */
    public void deleteHistoryElementByDocument(Document document) {
        List<HistoryElement> historyElements = repository.findAllByDocument(document);
        if (historyElements != null && !historyElements.isEmpty()) repository.deleteAll(historyElements);
    }

    /**
     * Delete a history element
     *
     * @param histId History element id
     * @param user User
     */
    public void deleteHistoryElement(int histId, User user) {
        HistoryElement historyElement = repository.findById(histId).orElse(null);
        if (historyElement != null) {
            repository.delete(historyElement);
        }else {
            throw new IllegalArgumentException("History element not found");
        }
    }

    /**
     * Delete all history elements of a document
     *
     * @param document
     */
    public void deleteHistoryElementsByDocument(Document document) {
        repository.deleteAll(repository.findAllByDocument(document));
    }
}
