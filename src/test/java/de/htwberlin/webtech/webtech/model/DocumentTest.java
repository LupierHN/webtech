package de.htwberlin.webtech.webtech.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document(1, "Test Document", "/path/to/doc", "pdf", "content", "2023-10-10", null, null);
        assertNotNull(document);
        assertEquals(1, document.getDocId());
        assertEquals("Test Document", document.getName());
    }

    @Test
    public void testDocumentSetterGetter() {
        Document document = new Document();
        document.setName("New Document");
        document.setPath("/new/path/to/doc");
        assertEquals("New Document", document.getName());
        assertEquals("/new/path/to/doc", document.getPath());
    }
}