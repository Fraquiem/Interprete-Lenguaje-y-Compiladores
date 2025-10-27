package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa una expresión if-else.
 * Ejemplo: if (x > 0) { return 1; } else { return -1; }
 */
public class IfExpression extends Expression {
    private Expression condition;
    private BlockStatement consequence;
    private BlockStatement alternative;
    
    public IfExpression(Token token, Expression condition, BlockStatement consequence, BlockStatement alternative) {
        super(token);
        this.condition = condition;
        this.consequence = consequence;
        this.alternative = alternative;
    }
    public Expression getCondition() {
        return condition;
    }

    public BlockStatement getConsequence() {
        return consequence;
    }

    public BlockStatement getAlternative() {
        return alternative;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if");
        sb.append(condition != null ? condition.toString() : "");
        sb.append(" ");
        sb.append(consequence != null ? consequence.toString() : "");
        
        if (alternative != null) {
            sb.append("else ");
            sb.append(alternative.toString());
        }
        
        return sb.toString();
    }
}
