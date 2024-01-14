package com.mycompany.principal;



public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

     void ejecutar(Tabla tabla) {
        try {
            if (initializer != null) {
                value = initializer.ejecutar(tabla);
            }

            if (!tabla.existeIdentificador(name.lexema)) {
                tabla.declarar(name.lexema, value);
            }
        } catch (Exception e) {
            System.err.println("Error al ejecutar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
