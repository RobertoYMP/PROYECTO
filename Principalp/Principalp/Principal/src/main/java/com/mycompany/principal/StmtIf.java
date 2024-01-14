package com.mycompany.principal;

public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    public void declarar(Tabla tabla) {

    }
    private void analyzeExpression(Expression expression, Tabla tabla) {

        if (expression instanceof ExprLiteral) {

        } else if (expression instanceof ExprAssign) {
            analizaAssignExpression((ExprAssign) expression, tabla);
        } else if (expression instanceof ExprLogical) {
            analizaLogicalExpression((ExprLogical) expression, tabla);
        } else if (expression instanceof ExprBinary) {
            analizaBinaryExpression((ExprBinary) expression, tabla);
        } else if (expression instanceof ExprUnary) {
            analizaUnaryExpression((ExprUnary) expression, tabla);
        } else if (expression instanceof ExprCallFunction) {
            analizaCallFunctionExpression((ExprCallFunction) expression, tabla);
        } else if (expression instanceof ExprVariable) {
            analizaVariableExpression((ExprVariable) expression, tabla);
        }

    }
    private void analizaAssignExpression(ExprAssign assignExpression, Tabla tabla) {

        //Se obtiene el nombre de la variable a la que se está asignando
        String varName = assignExpression.getName().getLexema();
        //Se obtiene el valor asociado con ese nombre de variable
        Object value = tabla.obtener(varName);
        //Se comprueba si el nombre de la variable está en el Hashmap
        boolean definido = tabla.existeIdentificador(varName);



        //Se verifica si esta variable tiene un valor null y no está definido en el Hashmap
        if (value == null && !definido) {
            //reportarError("Error en la asignación: Variable '" + varName + "' no declarada previamente.");
        } else {

            //Se manda a analizar el valor de la asignación
            analyzeExpression(assignExpression.getValue(), tabla);
            // Actualizar el valor en el ámbito local
            tabla.asignar(varName, assignExpression.getValue());
        }
    }
    private void analizaLogicalExpression(ExprLogical logicalExpression, Tabla tabla) {
        //Se obtiene la expresión de cada parte, de la Iquierda y Derecha
        Expression leftExpr = logicalExpression.getLeft();
        Expression rightExpr = logicalExpression.getRight();


        //Se manda a llamar la función para analizar la expresión del lado Izquierdo
        analyzeExpression(leftExpr, tabla);

        //Se manda a llamar la función para analizar la expresión del lado Derecho
        analyzeExpression(rightExpr, tabla);
    }
    private void analizaBinaryExpression(ExprBinary binaryExpression, Tabla tabla) {

        //Se obtiene la expresión de cada parte, de la Iquierda y Derecha
        Expression leftExpr = binaryExpression.getLeft();
        Expression rightExpr = binaryExpression.getRight();

        //Se manda a llamar la función para analizar la expresión del lado Izquierdo
        analyzeExpression(leftExpr, tabla);

        //Se manda a llamar la función para analizar la expresión del lado Derecho
        analyzeExpression(rightExpr, tabla);

    }
    private void analizaUnaryExpression(ExprUnary unaryExpression, Tabla tabla) {

        analyzeExpression(unaryExpression.getOperand(), tabla);

    }

    StmtIf(java.beans.Expression condition, Statement thenBranch, Statement elseBranch) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
