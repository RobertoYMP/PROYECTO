package com.mycompany.principal;


public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }
    
   Object ejecutar(TablaSimbolos tabla) {
        try {
            System.out.println(expression.ejecutar(tabla));
        } catch (Exception e) {
            System.err.println("Error al evaluar la expresión: " + e.getMessage());
            e.printStackTrace();
        }
	    return null;
    }
}
