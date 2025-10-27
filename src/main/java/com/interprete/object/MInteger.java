package com.interprete.object;

/**
 * Representa un objeto entero en runtime.
 */
public class MInteger implements MObject {
    private Integer value;
    
    public MInteger(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.INTEGER;
    }
    
    @Override
    public String inspect() {
        return String.valueOf(value);
    }
}
