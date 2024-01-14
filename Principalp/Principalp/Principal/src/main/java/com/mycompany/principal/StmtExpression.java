package com.mycompany.principal;

public class StmtExpression extends Statement {
    private final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
        
    }

    public void analizarSemantica(Tabla tabla) {
        expression.analizarSemantica(tabla); 
      }

    StmtExpression(java.beans.Expression expr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

    
