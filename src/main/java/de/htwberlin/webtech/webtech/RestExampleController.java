package de.htwberlin.webtech.webtech;

import de.htwberlin.webtech.webtech.model.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class RestExampleController {
    @GetMapping(path = "/docs")
    public ResponseEntity<ArrayList<Document>> getDoc() {
        final Document document1 = new Document(2, "Vorlesung 1", "/webtech/vorlesung1.md", "Notiz", "2024-10-20");
        final Document document2 = new Document(3, "Vorlesung 2", "/webtech/vorlesung2.md", "Notiz", "2024-10-27");
        final Document document3 = new Document(4, "Vorlesung 3", "/webtech/vorlesung3.md", "Notiz", "2024-11-03");
        final ArrayList<Document> documents = new ArrayList<>();
        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        return ResponseEntity.ok(documents);
    }
}