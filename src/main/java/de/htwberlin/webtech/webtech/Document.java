package de.htwberlin.webtech.webtech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Document {
    private Integer docId;
    private String name;
    private String path;
    private String docType;
    private String docDate;
}

