package com.interprete.token;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum que define todos los tipos de tokens que puede reconocer el lexer.
 */
public enum TokenType {
    // Operadores
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    
    // Comparadores
    EQ("=="),
    NOE("!="),
    LT("<"),
    LTE("<="),
    GT(">"),
    GTE(">="),
    
    // Operadores lógicos
    NOT("!"),
    
    // Keywords
    LET("let"),
    FUNCTION("function"),
    IF("if"),
    ELSE("else"),
    RETURN("return"),
    TRUE("true"),
    FALSE("false"),
    FOR("for"),
    WHILE("while"),
    
    // Delimitadores
    COMMA(","),
    SEMICOLON(";"),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    
    // Identificadores y literales
    IDENT(""),  // identificador de variable
    INT(""),    // número entero
    STRING(""), // string
    
    // Especiales
    EOF(""),    // fin de archivo
    ILLEGAL(""); // token ilegal
    
    private final String literal;
    
    TokenType(String literal) {
        this.literal = literal;
    }
    
    public String getLiteral() {
        return literal;
    }
    
    /**
     * Lookup table para mapear keywords a sus TokenType correspondientes.
     */
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    
    static {
        KEYWORDS.put("let", LET);
        KEYWORDS.put("function", FUNCTION);
        KEYWORDS.put("if", IF);
        KEYWORDS.put("else", ELSE);
        KEYWORDS.put("return", RETURN);
        KEYWORDS.put("true", TRUE);
        KEYWORDS.put("false", FALSE);
        KEYWORDS.put("for", FOR);
        KEYWORDS.put("while", WHILE);
    }
    
    /**
     * Identifica si un literal es una keyword o un identificador.
     * @param literal el literal a verificar
     * @return el TokenType correspondiente (IDENT si no es keyword)
     */
    public static TokenType lookupTokenType(String literal) {
        return KEYWORDS.getOrDefault(literal, IDENT);
    }
}
