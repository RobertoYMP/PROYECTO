package com.mycompany.principal;



public class StmtVar extends Statement {

  final Token name;
  final Expression initializer;
  
  public StmtVar(Token name, Expression initializer) {
    this.name = name;
    this.initializer = initializer;
  }
  @Override
  public Object evaluar(Tabla tabla) {
    Object value = null;
    if(initializer != null){
        value = initializer.evaluar(tabla);
    }
    if(!tabla.existeIdentificador(name.lexema)){
        tabla.declarar(name.lexema, value);
    }return null;
  }
  }
  


