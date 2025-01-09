package de.htwberlin.webtech.webtech.service;

import de.htwberlin.webtech.webtech.model.Document;
import de.htwberlin.webtech.webtech.model.Notification;
import de.htwberlin.webtech.webtech.model.User;
import de.htwberlin.webtech.webtech.persistence.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Create a new notification
     *
     * @param message String
     * @param recipient User
     * @return Notification
     */
    public Notification createNotification(String message, User recipient, Document document) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipient(recipient);
        notification.setTimestamp(new Date());
        notification.setRead(false);
        notification.setDocument(document);
        return  this.notificationRepository.save(notification);
    }

    /**
     * Get the latest notification
     *
     * @param user User
     * @return List<Notification>
     */
    public List<Notification> getLatestNotification(User user) {
        List<Notification> notificationList = this.notificationRepository.findAllByRecipientAndReadIsFalseOrderByTimestampDesc(user);
        notificationList.forEach(notification -> {
            int docId = notification.getDocument().getDocId();
            notification.setRecipient(null);
            notification.setDocument(null);
            notification.setDocument(new Document());
            notification.getDocument().setDocId(docId);
        });
        return notificationList;
    }

    /**
     * Get all notifications for a user
     *
     * @param user User
     * @return Iterable<Notification>
     */
    public List<Notification> getNotifications(User user) {
        return this.notificationRepository.findAllByRecipientOrderByTimestampDesc(user);
    }

    /**
     * Read notifications
     *
     * @param user User
     */
    public void readNotifications(User user) {
        List<Notification> notifications = this.notificationRepository.findAllByRecipientAndReadIsFalseOrderByTimestampDesc(user);
        notifications.forEach(notification -> {
            notification.setRead(true);
            this.notificationRepository.save(notification);
        });
    }

    /**
     * Read a notification
     *
     * @param nId integer NotificationId
     */
    public void readNotification(int nId) {
        Optional<Notification> notification = this.notificationRepository.findById(nId);
        notification.ifPresent(value -> {
            value.setRead(true);
            this.notificationRepository.save(value);
        });
    }

    /**
     * Delete a notification
     *
     * @param nId integer NotificationId
     */
    public void deleteNotification(int nId) {
        this.notificationRepository.deleteById(nId);
    }

    /**
     * Check for new notifications
     *
     * @param user User
     * @return boolean
     */
    public boolean hasNewNotifications(User user) {
        return this.notificationRepository.findAllByRecipientAndReadIsFalseOrderByTimestampDesc(user).iterator().hasNext();
    }

    /**
     * Delete all notifications
     *
     * @param user User
     */
    public void deleteAllNotifications(User user) {
        this.notificationRepository.deleteAll(this.notificationRepository.findAllByRecipient(user));
    }
}
