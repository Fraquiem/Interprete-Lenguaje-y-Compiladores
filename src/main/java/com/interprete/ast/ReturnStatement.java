package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Representa una declaración return.
 * Ejemplo: return 5;
 */
public class ReturnStatement extends Statement {
    private Expression returnValue;
    
    public ReturnStatement(Token token, Expression returnValue) {
        super(token);
        this.returnValue = returnValue;
    }
    public Expression getReturnValue() {
        return returnValue;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenLiteral());
        if (returnValue != null) {
            sb.append(" ");
            sb.append(returnValue.toString());
        }
        sb.append(";");
        return sb.toString();
    }
}
