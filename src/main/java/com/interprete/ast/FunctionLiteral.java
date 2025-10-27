package com.interprete.ast;

import com.interprete.token.Token;

import java.util.List;

/**
 * Representa un literal de función (definición de función).
 * Ejemplo: function(x, y) { x + y; }
 */
public class FunctionLiteral extends Expression {
    private List<Identifier> parameters;
    private BlockStatement body;
    
    public FunctionLiteral(Token token, List<Identifier> parameters, BlockStatement body) {
        super(token);
        this.parameters = parameters;
        this.body = body;
    }
    public List<Identifier> getParameters() {
        return parameters;
    }

    public BlockStatement getBody() {
        return body;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenLiteral());
        sb.append("(");
        
        if (parameters != null && !parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).toString());
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        
        sb.append(")");
        sb.append(body != null ? body.toString() : "");
        
        return sb.toString();
    }
}
