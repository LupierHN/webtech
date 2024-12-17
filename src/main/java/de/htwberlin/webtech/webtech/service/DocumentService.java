package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Iterable<Document> getDocuments() {
        return repository.findAll();
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
        Optional<Document> document = this.repository.findById(id);
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
        return this.repository.save(document);
    }

    public Document editDocument(final Document document) {
        if (!this.repository.existsById(document.getDocId())) return null;

        return addDocument(document);
    }

    public boolean removeDocument(final int id, User user) {
        final Optional<Document> document = getDocument(id, user);
        if (document.isPresent()) {
                this.repository.deleteById(id);
        }
        return document.isPresent();
    }
}
