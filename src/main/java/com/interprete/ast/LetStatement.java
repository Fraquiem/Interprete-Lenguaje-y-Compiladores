package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa una declaración let (variable).
 * Ejemplo: let x = 5;
 */
public class LetStatement extends Statement {
    private Identifier name;
    private Expression value;
    
    public LetStatement(Token token, Identifier name, Expression value) {
        super(token);
        this.name = name;
        this.value = value;
    }
    public Identifier getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenLiteral());
        sb.append(" ");
        sb.append(name != null ? name.toString() : "");
        sb.append(" = ");
        sb.append(value != null ? value.toString() : "");
        sb.append(";");
        return sb.toString();
    }
}
