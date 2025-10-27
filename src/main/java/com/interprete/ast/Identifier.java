package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa un identificador (nombre de variable).
 */
public class Identifier extends Expression {
    private String value;
    
    public Identifier(Token token, String value) {
        super(token);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
