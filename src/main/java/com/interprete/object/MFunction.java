package com.interprete.object;

import com.interprete.ast.BlockStatement;
import com.interprete.ast.Identifier;
import com.interprete.evaluator.Environment;

import java.util.List;

/**
 * Representa una función en runtime.
 * Almacena los parámetros, el cuerpo de la función y el entorno donde se define.
 */
public class MFunction implements MObject {
    private List<Identifier> parameters;
    private BlockStatement body;
    private Environment env;
    
    public MFunction(List<Identifier> parameters, BlockStatement body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }
    
    public List<Identifier> getParameters() {
        return parameters;
    }
    
    public BlockStatement getBody() {
        return body;
    }
    
    public Environment getEnv() {
        return env;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.FUNCTION;
    }
    
    @Override
    public String inspect() {
        return "function(...)";
    }
}
