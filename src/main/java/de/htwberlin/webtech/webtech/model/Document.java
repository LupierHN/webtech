package de.htwberlin.webtech.webtech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("docId")
    private Integer docId;
    private String name;
    private String path;
    private String docType;
    @JsonIgnore
    @Column(length=10485760)
    private String content;
    private String docDate;

    @ManyToOne
    @JoinColumn(name = "uId", referencedColumnName = "uId")
    private User owner;

    @JsonIgnore
    @ManyToMany(mappedBy = "sharedDocuments")
    private Set<User> sharedWith; // all users this document is shared with
}