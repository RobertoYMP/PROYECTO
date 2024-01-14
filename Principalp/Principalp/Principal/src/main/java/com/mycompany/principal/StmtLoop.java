package com.mycompany.principal;


public class StmtLoop extends Statement {
    final Statement initialization;
    final Expression condition;
    final Statement body;
    final Expression update;
    
    Object ejecutar (TablaSimbolos tabla){
         if (initialization != null) {
         System.out.println("Inicialización del bucle for");
            initialization.ejecutar(tabla);
        }

        while (condition == null || (Boolean) condition.ejecutar(tabla)) {
            body.ejecutar(tabla);

            if (update != null) {
                update.ejecutar(tabla);
            }
	}
	return null;
    }

    StmtLoop(Statement initialization, Expression condition, Expression update, Statement body) {
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    
}
