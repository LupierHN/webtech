package de.htwberlin.webtech.webtech.persistence;

import de.htwberlin.webtech.webtech.model.Notification;
import de.htwberlin.webtech.webtech.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    List<Notification> findAllByRecipientAndReadIsFalseOrderByTimestampDesc(User recipient);

    Iterable<Notification> findAllByRecipient(User user);
}
