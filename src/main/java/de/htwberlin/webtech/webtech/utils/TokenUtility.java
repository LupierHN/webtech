package de.htwberlin.webtech.webtech.utils;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenUtility {

    /**
     * Generates a Refresh Token
     *
     * @param user
     * @return refreshToken
     */
    public static Token generateRefreshToken(User user) {
        Date now = new Date();
        Key key = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
        return new Token(Jwts.builder()
                .setSubject(user.getUsername())
                .claim("tokenType", "refresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1209600000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact());
    }

    /**
     * Generates an Access Token
     *
     * @param user
     * @return accessToken
     */
    public static Token generateAccessToken(User user) {
    Date now = new Date();
    Key key = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
    return new Token(Jwts.builder()
            .setSubject(user.getUsername())
            .claim("tokenType", "access")
            .claim("uid", user.getUId())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 600000))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact());
}

    /**
     * Returns the Expiration Date of the Token
     *
     * @param token
     * @return expirationDate
     */
    public static Date getExpirationDate(Token token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody()
                    .getExpiration();
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
            return Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody()
                    .getSubject();
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
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody();
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
    public static Token renewToken(Token token, Token accessToken) {
        Key key = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody();
            Claims accessClaims = Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(accessToken.getToken())
                    .getBody();
            return new Token(Jwts.builder()
                    .setSubject(claims.getSubject())
                    .claim("tokenType", "access")
                    .claim("uid", accessClaims.get("uid"))
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + 600000))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact());
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Returns the Token Type
     *
     * @param token
     * @return tokenType
     */
    public static String getTokenType(Token token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody()
                    .get("tokenType", String.class);
        } catch (JwtException e) {
            return null;
        }
    }
}