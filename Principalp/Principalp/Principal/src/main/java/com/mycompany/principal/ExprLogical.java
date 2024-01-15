package com.mycompany.principal;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    Object evaluar (Tabla tabla){
        Object Valueizq = left.evaluar(tabla);
        Object Valueder = right.evaluar(tabla);
        if(Valueizq instanceof Boolean && Valueder instanceof Boolean){
            if(operator.tipo == TipoToken.AND)
                return((Boolean) Valueizq && (Boolean) Valueder);
            if(operator.tipo == TipoToken.OR){
                return((Boolean) Valueizq || (Boolean) Valueder);
        }
             throw new RuntimeException("No se reconoce un operador logico");
    }else {
            throw new RuntimeException("La operacion logica no es reconocida");
        }
    }}


