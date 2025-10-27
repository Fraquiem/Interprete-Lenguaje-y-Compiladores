package com.interprete.object;

import com.interprete.ast.BlockStatement;
import com.interprete.ast.Identifier;
import com.interprete.evaluator.Environment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Representa una función en runtime.
 * Almacena los parámetros, el cuerpo de la función y el entorno donde se define.
 */
@Getter
@AllArgsConstructor
public class MFunction implements MObject {
    private List<Identifier> parameters;
    private BlockStatement body;
    private Environment env;
    
    @Override
    public ObjectType type() {
        return ObjectType.FUNCTION;
    }
    
    @Override
    public String inspect() {
        return "function(...)";
    }
}
