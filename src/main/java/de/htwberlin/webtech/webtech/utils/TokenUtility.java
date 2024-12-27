package de.htwberlin.webtech.webtech.utils;

import de.htwberlin.webtech.webtech.model.Token;
import de.htwberlin.webtech.webtech.model.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import de.htwberlin.webtech.webtech.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class TokenUtility {

    /**
     * Generates a Refresh Token
     *
     * @param user User
     * @return refreshToken Token with expiration 14 days
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
     * @param user User
     * @return accessToken Token with expiration 10 minutes
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
     * @param token Token
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
     * @param token Token
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
     * Returns the User of the Token
     *
     * @param token Token
     * @param userService UserService
     * @return user User
     */
    public static User getUser(Token token, UserService userService) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody();
            Integer uid = claims.get("uid", Integer.class);
            if (uid == null) {
                String username = claims.getSubject();
                if (username == null) {
                    return null;
                }
                return userService.getUserByUsername(username);
            }
            return userService.getUser(uid);
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Returns the User of the Token from the Authorization Header
     *
     * @param header Authorization Header
     * @param userService UserService
     * @return user User
     */
    public static User getUserFromHeader(String header, UserService userService) {
    Token token = TokenUtility.getTokenFromHeader(header);
        assert token != null;
        return getUser(token, userService);
}

    /**
     * Validates the Token
     *
     * @param token a Token
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
     * @param token refreshToken
     * @param accessToken accessToken
     * @return newToken
     */
    public static Token renewToken(Token token, Token accessToken) {
        Key key = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        try {
            Claims accessClaims;
            try {
                accessClaims = Jwts.parserBuilder()
                        .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(accessToken.getToken())
                        .getBody();
            } catch (ExpiredJwtException e) {
                accessClaims = e.getClaims();
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token.getToken())
                    .getBody();

            if (claims.getSubject().equals(accessClaims.getSubject())) {
                return new Token(Jwts.builder()
                        .setSubject(accessClaims.getSubject())
                        .claim("tokenType", "access")
                        .claim("uid", accessClaims.get("uid"))
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + 600000))
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the Token Type
     *
     * @param token Token
     * @return tokenType (access/refresh)
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

    /**
     * Returns the Token from the Header
     *
     * @param authHeader Authorization Header
     * @return token accessToken
     */
    public static Token getTokenFromHeader(String authHeader) {
        try {
            return new Token(authHeader.substring(7));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validates the Authorization Header
     *
     * @param authHeader Authorization Header
     * @return boolean
     */
    public static boolean validateAuthHeader( String authHeader) {
        Token token = getTokenFromHeader(authHeader);
        if (token == null) return false;
        return validateToken(token);
    }

    //TESTING

    /**
     * Generates a Test Token with expiration now
     * @return token with expiration now
     */
    public static Token getTestToken() {
        User user = new User();
        Date now = new Date();
        user.setUsername("HanzDieter");
        user.setUId(12);
        Key key = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
        return new Token(Jwts.builder()
                .setSubject(user.getUsername())
                .claim("tokenType", "access")
                .claim("uid", user.getUId())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact());
    }
}