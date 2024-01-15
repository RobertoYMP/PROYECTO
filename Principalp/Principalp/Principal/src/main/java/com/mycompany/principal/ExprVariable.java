package com.mycompany.principal;

class ExprVariable extends Expression {
    final Token name;
    final Tabla tabla;

    ExprVariable(Token name, Tabla tabla) {
        this.name = name;
        this.tabla= tabla;
    }
    
    Object evaluar (Tabla tabla){
        if(!tabla.existeIdentificador(name.lexema)){
            throw new RuntimeException("No se declaro la variable correctamente");
        }return tabla.obtener(name.lexema);
    }
}