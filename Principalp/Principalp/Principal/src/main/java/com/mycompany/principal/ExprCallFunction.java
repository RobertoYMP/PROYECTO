package com.mycompany.principal;
import java.util.ArrayList;
import java.util.List;

public class ExprCallFunction extends Expression {
    final Expression callee;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, List<Expression> arguments) {
        this.callee = callee;
        this.arguments = arguments;//Se inicia una lista de argumentos nueva si no hay nada o es null
    }

/*
    @Override
    public String toString() {
        String calleeStr = (callee != null) ? callee.toString() : "null";

        StringBuilder argsStr = new StringBuilder();
        for (Expression arg : arguments) {
            argsStr.append((arg != null) ? arg.toString() : "null").append(", ");
        }
        if (argsStr.length() > 0) {
            argsStr.setLength(argsStr.length() - 2); // Elimina la última coma y espacio
        }

        return calleeStr + "(" + argsStr.toString() + ")";
    }*/

    ExprCallFunction(Expression expr, List<Expression> lstArguments, Tabla tabla) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


@Override
public Object evaluar(Tabla tabla) {
        Token name = ((ExprVariable)callee).name;
        Object obj = tabla.obtener(name.lexema);
        if(obj instanceof StmtFunction){
            StmtFunction funcion = (StmtFunction)obj;
            List<Object> argumento = new ArrayList<>();
            for(Expression expression : arguments ){
                argumento.add(expression.evaluar(tabla));
                
            }
            if(argumento.size() != funcion.params.size()){
                throw new RuntimeException("El numero de argumentos no son correctos para la funcion"+ name.lexema+" ");
             
            }
            
            Tabla entornoLocal = new Tabla();
            for (int i = 0; i < argumento.size(); i++) {
                entornoLocal.asignar(funcion.params.get(i).lexema, argumento.get(i));
            }
            funcion.body.evaluar(entornoLocal);
        }
        throw new RuntimeException("El id no corresponde a la funcion");
    }}


