package com.interprete.ast;

import com.interprete.token.Token;

import java.util.List;

/**
 * Representa una llamada a función.
 * Ejemplo: suma(1, 2)
 */
public class CallExpression extends Expression {
    private Expression function;
    private List<Expression> arguments;
    
    public CallExpression(Token token, Expression function, List<Expression> arguments) {
        super(token);
        this.function = function;
        this.arguments = arguments;
    }
    public Expression getFunction() {
        return function;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(function != null ? function.toString() : "");
        sb.append("(");
        
        if (arguments != null && !arguments.isEmpty()) {
            for (int i = 0; i < arguments.size(); i++) {
                sb.append(arguments.get(i).toString());
                if (i < arguments.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        
        sb.append(")");
        return sb.toString();
    }
}
