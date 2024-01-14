package com.mycompany.principal;

class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }
    
    Object tablasimbolos (Tabla tabla){
        if(!tabla.existeIdentificador(name.lexema)){
            throw new RuntimeException("No se declaro la variable correctamente");
        }return tabla.obtener(name.lexema);
    }
}