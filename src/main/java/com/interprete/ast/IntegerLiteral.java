package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa un literal entero.
 */
@Getter
public class IntegerLiteral extends Expression {
    private Integer value;
    
    public IntegerLiteral(Token token, Integer value) {
        super(token);
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
