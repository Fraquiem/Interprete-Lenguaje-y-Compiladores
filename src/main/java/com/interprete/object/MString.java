package com.interprete.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa un objeto string en runtime.
 */
@Getter
@AllArgsConstructor
public class MString implements MObject {
    private String value;
    
    @Override
    public ObjectType type() {
        return ObjectType.STRING;
    }
    
    @Override
    public String inspect() {
        return value;
    }
}
