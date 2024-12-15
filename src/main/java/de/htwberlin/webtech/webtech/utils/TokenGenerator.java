package de.htwberlin.webtech.webtech.utils;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class TokenGenerator {
    private static final String secret = System.getenv("JWT_SECRET");

    /**
     * Generates a Refresh Token
     *
     * @param user
     * @return refreshToken
     */
    public static Token generateRefreshToken(User user) {
        Date now = new Date();
        return new Token(Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1209600000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact());
    }

    /**
     * Generates an Access Token
     *
     * @param user
     * @return
     */
    public static Token generateAccessToken(User user) {
        Date now = new Date();
        return new Token(Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 600000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact());
    }

    /**
     * Returns the Expiration Date of the Token
     *
     * @param token
     * @return expirationDate
     */
    public static Date getExpirationDate(Token token) {
        try{
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token.getToken()).getBody().getExpiration();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Returns the Subject of the Token
     *
     * @param token
     * @return subject
     */
    public static String getSubject(Token token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token.getToken()).getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Validates the Token
     *
     * @param token
     * @return boolean
     */
    public static boolean validateToken(Token token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token.getToken()).getBody();
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Renews the Token
     *
     * @param token
     * @return newToken
     */
    public static Token renewToken(Token token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token.getToken()).getBody();
            return new Token(Jwts.builder()
                    .setSubject(claims.getSubject())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 600000))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact());
        } catch (JwtException e) {
            return null;
        }
    }

}
