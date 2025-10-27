package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa un identificador (nombre de variable).
 */
@Getter
public class Identifier extends Expression {
    private String value;
    
    public Identifier(Token token, String value) {
        super(token);
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
