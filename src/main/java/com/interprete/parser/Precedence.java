package com.interprete.parser;

import com.interprete.token.TokenType;

import java.util.HashMap;
import java.util.Map;

/**
 * Define los niveles de precedencia de los operadores.
 * Mientras más grande el número, mayor precedencia (se ejecuta primero).
 */
public enum Precedence {
    LOWEST(1),
    EQUALS(2),      // == !=
    LESSGREATER(3), // < > <= >=
    SUM(4),         // + -
    PRODUCT(5),     // * /
    PREFIX(6),      // ! -
    CALL(7);        // function calls
    
    private final int value;
    
    Precedence(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * Map que asocia cada token con su nivel de precedencia.
     */
    private static final Map<TokenType, Precedence> PRECEDENCES = new HashMap<>();
    
    static {
        PRECEDENCES.put(TokenType.EQ, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.NOE, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.LT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.GT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.LTE, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.GTE, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.PLUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.MINUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.MULTIPLICATION, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.DIVISION, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.LPAREN, Precedence.CALL);
    }
    
    /**
     * Obtiene el nivel de precedencia de un token.
     */
    public static Precedence getPrecedence(TokenType tokenType) {
        return PRECEDENCES.getOrDefault(tokenType, Precedence.LOWEST);
    }
}
