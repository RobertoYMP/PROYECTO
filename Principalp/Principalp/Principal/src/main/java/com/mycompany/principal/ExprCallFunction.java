package com.mycompany.principal;

import java.util.List;
import java.util.ArrayList;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
       // this.params = params;
        this.arguments = arguments;
    }
    @Override
    Object tablasimbolos (Tabla tabla){
        Token name = ((ExprVariable)callee).name;
        Object obj = tabla.obtener(name.lexema);
        if(obj instanceof StmtFunction){
            StmtFunction funcion = (StmtFunction)obj;
            List<Object> argumento = new ArrayList<>();
            for(Expression expression : arguments ){
                argumento.add(expression.tablasimbolos(tabla));
                
            }
            if(argumento.size() != funcion.params.size()){
                throw new RuntimeException("El numero de argumentos no son correctos para la funcion"+ name.lexema+" ");
                
            }
            
            Tabla entornoLocal = new Tabla();
            for (int i = 0; i < argumento.size(); i++) {
                entornoLocal.asignar(funcion.params.get(i).lexema, argumento.get(i));
            }
            funcion.body.ejecutar(entornoLocal);
        }
        throw new RuntimeException("El id no corresponde a la funcion");
    }}
    



