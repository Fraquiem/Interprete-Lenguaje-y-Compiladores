package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa una expresión usada como statement.
 * Ejemplo: x + 5;
 */
public class ExpressionStatement extends Statement {
    private Expression expression;
    
    public ExpressionStatement(Token token, Expression expression) {
        super(token);
        this.expression = expression;
    }
    public Expression getExpression() {
        return expression;
    }

    
    @Override
    public String toString() {
        return expression != null ? expression.toString() : "";
    }
}
