package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
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

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final UserService userService;

    //JUST FOR TESTING
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Document>> getDocuments(@RequestParam final Optional<String> docType, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final Iterable<Document> result = docType.isEmpty() || docType.get().isBlank()
                ? documentService.getUserDocuments(user)
                : documentService.getDocuments(docType.get(), user);
        return ResponseEntity.ok(result);
    }

    //JUST FOR TESTING
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllDocuments() {
        documentService.removeAllDocuments();
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all documents of a user
     *
     * @param authHeader Authorization Header with access token
     * @return List of documents
     */
    @GetMapping("/all")
    public ResponseEntity<Iterable<Document>> getUserDocuments(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        final Iterable<Document> result = documentService.getUserDocuments(user);
        return ResponseEntity.ok(result);
    }

    /**
     * Get a document by id
     *
     * @param id Document ID
     * @param authHeader Authorization Header with access token
     * @return Document
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable("id") final int id, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final Optional<Document> documentOptional = documentService.getDocument(id, user);
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(documentOptional.get());
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<String> getDocumentContent(@PathVariable("id") final int id, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final Optional<Document> documentOptional = documentService.getDocumentContent(id, user);
        if (documentOptional.isPresent()) System.out.println("Document content: " + documentOptional.get().getContent());
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(documentOptional.get().getContent());
    }

    @PutMapping("/content/{id}")
    public ResponseEntity<String> setDocumentContent(@PathVariable("id") final int id, @RequestBody String content, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        documentService.setDocumentContent(id, content, user);
        return ResponseEntity.ok(content);
    }

    /**
     * Find a document by name or contentsnippet
     *
     * @param search String
     * @param authHeader Authorization Header with access token
     * @return List of documents
     */

    /**
     * Delete a document by id
     *
     * @param id Document ID
     * @param authHeader Authorization Header with access token
     * @return Status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") final int id, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        final boolean removed = documentService.removeDocument(id, user);
        if (removed) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    /**
     * Add a document
     *
     * @param document Document
     * @param authHeader Authorization Header with access token
     * @return Document
     */
    @PostMapping
    public ResponseEntity<Document> addDocument(@Valid @RequestBody final Document document, @RequestHeader("Authorization") String authHeader) {
        System.out.println("Document content: " + document.getContent());
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        document.setOwner(TokenUtility.getUserFromHeader(authHeader, userService));
        String format = "yyyy-MM-dd";
        DateFormat df = new java.text.SimpleDateFormat(format);
        Date now = new Date();
        document.setDocDate(df.format(now));
        final Document created = documentService.addDocument(document);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Edit a document
     *
     * @param document Document
     * @param authHeader Authorization Header with access token
     * @return Document
     */
    @PutMapping
    public ResponseEntity<Document> editDocument(@Valid @RequestBody final Document document, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        document.setOwner(TokenUtility.getUserFromHeader(authHeader, userService));
        if (document.getContent() == null) {
            System.err.println("Document content is null");
        } else {
            System.out.println("Document content: " + document.getContent());
        }
        final Document edited = documentService.editDocument(document);
        if (edited == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(edited);
    }

    /**
     * Get all shared documents of a user (shared with me)
     *
     * @param authHeader Authorization Header with access token
     * @return List of shared documents
     */
    @GetMapping("/shared")
    public ResponseEntity<Set<Document>> getSharedDocuments(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(user.getSharedDocuments());
    }

    /**
     * Get all documents shared with users (owned by me)
     *
     * @param authHeader Authorization Header with access token
     * @return List of shared documents
     */
    @GetMapping("/sharedWith")
    public ResponseEntity<Iterable<Document>> getSharedWithDocuments(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(documentService.getSharedDocuments(user));
    }

    /**
     * Share a document with a user
     *
     * @param docId DocumentID to share
     * @param uId UserID to share with
     * @param authHeader Authorization Header with access token
     * @return Status
     */
    @PostMapping("/share/{docId}/{uId}")
    public ResponseEntity<Void> shareDocument(@PathVariable("docId") final int docId, @PathVariable("uId") final int uId, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        final Optional<Document> documentOptional = documentService.getDocument(docId, user);
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        final User shareWith = userService.getUser(uId);
        if (shareWith == null) return ResponseEntity.notFound().build();
        final Document document = documentOptional.get();
        document.getSharedWith().add(shareWith);
        shareWith.getSharedDocuments().add(document);
        userService.updateUser(shareWith);
        documentService.editDocument(document);
        return ResponseEntity.noContent().build();
    }

    /**
     * Unshare a document with a user
     *
     * @param docId DocumentID to unshare
     * @param uId UserID to unshare with
     * @param authHeader Authorization Header with access token
     * @return Status
     */
    @DeleteMapping("/share/{docId}/{uId}")
    public ResponseEntity<Void> unshareDocument(@PathVariable("docId") final int docId, @PathVariable("uId") final int uId, @RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        final Optional<Document> documentOptional = documentService.getDocument(docId, user);
        if (!documentOptional.isPresent()) return ResponseEntity.notFound().build();
        final User shareWith = userService.getUser(uId);
        if (shareWith == null) return ResponseEntity.notFound().build();
        final Document document = documentOptional.get();
        document.getSharedWith().remove(shareWith);
        shareWith.getSharedDocuments().remove(document);
        userService.updateUser(shareWith);
        documentService.editDocument(document);
        return ResponseEntity.noContent().build();
    }
}
