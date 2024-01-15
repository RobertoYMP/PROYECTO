package com.mycompany.principal;

class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }

    ExprVariable(Token id, Tabla tabla) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    Object evaluar (Tabla tabla){
        if(!tabla.existeIdentificador(name.lexema)){
            throw new RuntimeException("No se declaro la variable correctamente");
        }return tabla.obtener(name.lexema);
    }
}