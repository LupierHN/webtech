package de.htwberlin.webtech.webtech.model;

import de.htwberlin.webtech.webtech.utils.TokenGenerator;

import java.util.Date;

public class RefreshToken implements Token {
    private String token;
    private Date expirationDate;
    private String subject;


    public RefreshToken(Date expirationDate, String subject) {
        this.expirationDate = expirationDate;
        this.subject = subject;
        this.token = TokenGenerator.generateRefreshToken(expirationDate, subject);
    }

    public RefreshToken(String token) {
        this.token = token;
        this.expirationDate = TokenGenerator.getExpirationDate(token);
        this.subject = TokenGenerator.getSubject(token);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}


