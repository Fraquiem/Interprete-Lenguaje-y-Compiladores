package com.interprete.object;

/**
 * Representa el valor null en runtime (singleton).
 */
public class MNull implements MObject {
    private static final MNull INSTANCE = new MNull();
    
    private MNull() {}
    
    public static MNull getInstance() {
        return INSTANCE;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.NULL;
    }
    
    @Override
    public String inspect() {
        return "null";
    }
}
