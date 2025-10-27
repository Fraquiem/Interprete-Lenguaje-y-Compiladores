package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa un literal booleano (true/false).
 */
public class BooleanLiteral extends Expression {
    private Boolean value;
    
    public BooleanLiteral(Token token, Boolean value) {
        super(token);
        this.value = value;
    }
    public Boolean getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
