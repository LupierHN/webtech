package de.htwberlin.webtech.webtech.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import de.htwberlin.webtech.webtech.utils.TokenGenerator;

@Getter
@Setter
public class AccessToken implements Token {
    private final String token;
    private final Date expirationDate;
    private final String subject;

    public AccessToken(Date expirationDate, String subject) {
        this.expirationDate = expirationDate;
        this.subject = subject;
        this.token = TokenGenerator.generateAccessToken(expirationDate, subject);
    }

    public AccessToken(String token) {
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
