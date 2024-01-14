package com.mycompany.principal;

public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
        
    }


    Object ejecutar(Tabla tabla){
        analyzeExpression(expressionStatement.getExpression(), localScope);
    }
    StmtExpression(java.beans.Expression expr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
