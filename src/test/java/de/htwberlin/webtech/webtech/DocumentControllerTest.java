package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.service.DocumentService;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.web.DocumentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Test for getting all documents - Expected status: 200 OK")
    public void testGetDocuments() throws Exception {
        mockMvc.perform(get("/api/documents")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for adding a document - Expected status: 201 Created")
    public void testAddDocument() throws Exception {
        Document document = new Document();
        document.setName("Test Document");

        Mockito.when(documentService.addDocument(Mockito.any(Document.class))).thenReturn(document);

        mockMvc.perform(post("/api/documents")
                .header("Authorization", "Bearer testtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Document\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test for editing a document - Expected status: 200 OK")
    public void testEditDocument() throws Exception {
        Document document = new Document();
        document.setName("Test Document");

        Mockito.when(documentService.editDocument(Mockito.any(Document.class))).thenReturn(document);

        mockMvc.perform(put("/api/documents")
                .header("Authorization", "Bearer testtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Document\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test for deleting a document - Expected status: 204 No Content")
    public void testDeleteDocument() throws Exception {
        Mockito.when(documentService.removeDocument(1, null)).thenReturn(true);

        mockMvc.perform(delete("/api/documents/1")
                .header("Authorization", "Bearer testtoken"))
                .andExpect(status().isNoContent());
    }
}