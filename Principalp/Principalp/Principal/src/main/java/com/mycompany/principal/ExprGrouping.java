package com.mycompany.principal;
import com.mycompany.principal.Token;
import java.util.List;
public class ExprGrouping extends Expression {
    final Expression expression;

    ExprGrouping(Expression expression) {
        this.expression = expression;
    }
    @Override
    Object evaluar (Tabla tabla){
        return expression.evaluar(tabla);
    }
}