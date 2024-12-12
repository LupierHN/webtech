package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.service.DocumentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Document>> getDocuments(@RequestParam final Optional<String> docType) {
        final Iterable<Document> result = docType.isEmpty() || docType.get().isBlank()
                ? documentService.getDocuments()
                : documentService.getDocuments(docType.get());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable("id") final int id) {
        final Optional<Document> documentOptional = documentService.getDocument(id);
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(documentOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") final int id) {
        final boolean removed = documentService.removeDocument(id);
        if (removed) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@Valid @RequestBody final Document document) {
        final Document created = documentService.addDocument(document);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Document> editDocument(@Valid @RequestBody final Document document) {
        final Document edited = documentService.editDocument(document);
        if (edited == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(edited);
    }
}
