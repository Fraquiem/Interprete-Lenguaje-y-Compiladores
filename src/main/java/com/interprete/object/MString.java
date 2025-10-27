package com.interprete.object;

/**
 * Representa un objeto string en runtime.
 */
public class MString implements MObject {
    private String value;
    
    public MString(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.STRING;
    }
    
    @Override
    public String inspect() {
        return value;
    }
}
