package com.interprete.ast;

/**
 * Interfaz base para todos los nodos del AST.
 * Cada nodo debe poder retornar su token literal y convertirse a string.
 */
public interface ASTNode {
    /**
     * @return el token literal del nodo
     */
    String tokenLiteral();
    
    /**
     * @return representación en string del nodo
     */
    String toString();
}
