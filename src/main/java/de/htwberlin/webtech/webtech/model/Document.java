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
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;
    private String name;
    private String path;
    private String docType;
    private String content;
    private String docDate;

    @ManyToOne
    @JoinColumn(name = "uId", referencedColumnName = "uId")
    private User owner;
}

