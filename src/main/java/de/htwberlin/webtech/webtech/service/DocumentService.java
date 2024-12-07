package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.Document;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Iterable<Document> getDocumemts() {
        return repository.findAll();
    }

    public Iterable<Document> getDocuments(final String affiliation) {
        return StreamSupport.stream(this.getDocuments().spliterator(), false)
                .filter(document -> document.getAffiliation().equals(affiliation))
                .toList();
    }
}
