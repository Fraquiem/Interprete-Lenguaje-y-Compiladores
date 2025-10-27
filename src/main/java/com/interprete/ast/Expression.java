package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Clase base abstracta para todas las expresiones.
 */
public abstract class Expression implements ASTNode {
    protected Token token;
    
    public Expression(Token token) {
        this.token = token;
    }
    
    @Override
    public String tokenLiteral() {
        return token.literal();
    }
}
