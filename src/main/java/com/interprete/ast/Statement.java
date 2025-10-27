package com.interprete.ast;

import com.interprete.token.Token;

/**
 * Clase base abstracta para todos los statements (declaraciones).
 */
public abstract class Statement implements ASTNode {
    protected Token token;
    
    public Statement(Token token) {
        this.token = token;
    }
    
    @Override
    public String tokenLiteral() {
        return token.literal();
    }
}
