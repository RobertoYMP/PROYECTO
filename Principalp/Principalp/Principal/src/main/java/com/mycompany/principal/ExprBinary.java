package com.mycompany.principal;


    public class ExprBinary extends Expression {
        final Expression left;
        final Token operator;
        final Expression right;
    

        ExprBinary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
            
        }


     /*   @Override
        public void print(String indentation) {
            System.out.println(indentation + "ExprBinary");
            System.out.println(indentation + "\tLeft:");
            left.print(indentation + "\t\t");
            System.out.println(indentation + "\tOperator: " + operator.lexema);
            System.out.println(indentation + "\tRight:");
            right.print(indentation + "\t\t");
        }*/

        @Override
    public Object evaluar(Tabla tabla) {
        Object Valueizq = left.evaluar(tabla);
        Object Valueder = right.evaluar(tabla);
//VERIFICAR LADO IZQUIERDO CON DERECHO EN CUESTIONES NUMERICAS
          if(Valueizq instanceof Number && Valueder instanceof Number){
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
                } }
                else if(Valueizq instanceof String && Valueder instanceof String) {//Concatenacion de cadenas
                  if(operator.tipo == TipoToken.PLUS){
                      return(String) Valueizq + (String) Valueder;
                  }else{
                  throw new RuntimeException("No es aplicable para cadenas");
            }
        }
                throw new RuntimeException("No se pueden reconocer operaciones con variables distintas ");

    }
    }

