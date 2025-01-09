package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.DocumentRepository;
import de.htwberlin.webtech.webtech.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @MockBean
    private DocumentRepository documentRepository;

    private User user;
    private Document document;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUId(1);
        user.setUsername("testuser");

        document = new Document();
        document.setDocId(1);
        document.setName("Test Document");
        document.setOwner(user);
    }

    @Test
    @DisplayName("Test for getting all documents")
    public void testGetDocuments() {
        Mockito.when(documentRepository.findAll()).thenReturn(Set.of(document));

        Iterable<Document> documents = documentService.getDocuments();
        assertNotNull(documents);
        assertTrue(documents.iterator().hasNext());
    }

    @Test
    @DisplayName("Test for getting a document by ID")
    public void testGetDocumentById() {
        Mockito.when(documentRepository.findById(anyInt())).thenReturn(Optional.of(document));

        Optional<Document> foundDocument = documentService.getDocument(1, user);
        assertTrue(foundDocument.isPresent());
        assertEquals(document.getDocId(), foundDocument.get().getDocId());
    }

    @Test
    @DisplayName("Test for adding a document")
    public void testAddDocument() {
        Mockito.when(documentRepository.save(any(Document.class))).thenReturn(document);

        Document savedDocument = documentService.addDocument(document);
        assertNotNull(savedDocument);
        assertEquals(document.getDocId(), savedDocument.getDocId());
    }

    @Test
    @DisplayName("Test for editing a document")
    public void testEditDocument() {
        Mockito.when(documentRepository.existsById(anyInt())).thenReturn(true);
        Mockito.when(documentRepository.findById(anyInt())).thenReturn(Optional.of(document));
        Mockito.when(documentRepository.save(any(Document.class))).thenReturn(document);

        Document updatedDocument = documentService.editDocument(document);
        assertNotNull(updatedDocument);
        assertEquals(document.getDocId(), updatedDocument.getDocId());
    }

    @Test
    @DisplayName("Test for removing a document")
    public void testRemoveDocument() {
        Mockito.when(documentRepository.findById(anyInt())).thenReturn(Optional.of(document));

        boolean isRemoved = documentService.removeDocument(1, user);
        assertTrue(isRemoved);
    }
}