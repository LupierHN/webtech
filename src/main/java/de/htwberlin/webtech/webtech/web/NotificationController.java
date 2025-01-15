package de.htwberlin.webtech.webtech.web;

import de.htwberlin.webtech.webtech.model.Notification;
import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.service.NotificationService;
import de.htwberlin.webtech.webtech.service.UserService;
import de.htwberlin.webtech.webtech.utils.TokenUtility;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * Get all notifications for a user
     *
     * @param authHeader String
     * @return List<Notification>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        final List<Notification> notifications = notificationService.getNotifications(user);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Read all notifications for a user
     *
     * @param authHeader String
     * @return ResponseEntity
     */
    @PutMapping("/read")
    public ResponseEntity<Void> readAllNotifications(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        notificationService.readNotifications(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete either a single notification or all notifications
     *
     * @param authHeader String
     * @param nId Integer
     * @return ResponseEntity
     */
    @DeleteMapping("/{nId}")
    public ResponseEntity<Void> deleteNotification(@RequestHeader("Authorization") String authHeader, @PathVariable Integer nId) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        if (nId == -1) {
            notificationService.deleteAllNotifications(user);
        } else if(nId > 0) {
            notificationService.deleteNotification(nId);
        }else{
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
