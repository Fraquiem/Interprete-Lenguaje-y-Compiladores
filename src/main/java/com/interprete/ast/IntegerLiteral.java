package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa un literal entero.
 */
public class IntegerLiteral extends Expression {
    private Integer value;
    
    public IntegerLiteral(Token token, Integer value) {
        super(token);
        this.value = value;
    }
    public Integer getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
