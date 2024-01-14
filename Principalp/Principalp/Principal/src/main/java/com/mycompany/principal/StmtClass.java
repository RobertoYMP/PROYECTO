package com.mycompany.principal;

import java.util.List;

public class StmtClass extends Statement {
    final Token name;
    final ExprVariable superclass;
    final List<StmtFunction> methods;

    StmtClass(Token name, ExprVariable superclass, List<StmtFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }
    Object ejecutar(Tabla tabla){
        try{
            Object Valuesup = null;
            if(superclass != null){
                Valuesup = superclass.ejecutar(tabla);
            }
            Tabla classEnv = new Tabla(tabla);
            classEnv.asignar(name.lexema, Valuesup);
            for(StmtFunction method : methods){
                method.ejecutar(classEnv);  
            }
            return null;
        }catch (Exception e){
            //throw new RuntimeException("No se ejecuto la clase");
           System.err.println("No se ejecuto la clase " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
