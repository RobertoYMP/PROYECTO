/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.principal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Daniel
 */
public class TablaSimbolos {

  private TablaSimbolos tablaPadre; 
  private Map<String, Object> valores;

  public TablaSimbolos(TablaSimbolos padre) {
    this.tablaPadre = padre;
    this.valores = new HashMap<>(); 
  }
  
  boolean existe(String identificador) {
    if (this.valores.containsKey(identificador)) {
      return true;
    }
    
    if (this.tablaPadre != null) {
      return this.tablaPadre.existe(identificador);  
    }
    
    return false;
  }

  Object obtener(String identificador) {
    if (this.valores.containsKey(identificador)) {
      return this.valores.get(identificador);
    } 
    
    if (this.tablaPadre != null) {
      return this.tablaPadre.obtener(identificador);
    }
    
    throw new RuntimeException("Variable no encontrada "+identificador);
  }

  void asignar(String identificador, Object valor) {
    this.valores.put(identificador, valor); 
  }

  // métodos como existe(), obtener(), asignar()
}
