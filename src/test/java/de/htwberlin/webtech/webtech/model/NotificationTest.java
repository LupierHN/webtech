package de.htwberlin.webtech.webtech.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    @Test
    public void testNotificationCreation() {
        Date now = new Date();
        Notification notification = new Notification(1, "New Notification", now, false, null, null);
        assertNotNull(notification);
        assertEquals(1, notification.getNId());
        assertEquals("New Notification", notification.getMessage());
        assertEquals(now, notification.getTimestamp());
    }

    @Test
    public void testNotificationSetterGetter() {
        Notification notification = new Notification();
        notification.setMessage("Updated Notification");
        notification.setRead(true);
        assertEquals("Updated Notification", notification.getMessage());
        assertTrue(notification.isRead());
    }
}