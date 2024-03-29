package com.mycompany.principal;

import javax.swing.plaf.nimbus.State;
import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
   public Object evaluar(Tabla tabla) {
    tabla.NuevaTabla();
    Object returnValue = null;
        for (Statement stmt : statements) {
            returnValue = stmt.evaluar(tabla);
            if (returnValue instanceof StmtReturn) {
                break;
            }
        }
    return returnValue;
	}


    
}





    
