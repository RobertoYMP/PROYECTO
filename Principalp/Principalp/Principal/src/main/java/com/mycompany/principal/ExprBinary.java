package com.mycompany.principal;



public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
   @Override 
    Object tablasimbolos (Tabla tabla){
        Object Valueizq = left.tablasimbolos(tabla);
        Object Valueder = left.tablasimbolos(tabla);
        if(Valueizq instanceof Number && Valueder instanceof Number){
            //PARA EL TOKEN +
            if(operator.tipo == TipoToken.PLUS){
                return ((Number) Valueizq).floatValue() + ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.MINUS){
                    return((Number) Valueizq).floatValue() - ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.STAR){
                    return((Number) Valueizq).floatValue() * ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.SLASH){
                    return ((Number) Valueizq).floatValue() / ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.GREATER){
                    return((Number) Valueizq).floatValue() > ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.LESS){
                    return((Number) Valueizq).floatValue() < ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.GREATER_EQUAL){
                    return((Number) Valueizq).floatValue() >= ((Number) Valueder).floatValue();
                }else if(operator.tipo == TipoToken.LESS_EQUAL){
                    return((Number) Valueizq).floatValue() <= ((Number) Valueder).floatValue();
                }else{
                    throw new RuntimeException("No hay algun operador numerico");
                }
        }else if(Valueizq instanceof String && Valueder instanceof String){
            if(operator.tipo == TipoToken.PLUS){
                return(String) Valueizq + (String) Valueder;
            }else{
            throw new RuntimeException("No es aplicable para cadenas");
            }
        } else {
            throw new RuntimeException("No es el tipo de dato");
            }
       }
    
}


