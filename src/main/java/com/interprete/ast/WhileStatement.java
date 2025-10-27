package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

/**
 * Representa un bucle while.
 * Ejemplo: while (x < 10) { x = x + 1; }
 */
@Getter
public class WhileStatement extends Statement {
    private Expression condition;
    private BlockStatement body;
    
    public WhileStatement(Token token, Expression condition, BlockStatement body) {
        super(token);
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenLiteral());
        sb.append(" ");
        sb.append(condition != null ? condition.toString() : "");
        sb.append(" ");
        sb.append(body != null ? body.toString() : "");
        return sb.toString();
    }
}
