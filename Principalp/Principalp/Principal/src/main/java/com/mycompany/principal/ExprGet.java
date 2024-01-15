package com.mycompany.principal;


public class ExprGet extends Expression{
    final Expression object;
    final Token name;

    ExprGet(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }
 Object evaluar (Tabla tabla){
        return null;
    }
}
    