package de.htwberlin.webtech.webtech.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryElement {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @JsonProperty("histId")
    private Integer histId;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "docId", referencedColumnName = "docId")
    private Document document;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "uId", referencedColumnName = "uId")
    private User user;
}
