package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa un literal booleano (true/false).
 */
@Getter
public class BooleanLiteral extends Expression {
    private Boolean value;
    
    public BooleanLiteral(Token token, Boolean value) {
        super(token);
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
