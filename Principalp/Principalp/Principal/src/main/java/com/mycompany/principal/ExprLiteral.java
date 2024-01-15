package com.mycompany.principal;

class ExprLiteral extends Expression {
    final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }
    @Override
            Object evaluar (Tabla tabla){
                return value;
            }

}
