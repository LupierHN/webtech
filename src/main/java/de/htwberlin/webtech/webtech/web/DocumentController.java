package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.UserRepository;
import de.htwberlin.webtech.webtech.service.DocumentService;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
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
    private final UserService userService;

    //JUST FOR TESTING
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Document>> getDocuments(@RequestParam final Optional<String> docType) {
        final Iterable<Document> result = docType.isEmpty() || docType.get().isBlank()
                ? documentService.getDocuments()
                : documentService.getDocuments(docType.get());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Document>> getUserDocuments(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        final Iterable<Document> result = documentService.getUserDocuments(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable("id") final int id, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final Optional<Document> documentOptional = documentService.getDocument(id, user);
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(documentOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") final int id, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final boolean removed = documentService.removeDocument(id, user);
        if (removed) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Document> addDocument(@Valid @RequestBody final Document document, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        document.setOwner(TokenUtility.getUserFromHeader(authHeader, userService));
        final Document created = documentService.addDocument(document);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Document> editDocument(@Valid @RequestBody final Document document, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        document.setOwner(TokenUtility.getUserFromHeader(authHeader, userService));
        final Document edited = documentService.editDocument(document);
        if (edited == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(edited);
    }
}
