package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.HistoryElement;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.DocumentService;
import de.htwberlin.webtech.webtech.service.HistoryElementService;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryElementService historyElementService;
    private final UserService userService;
    private final DocumentService documentService;

    /**
     * Get all history elements of a user
     *
     * @param authHeader String
     * @return List<HistoryElement>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HistoryElement>> getHistoryElements(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        List<HistoryElement> historyElements = historyElementService.getHistoryElements(user);
        historyElements.forEach(historyElement -> documentService.censorDocumentOwner(historyElement.getDocument()));
        return ResponseEntity.ok(historyElements);
    }

    /**
     * Add a new history element
     *
     * @param authHeader String
     * @param documentId int
     */
    @PostMapping("/{documentId}")
    public ResponseEntity<HistoryElement> addHistoryElement(@RequestHeader("Authorization") String authHeader, @PathVariable Integer documentId) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        Document doc = documentService.getDocument(documentId, user).orElse(null);
        if (doc == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(historyElementService.addHistoryElement(user, doc));
    }

    /**
     * Delete either all history elements of a user or a specific history element
     *
     * @param authHeader String
     * @param histId int
     */
    @DeleteMapping("/{histId}")
    public ResponseEntity<Void> deleteHistoryElement(@RequestHeader("Authorization") String authHeader, @PathVariable Integer histId) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        if (histId == -1) {
            historyElementService.deleteHistoryElements(user);
        } else if(histId > 0) {
            try {
                historyElementService.deleteHistoryElement(histId, user);
            }catch (Exception e){
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


}
