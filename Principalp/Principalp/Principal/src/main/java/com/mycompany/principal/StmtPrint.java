package com.mycompany.principal;


public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }
  @Override  
   public Object evaluar(Tabla tabla) {
            //System.out.println(expression.evaluar(tabla));
	    return null;
    }
}
