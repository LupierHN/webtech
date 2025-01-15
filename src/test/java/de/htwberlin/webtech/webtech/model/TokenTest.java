package de.htwberlin.webtech.webtech.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    public void testTokenCreation() {
        Token token = new Token("sampleToken");
        assertNotNull(token);
        assertEquals("sampleToken", token.getToken());
    }

    @Test
    public void testTokenSetterGetter() {
        Token token = new Token();
        token.setToken("newToken");
        assertEquals("newToken", token.getToken());
    }
}