package com.interprete.evaluator;

import com.interprete.object.MObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabla de símbolos que maneja el scope de variables.
 * Soporta scopes anidados mediante encadenamiento.
 */
public class Environment {
    private final Map<String, MObject> store;
    private Environment outer;
    
    public Environment() {
        this.store = new HashMap<>();
        this.outer = null;
    }
    
    /**
     * Crea un nuevo environment encadenado (para scopes anidados).
     */
    public Environment(Environment outer) {
        this();
        this.outer = outer;
    }
    
    /**
     * Obtiene el valor de una variable por su nombre.
     * Si no existe en el scope actual, busca en el scope externo.
     */
    public MObject get(String name) {
        MObject obj = store.get(name);
        if (obj == null && outer != null) {
            obj = outer.get(name);
        }
        return obj;
    }
    
    /**
     * Asigna un valor a una variable.
     */
    public void set(String name, MObject value) {
        store.put(name, value);
    }
    
    /**
     * Verifica si una variable existe en el scope actual.
     */
    public boolean contains(String name) {
        return store.containsKey(name);
    }
}
