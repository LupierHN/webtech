package de.htwberlin.webtech.webtech.model;


import java.util.Date;

public interface Token {
    String getToken();
    Date getExpirationDate();
    String getSubject();
}
