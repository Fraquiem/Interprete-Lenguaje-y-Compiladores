package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa un bucle for (simplificado estilo Go).
 * Ejemplo: for (let i = 0; i < 10; i = i + 1) { ... }
 */
public class ForStatement extends Statement {
    private Statement initialization;
    private Expression condition;
    private Statement increment;
    private BlockStatement body;
    
    public ForStatement(Token token, Statement initialization, Expression condition, Statement increment, BlockStatement body) {
        super(token);
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }
    public Statement getInitialization() {
        return initialization;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIncrement() {
        return increment;
    }

    public BlockStatement getBody() {
        return body;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenLiteral());
        sb.append(" (");
        if (initialization != null) sb.append(initialization.toString());
        sb.append("; ");
        if (condition != null) sb.append(condition.toString());
        sb.append("; ");
        if (increment != null) sb.append(increment.toString());
        sb.append(") ");
        if (body != null) sb.append(body.toString());
        return sb.toString();
    }
}
