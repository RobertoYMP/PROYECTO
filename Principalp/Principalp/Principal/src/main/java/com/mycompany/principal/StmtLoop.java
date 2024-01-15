package com.mycompany.principal;

public class StmtLoop extends Statement {
    final Expression condition;
    final Statement inicializar;
    final Expression incremento;
    final Statement body;

    StmtLoop(Expression condition, Statement body, Statement inicializar, Expression incremento) {
        this.condition = condition;
        this.body = body;
        this.inicializar = inicializar;
        this.incremento = incremento;
        
    }
    StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
        this.inicializar = null;
        this.incremento = null;
    }

    @Override 
    public Object evaluar(Tabla tabla){
        if(inicializar != null){
            inicializar.evaluar(tabla);
         }
    while (condition == null || (Boolean) condition.evaluar(tabla)) {
        body.evaluar(tabla);
        if (incremento != null) {
        incremento.evaluar(tabla);
      }
    }
    return null;
    }
   }