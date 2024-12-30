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
    private final DocumentController documentController;
    private final NotificationService notificationService;


    /**
     * Get the latest notification for a user wich are not read
     *
     * @param authHeader
     * @return List<Notification>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        final List<Notification> notifications = notificationService.getLatestNotification(user);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark a notification as read
     *
     * @param authHeader Authorization Header
     * @param notification Notification
     * @return ResponseEntity
     */
    @PutMapping(value = "/read")
    public ResponseEntity<Void> markNotificationAsRead(@RequestHeader("Authorization") String authHeader, @RequestBody Notification notification) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        if (notificationService.readNotification(notification.getNId())) return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Mark all notifications as read
     *
     * @param authHeader Authorization Header
     * @return ResponseEntity
     */
    @PutMapping(value = "/readAll")
    public ResponseEntity<Void> markAllNotificationsAsRead(@RequestHeader("Authorization") String authHeader) {
        if (!TokenUtility.validateAuthHeader(authHeader)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        final User user = TokenUtility.getUserFromHeader(authHeader, userService);
        if (user == null) return ResponseEntity.notFound().build();
        notificationService.readNotifications(user);
        return ResponseEntity.ok().build();
    }





}
