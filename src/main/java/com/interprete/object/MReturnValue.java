package com.interprete.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Envuelve un valor de retorno para permitir su propagación fuera de funciones.
 */
@Getter
@AllArgsConstructor
public class MReturnValue implements MObject {
    private MObject value;
    
    @Override
    public ObjectType type() {
        return ObjectType.RETURN_VALUE;
    }
    
    @Override
    public String inspect() {
        return value != null ? value.inspect() : "null";
    }
}
