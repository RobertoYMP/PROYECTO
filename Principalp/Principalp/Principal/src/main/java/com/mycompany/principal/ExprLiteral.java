package com.mycompany.principal;

class ExprLiteral extends Expression {
    final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }
    @Override
            Object tablasimbolos (Tabla tabla){
                return value;
            }

}
