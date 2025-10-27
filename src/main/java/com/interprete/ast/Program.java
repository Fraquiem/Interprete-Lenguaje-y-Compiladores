package com.interprete.ast;

import lombok.Getter;

import java.util.List;

/**
 * Representa un programa completo, que es una secuencia de statements.
 */
@Getter
public class Program implements ASTNode {
    private List<Statement> statements;
    
    public Program(List<Statement> statements) {
        this.statements = statements;
    }
    
    @Override
    public String tokenLiteral() {
        if (statements != null && !statements.isEmpty()) {
            return statements.get(0).tokenLiteral();
        }
        return "";
    }
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Statement statement : statements) {
            out.append(statement.toString());
        }
        return out.toString();
    }
}
