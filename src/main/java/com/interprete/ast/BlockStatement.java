package com.interprete.ast;

import com.interprete.token.Token;
import lombok.Getter;

import java.util.List;

/**
 * Representa un bloque de código (entre llaves).
 * Ejemplo: { let x = 5; let y = 10; }
 */
@Getter
public class BlockStatement extends Statement {
    private List<Statement> statements;
    
    public BlockStatement(Token token, List<Statement> statements) {
        super(token);
        this.statements = statements;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (statements != null) {
            for (Statement statement : statements) {
                sb.append(statement.toString());
            }
        }
        return sb.toString();
    }
}
