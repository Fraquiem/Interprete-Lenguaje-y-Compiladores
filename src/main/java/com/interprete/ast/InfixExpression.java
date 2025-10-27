package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa una expresión con operador infijo.
 * Ejemplos: 5 + 3, x == y, a < b
 */
@Getter
public class InfixExpression extends Expression {
    private Expression left;
    private String operator;
    private Expression right;
    
    public InfixExpression(Token token, Expression left, String operator, Expression right) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(left != null ? left.toString() : "");
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        sb.append(right != null ? right.toString() : "");
        sb.append(")");
        return sb.toString();
    }
}
