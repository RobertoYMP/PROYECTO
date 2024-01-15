package com.mycompany.principal;

public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }

    @Override
    public Object evaluar(Tabla tabla) {
            return value.evaluar(tabla);
    }
}
