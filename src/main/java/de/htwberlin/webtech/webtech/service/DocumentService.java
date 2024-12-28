package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    /**
     * Get all documents
     *
     * @return Iterable<Document>
     */
    public Iterable<Document> getDocuments() { return this.documentRepository.findAll(); }

    /**
     * Get all documents of a user
     *
     * @param user User
     * @return Set<Document>
     */
    public Iterable<Document> getUserDocuments(User user) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> Objects.equals(document.getOwner().getUId(), user.getUId()))
                .sorted(Comparator.comparingInt(Document::getDocId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * Get all documents of a user by document type
     *
     * @param user User
     * @param docType Document Type
     * @return Set<Document>
     */
    public Iterable<Document> getUserDocuments(final String docType, User user) {
        return StreamSupport.stream(this.getUserDocuments(user).spliterator(), false)
                .filter(document -> document.getDocType() != null && document.getDocType().equalsIgnoreCase(docType))
                .sorted(Comparator.comparingInt(Document::getDocId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * Get a document by id
     *
     * @param id Document ID
     * @param user User
     * @return Document
     */
    public Optional<Document> getDocument(final int id, User user) {
        Optional<Document> document = this.documentRepository.findById(id);
        if (document.isPresent()) {
            if (Objects.equals(document.get().getOwner().getUId(), user.getUId())) {
                return document;
            }
        }
        return Optional.empty();
    }

    /**
     * Get all documents shared with other users
     *
     * @param user User
     * @return Set<Document>
     */
    public Iterable<Document> getSharedDocuments(final User user) {
        Iterable<Document> docs = this.getUserDocuments(user);
        return StreamSupport.stream(docs.spliterator(), false)
                .filter(document -> document.getSharedWith() != null && !document.getSharedWith().isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Add a document
     *
     * @param document
     * @return
     */
    public Document addDocument(final Document document) {
        return this.documentRepository.save(document);
    }

    /**
     * Edit a document
     *
     * @param document Document
     * @return Document
     */
    public Document editDocument(final Document document) {
        if (!this.documentRepository.existsById(document.getDocId())) return null;
        Optional<Document> db_document = this.documentRepository.findById(document.getDocId());
        db_document.ifPresent(value -> document.setContent(value.getContent()));
        return addDocument(document);
    }


    /**
     * Get the content of a document
     *
     * @param id Document ID
     * @param user User
     * @return Optional<Document>
     */
    public Optional<Document> getDocumentContent(int id, User user) {
        Optional<Document> document = this.documentRepository.findById(id);
        if (document.isPresent() && document.get().getOwner().getUId().equals(user.getUId())) {
            return document;
        }
        return Optional.empty();
    }


    /**
     * Set the content of a document
     *
     * @param id Document ID
     * @param content String with Content
     * @param user User
     */
    public void setDocumentContent(int id, String content, User user) {
        Optional<Document> document = this.documentRepository.findById(id);
        if (document.isPresent() && document.get().getOwner().getUId().equals(user.getUId())) {
            Document doc = document.get();
            doc.setContent(content);
            this.documentRepository.save(doc);
        }
    }

    /**
     * Remove a document
     *
     * @param id Document ID
     * @param user User
     * @return Boolean
     */
    public boolean removeDocument(final int id, User user) {
        final Optional<Document> document = getDocument(id, user);
        if (document.isPresent()) {
                this.documentRepository.deleteById(id);
        }
        return document.isPresent();
    }

    /**
     * Search for documents by name or content
     *
     * @param search String
     * @param user User
     * @return Iterable<Document>
     */
    public Iterable<Document> searchDocuments(String search, User user) {
        String lowerCaseSearch = search.toLowerCase();
        Iterable<Document> docs = this.getUserDocuments(user);
        return StreamSupport.stream(docs.spliterator(), false)
                .filter(document -> document.getName().toLowerCase().contains(lowerCaseSearch) || document.getContent().toLowerCase().contains(lowerCaseSearch))
                .collect(Collectors.toSet());
    }

    //JUST FOR TESTING
    public void removeAllDocuments() {
        this.documentRepository.deleteAll();
    }
}
