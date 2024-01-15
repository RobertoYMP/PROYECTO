package com.mycompany.principal;

public class StmtExpression extends Statement {
    private final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
        
    }

    StmtExpression(java.beans.Expression expr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public Object evaluar(Tabla tabla){
        return expression.evaluar(tabla);
    }

}

    
