package de.htwberlin.webtech.webtech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SharedDoc {
    @EmbeddedId
    private DocumentUserId id;

    @ManyToOne
    @MapsId("docId")
    @JoinColumn(name = "docId")
    private Document document;

    @ManyToOne
    @MapsId("uId")
    @JoinColumn(name = "uId")
    private User user;

    private boolean canEdit;
}



