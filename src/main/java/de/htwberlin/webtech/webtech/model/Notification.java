package de.htwberlin.webtech.webtech.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("nId")
    private Integer nId;
    private String message;
    private Date timestamp;
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "uId", referencedColumnName = "uId")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "docId", referencedColumnName = "docId")
    private Document document;
}
