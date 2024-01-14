/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.principal;


import java.util.HashMap;
import java.util.Map;

public class Tabla  {

    private final Map<String, Object> values = new HashMap<>();

    boolean existeIdentificador(token name){
        return values.containsKey(name);
    }

    Object obtener(token identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new RuntimeException("Variable no definida '" + name + "'.");
    }

    void asignar(token name, Object valor){
        values.put(name, valor);
    }


}