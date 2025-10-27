package com.interprete.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa un objeto entero en runtime.
 */
@Getter
@AllArgsConstructor
public class MInteger implements MObject {
    private Integer value;
    
    @Override
    public ObjectType type() {
        return ObjectType.INTEGER;
    }
    
    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
