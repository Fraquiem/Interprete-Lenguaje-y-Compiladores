package com.interprete.object;

/**
 * Representa un objeto booleano en runtime.
 */
public class MBoolean implements MObject {
    private Boolean value;
    
    public MBoolean(Boolean value) {
        this.value = value;
    }
    
    public Boolean getValue() {
        return value;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.BOOLEAN;
    }
    
    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
