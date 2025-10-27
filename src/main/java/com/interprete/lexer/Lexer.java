package com.interprete.lexer;

import com.interprete.token.Token;
import com.interprete.token.TokenType;

/**
 * El lexer identifica los tokens del código fuente.
 * Convierte el input string en una secuencia de tokens.
 */
public class Lexer {
    private final String source;
    private int currentPos;
    private String currentChar;
    private int readCurrentPos;
    
    public Lexer(String source) {
        this.source = source;
        this.currentPos = 0;
        this.readCurrentPos = 0;
        this.currentChar = "";
        readCharacter();
    }
    
    /**
     * Lee el siguiente carácter y actualiza las posiciones.
     */
    private void readCharacter() {
        if (readCurrentPos >= source.length()) {
            currentChar = "";
        } else {
            currentChar = String.valueOf(source.charAt(readCurrentPos));
        }
        currentPos = readCurrentPos;
        readCurrentPos++;
    }
    
    /**
     * Omite espacios en blanco.
     */
    private void skipWhitespace() {
        while (currentChar.length() > 0 && Character.isWhitespace(currentChar.charAt(0))) {
            readCharacter();
        }
    }
    
    /**
     * Lee el siguiente carácter sin avanzar el cursor.
     * @return el siguiente carácter o "" si es EOF
     */
    private String peekCharacter() {
        if (readCurrentPos >= source.length()) {
            return "";
        } else {
            return String.valueOf(source.charAt(readCurrentPos));
        }
    }
    
    /**
     * Verifica si el carácter es una letra.
     * Soporta caracteres en español (ñ, á, é, í, ó, ú).
     */
    private boolean isLetter(String character) {
        if (character.isEmpty()) return false;
        char c = character.charAt(0);
        return Character.isLetter(c) || c == '_';
    }
    
    /**
     * Verifica si el carácter es un dígito.
     */
    private boolean isNumber(String character) {
        if (character.isEmpty()) return false;
        return Character.isDigit(character.charAt(0));
    }
    
    /**
     * Lee un identificador completo (letras y dígitos).
     */
    private String readIdentifier() {
        int initialPosition = currentPos;
        boolean isFirstLetter = true;
        
        while ((isLetter(currentChar)) || (!isFirstLetter && isNumber(currentChar))) {
            readCharacter();
            isFirstLetter = false;
        }
        
        return source.substring(initialPosition, currentPos);
    }
    
    /**
     * Lee un número entero.
     */
    private String readNumber() {
        int initialPosition = currentPos;
        
        while (isNumber(currentChar)) {
            readCharacter();
        }
        
        return source.substring(initialPosition, currentPos);
    }
    
    /**
     * Devuelve el siguiente token.
     */
    public Token nextToken() {
        skipWhitespace();
        
        if (currentChar.isEmpty()) {
            return new Token(TokenType.EOF, "");
        }
        
        Token token = null;
        
        // Operadores de dos caracteres
        if (currentChar.equals("=")) {
            if (peekCharacter().equals("=")) {
                readCharacter();
                token = new Token(TokenType.EQ, "==");
            } else {
                token = new Token(TokenType.ASSIGN, currentChar);
            }
        } else if (currentChar.equals("+")) {
            token = new Token(TokenType.PLUS, currentChar);
        } else if (currentChar.equals(",")) {
            token = new Token(TokenType.COMMA, currentChar);
        } else if (currentChar.equals(";")) {
            token = new Token(TokenType.SEMICOLON, currentChar);
        } else if (currentChar.equals(">")) {
            if (peekCharacter().equals("=")) {
                readCharacter();
                token = new Token(TokenType.GTE, ">=");
            } else {
                token = new Token(TokenType.GT, currentChar);
            }
        } else if (currentChar.equals("<")) {
            if (peekCharacter().equals("=")) {
                readCharacter();
                token = new Token(TokenType.LTE, "<=");
            } else {
                token = new Token(TokenType.LT, currentChar);
            }
        } else if (currentChar.equals("!")) {
            if (peekCharacter().equals("=")) {
                readCharacter();
                token = new Token(TokenType.NOE, "!=");
            } else {
                token = new Token(TokenType.NOT, currentChar);
            }
        } else if (currentChar.equals("(")) {
            token = new Token(TokenType.LPAREN, currentChar);
        } else if (currentChar.equals(")")) {
            token = new Token(TokenType.RPAREN, currentChar);
        } else if (currentChar.equals("{")) {
            token = new Token(TokenType.LBRACE, currentChar);
        } else if (currentChar.equals("}")) {
            token = new Token(TokenType.RBRACE, currentChar);
        } else if (currentChar.equals("-")) {
            token = new Token(TokenType.MINUS, currentChar);
        } else if (currentChar.equals("*")) {
            token = new Token(TokenType.MULTIPLICATION, currentChar);
        } else if (currentChar.equals("/")) {
            token = new Token(TokenType.DIVISION, currentChar);
        } else if (isLetter(currentChar)) {
            String literal = readIdentifier();
            TokenType tokenType = TokenType.lookupTokenType(literal);
            return new Token(tokenType, literal);
        } else if (isNumber(currentChar)) {
            String literal = readNumber();
            return new Token(TokenType.INT, literal);
        } else {
            token = new Token(TokenType.ILLEGAL, currentChar);
        }
        
        readCharacter();
        return token;
    }
}
