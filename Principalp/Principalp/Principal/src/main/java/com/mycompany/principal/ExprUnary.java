package com.mycompany.principal;
public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

  public Object evaluar (Tabla tabla){
        Object resultabla = right.evaluar(tabla);
        if(operator.tipo == TipoToken.BANG){
            if(resultabla instanceof Boolean){//!
                return !((Boolean)resultabla);
            }else{
                throw new RuntimeException("No se reconocio una expresion logica");
            }
        }if(operator.tipo == TipoToken.MINUS){//-
            if(resultabla instanceof Number){
            return -((Number)resultabla).floatValue();
            }
            else{
                throw new RuntimeException("No se reconocio una expresion numerica");
            }
        }
        throw new RuntimeException("No hay operadores unarios");
    }

}
