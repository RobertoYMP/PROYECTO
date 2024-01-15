package com.mycompany.principal;

public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    public void declarar(Tabla tabla) {

    }
   @Override 
   public Object evaluar(Tabla tabla){
      if (!(condition.evaluar(tabla) instanceof Boolean)) {
            throw new RuntimeException("La condición de if no es booleana.");
        }
        if ((Boolean) condition.evaluar(tabla)) {
            return thenBranch.evaluar(tabla);
        } else if (elseBranch != null) {
            return elseBranch.evaluar(tabla);
            
        }
        return null;
    }
}
