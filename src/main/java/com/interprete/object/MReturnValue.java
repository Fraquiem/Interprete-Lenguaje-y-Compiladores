package com.interprete.object;

/**
 * Envuelve un valor de retorno para permitir su propagación fuera de funciones.
 */
public class MReturnValue implements MObject {
    private MObject value;
    
    public MReturnValue(MObject value) {
        this.value = value;
    }
    
    public MObject getValue() {
        return value;
    }
    
    @Override
    public ObjectType type() {
        return ObjectType.RETURN_VALUE;
    }
    
    @Override
    public String inspect() {
        return value != null ? value.inspect() : "null";
    }
}
