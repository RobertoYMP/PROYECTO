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
    
    @Override
    public Object evaluar(Tabla tabla){
    CreacionClases clase = new CreacionClases(name.lexema,methods);
        if (tabla.existeIdentificador(name.lexema)) {
            throw new RuntimeException("No se reconoce la clase, ya que existe");
        }
            tabla.asignar(name.lexema, clase);
        return clase;
    }
}