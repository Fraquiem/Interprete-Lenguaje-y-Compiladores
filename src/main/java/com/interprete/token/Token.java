package com.interprete.token;

/**
 * Representa un token del lenguaje.
 * Usa un record de Java 21 para inmutabilidad.
 */
public record Token(TokenType tokenType, String literal) {
    
    @Override
    public String toString() {
        return tokenType.name() + ":" + literal;
    }
}
