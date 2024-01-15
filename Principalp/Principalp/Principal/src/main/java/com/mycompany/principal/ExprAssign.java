package com.mycompany.principal;

public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
    
    public Object evaluar(Tabla tabla) {
    Object EvaValue = value.evaluar(tabla);
    if (tabla.existeIdentificador(name.lexema)) {
        tabla.asignar(name.lexema, EvaValue);
    } else {
        throw new RuntimeException("No se reconocio la variable"  );
    }

    return EvaValue;
}
    
}

