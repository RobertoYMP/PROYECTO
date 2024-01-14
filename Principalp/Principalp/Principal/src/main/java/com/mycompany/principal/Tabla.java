package com.mycompany.principal;

import java.util.HashMap;
import java.util.Map;

public class Tabla extends TablaSimbolos{

    private final Map<String, Object> values = new HashMap<>();

    boolean existeIdentificador(String identificador){
        return values.containsKey(identificador);
    }
    
    public Tabla() {
    super(null);
	}

    Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new RuntimeException("Variable no definida '" + identificador + "'.");
    }

    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }


}