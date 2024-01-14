package com.mycompany.principal;

public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }

    Object ejecutar(Tabla tabla) {
        try {
            return value.ejecutar(tabla);
        } catch (Exception e) {
            System.err.println("Error al evaluar la expresión: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
