package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa una expresión con operador prefijo.
 * Ejemplos: !true, -5
 */
@Getter
public class PrefixExpression extends Expression {
    private String operator;
    private Expression right;
    
    public PrefixExpression(Token token, String operator, Expression right) {
        super(token);
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(operator);
        sb.append(right != null ? right.toString() : "");
        sb.append(")");
        return sb.toString();
    }
}
