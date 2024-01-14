package com.mycompany.principal;


public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
    @Override
            Object tablasimbolos(Tabla tabla){
            if (!tabla.existeIdentificador(name.lexema)){
            throw new RuntimeException("No se reconocio la variable");
             }
            tabla.asignar(name.lexema, value);
            return tabla.obtener(name.lexema);
    }

    ExprAssign(Token name, java.beans.Expression value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
