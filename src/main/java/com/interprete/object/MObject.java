package com.interprete.object;

/**
 * Interfaz base para todos los objetos runtime del intérprete.
 */
public interface MObject {
    /**
     * @return el tipo del objeto
     */
    ObjectType type();
    
    /**
     * @return representación en string del objeto
     */
    String inspect();
}
