package de.htwberlin.webtech.webtech.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUserId implements java.io.Serializable {
    private Integer docId;
    private Integer uId;
}
