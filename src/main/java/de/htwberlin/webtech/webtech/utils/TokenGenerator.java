package de.htwberlin.webtech.webtech.utils;

import java.util.Date;

public class TokenGenerator {

    /**
     * Generates a Refresh Token
     *
     * @param expirationDate
     * @param subject
     * @return refreshToken
     */
    public static String generateRefreshToken(Date expirationDate, String subject) {
        return "";
    }

    /**
     * Generates an Access Token
     *
     * @param expirationDate
     * @param subject
     * @return accessToken
     */
    public static String generateAccessToken(Date expirationDate, String subject) {
        return "";
    }

    /**
     * Returns the Expiration Date of the Token
     *
     * @param token
     * @return expirationDate
     */
    public static Date getExpirationDate(String token) {
        return new Date();
    }

    /**
     * Returns the Subject of the Token
     *
     * @param token
     * @return subject
     */
    public static String getSubject(String token) {
        return "";
    }

}
