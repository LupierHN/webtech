package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Iterable<Document> getDocuments(final String docType) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> document.getDocType() != null && document.getDocType().equalsIgnoreCase(docType))
                .collect(Collectors.toSet());
    }

    public Optional<Document> getDocument(final int id) {
        return this.repository.findById(id);
    }

    public Document addDocument(final Document document) {
        return this.repository.save(document);
    }

    public Document editDocument(final Document document) {
        if (!this.repository.existsById(document.getDocId())) return null;

        return addDocument(document);
    }

    public boolean removeDocument(final int id) {
        final boolean exists = this.repository.existsById(id);
        if (exists) this.repository.deleteById(id);
        return exists;
    }
}
