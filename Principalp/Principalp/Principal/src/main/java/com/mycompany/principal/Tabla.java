package com.mycompany.principal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabla {
    private  Map<String, Object> values = new HashMap<>();
 /*   private Map<String, Object> registroClases = new HashMap<>();
    private Map<String, Object> registroMetodos = new HashMap<>();*/

   /* public Tabla() {
           values = (Map<String, Object>) List.of(new HashMap<>());// El alcance global es simplemente el alcance actual en este enfoque
    }*/

    boolean existeIdentificador(String identificador) {
        return values.containsKey(identificador);
    }
/*
    void registrarClase(String nombreClase, Object definicionClase) {
        registroClases.put(nombreClase, definicionClase);
    }
*/
    Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new RuntimeException("Identificador no definido '" + identificador + "'.");
    }

    void NuevaTabla() {
        // Crear un nuevo alcance actual vacío
        values = new HashMap<>();
    }
    void declarar(String identificador, Object valorInicial) {
        values.put(identificador, valorInicial);
    }

    void asignar(String identificador, Object valor) {
      values.put(identificador, valor);
    }
    
/*
    void registrarFuncion(String nombreFuncion, Object definicionFuncion) {
        registroMetodos.put(nombreFuncion, definicionFuncion);
    }

    Object obtenerClase(String nombreClase) {
        return registroClases.get(nombreClase);
    }

    boolean existeClase(String nombreClase) {
        return registroClases.containsKey(nombreClase);
    }

    public Object obtenerFuncion(String nombreFuncion) {
        if (registroMetodos.containsKey(nombreFuncion)) {
            return registroMetodos.get(nombreFuncion);
        }
        throw new RuntimeException("Función no definida '" + nombreFuncion + "'.");
    }*/
}




























/*
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
*/