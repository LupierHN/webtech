package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    public Iterable<Document> getDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Get all documents of a user
     *
     * @param user User
     * @return Set<Document>
     */
    public Iterable<Document> getUserDocuments(User user) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> document.getOwner().getUId() == user.getUId())
                .collect(Collectors.toSet());
    }


    /**
     * Get all documents of a user by document type
     *
     * @param user User
     * @param docType Document Type
     * @return Set<Document>
     */
    public Iterable<Document> getDocuments(final String docType, User user) {
        return StreamSupport.stream(this.getUserDocuments(user).spliterator(), false)
                .filter(document -> document.getDocType() != null && document.getDocType().equalsIgnoreCase(docType))
                .collect(Collectors.toSet());
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
        String format = "yyyy-MM-dd";
        DateFormat df = new java.text.SimpleDateFormat(format);
        Date now = new Date();
        document.setDocDate(df.format(now));
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
        return addDocument(document);
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
}
