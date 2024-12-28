package de.htwberlin.webtech.webtech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "sharedDocuments",
            joinColumns = {@JoinColumn(name = "uId")},
            inverseJoinColumns =  { @JoinColumn(name = "docId") }
    )
    Set<Document> sharedDocuments;
}