package com.interprete.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa un objeto booleano en runtime.
 */
@Getter
@AllArgsConstructor
public class MBoolean implements MObject {
    private Boolean value;
    
    @Override
    public ObjectType type() {
        return ObjectType.BOOLEAN;
    }
    
    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
