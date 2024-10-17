package de.htwberlin.webtech.webtech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Hero {
    private String name;
    private String affiliation;
    private double heightInM;
}

