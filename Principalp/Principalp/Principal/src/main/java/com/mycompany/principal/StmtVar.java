package com.mycompany.principal;



public class StmtVar extends Statement {

  final Token name;
  final Expression initializer;
  
  public StmtVar(Token name, Expression initializer) {
    this.name = name;
    this.initializer = initializer;
  }
  
  public Object ejecutar(Tabla tabla) {
  this.declarar(tabla);
  return null;
}

  public void declarar(Tabla tabla) {
    String nombre = this.name.getLexema();
    Object valor = this.initializer.evaluar(); // evaluar la expresión

    if (tabla.existe(nombre)) {
      // Reportar error semántico 
    } else {
      // Declarar la variable en la tabla
      tabla.asignar(nombre, valor);
    }
  }
  
  

}

