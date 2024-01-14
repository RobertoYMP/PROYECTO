package com.mycompany.principal;

import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    Object ejecutar(TablaSimbolos tabla){
        tabla.asignar(name.lexema,this);
	return null;
    }
}
