package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.DocumentUserId;
import de.htwberlin.webtech.webtech.model.SharedDoc;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import de.htwberlin.webtech.webtech.persistence.SharedDocRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SharedDocRepository sharedDocRepository;


    public Iterable<Document> getDocuments() {
        return documentRepository.findAll();
    }

    public Iterable<Document> getUserDocuments(User user) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> document.getOwner().getUId() == user.getUId())
                .collect(Collectors.toSet());
    }

    public Iterable<Document> getDocuments(final String docType) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> document.getDocType() != null && document.getDocType().equalsIgnoreCase(docType))
                .collect(Collectors.toSet());
    }

    public Optional<Document> getDocument(final int id, User user) {
        Optional<Document> document = this.documentRepository.findById(id);
        if (document.isPresent()) {
            if (Objects.equals(document.get().getOwner().getUId(), user.getUId())) {
                return document;
            }
        }
        return Optional.empty();
    }

    public Document addDocument(final Document document) {
        String format = "yyyy-MM-dd";
        DateFormat df = new java.text.SimpleDateFormat(format);
        Date now = new Date();
        document.setDocDate(df.format(now));
        return this.documentRepository.save(document);
    }

    public Document editDocument(final Document document) {
        if (!this.documentRepository.existsById(document.getDocId())) return null;

        return addDocument(document);
    }

    public boolean removeDocument(final int id, User user) {
        final Optional<Document> document = getDocument(id, user);
        if (document.isPresent()) {
                this.documentRepository.deleteById(id);
        }
        return document.isPresent();
    }

    public Set<Document> getSharedDocumentsByUser(User user) {
        return user.getSharedDocuments().stream()
                .map(SharedDoc::getDocument)
                .collect(Collectors.toSet());
    }

    public boolean shareDocument(int documentId, int userId, User user) {
        Optional<Document> document = this.documentRepository.findById(documentId);
        if (document.isEmpty()) return false;
        Optional<User> sharedUser = this.userRepository.findById(userId);
        if (sharedUser.isEmpty()) return false;

        DocumentUserId documentUserId = new DocumentUserId(documentId, userId);
        SharedDoc sharedDoc = new SharedDoc();
        sharedDoc.setId(documentUserId);
        sharedDoc.setDocument(document.get());
        sharedDoc.setUser(sharedUser.get());
        sharedDoc.setCanEdit(false);

        this.sharedDocRepository.save(sharedDoc);
        return true;
    }
}
