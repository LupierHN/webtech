package de.htwberlin.webtech.webtech;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RestExampleController {
    @GetMapping(path = "/docs")
    public ResponseEntity<Document> getDoc() {
        final Document document = new Document(2,"Vorlesung 1", "/webtech/vorlesung1.md", "Notiz", "2024-10-20");
        return ResponseEntity.ok(document);

    }
}

