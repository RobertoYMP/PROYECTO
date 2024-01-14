package com.mycompany.principal;

public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
        
    }


    Object ejecutar(Tabla tabla){
        analyzeExpression(expressionStatement.getExpression(), localScope);
        return null;
         //return expression.ejecutar(tabla);
    }


}
