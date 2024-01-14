package com.mycompany.principal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Semantico {

    //Métodos para Scope
    private class Scope {

        //Se crea un Hashmap
        private Map<String, Object> symbolTable;

        //Representa los Hashmap padres del symbolTable local
        private Scope parentScope;

        //Crea el Hashmap junto con su Mapa al que se le asocia
        public Scope(Scope parentScope) {

            this.parentScope = parentScope;
            this.symbolTable = new HashMap<>();

        }

        //Función para definir y agregar en el Hashmap
        public void define(String varName, Object value, Scope localScope) {

            if(value instanceof ExprLiteral){
                symbolTable.put(varName, value);
            }else if(value instanceof ExprVariable){
                Expression var = (Expression) value;
            String variableName = getVariableName(var);
             symbolTable.put(varName, localScope.resolve(variableName));
        }else if(value instanceof ExprBinary){
            Expression expr = (Expression) value;
            value = getValue(expr, localScope);
            symbolTable.put(varName, value);
        }else{
             symbolTable.put(varName, value);
        }

        }

        //Función para retornar el valor de la variable si está en el Hashmap
        public Object resolve(String varName) {

            //Se le asigna el valor que está asociado al nombre de la Variable a buscar
            Object value = symbolTable.get(varName);

            //Se verifica si el valor no es nulo y si la tabla contiene el nombre de la Variable
            if (value != null || symbolTable.containsKey(varName)) {

                if(value instanceof ExprLiteral){
                    return ((ExprLiteral)value).getValue();
                }else if(value instanceof ExprBinary){

                    return value;
                }else{
                    //Retorna el valor asociado
                    return value;
                }
                

            } else if (parentScope != null) {

                //Si el padre no es nulo, significa que tiene un Hashmap padre, por lo que se llama recursivamente, pero con el Hashmap padre para analizar
                return parentScope.resolve(varName);

            } else {

                //Retorna null en caso de que no esté en ningún Hashmap, lo que significaría que no está declarada la variable.
                return null; 

            }

        }

        //Función para comprobar si la variable está definida en el Hashmap
         public boolean isDefined(String varName) {

            if (symbolTable.containsKey(varName)) {

                return true;

            } else if (parentScope != null) {

                return parentScope.isDefined(varName);

            } else {

                return false;

            }
        }
    }

    //Se crea una variable globalScope
    private Scope globalScope;

    //Se crea un nuevo Scope con padre null, lo que quiere decir que será el Hashmap global
    public Semantico() {

        this.globalScope = new Scope(null);

    }

    //Función para analizar el árbol que se envía del Analizador Sintáctico
    public static void analyze(Program program) {

        Semantico semanticAnalyzer = new Semantico();
        semanticAnalyzer.analyzeDeclarations(program.getDeclarations(), semanticAnalyzer.globalScope);

        if (!semanticAnalyzer.hayErrores()) {
            // System.out.println("Sin errores semanticos");
        }

    }

    private boolean erroresSemanticos = false; //Variable auxiliar para detectar errores Semánticos

    //Función para retornar la variable erroresSemanticos
    public boolean hayErrores() {
        return erroresSemanticos;
    }

    // Función para escribir el mensaje de Error
    private void reportarError(String mensaje) {
        throw new RuntimeException("Error Semántico: " + mensaje);
    }


    //Función donde se itera en la Lista de Statements del árbol del programa
    private void analyzeDeclarations(List<Statement> declarations, Scope localScope) {

        for (Statement declaration : declarations) {

            if (declaration instanceof StmtVar) {
                analyzeVarDeclaration((StmtVar) declaration, localScope);
            } else if (declaration instanceof StmtFunction) {
                analyzeFunctionDeclaration((StmtFunction) declaration, localScope);
            } else if (declaration instanceof StmtExpression) {
                analyzeExpressionStatement((StmtExpression) declaration, localScope);
            } else if (declaration instanceof StmtIf) {
                analyzeIfStatement((StmtIf) declaration, localScope);
            } else if (declaration instanceof StmtLoop) {
                analyzeLoopStatement((StmtLoop) declaration, localScope);
            } else if (declaration instanceof StmtPrint) {
                analyzePrintStatement((StmtPrint) declaration, localScope);
            } else if (declaration instanceof StmtReturn) {
                analyzeReturnStatement((StmtReturn) declaration, localScope);
            } else if (declaration instanceof StmtBlock) {
                analyzeBlockStatement((StmtBlock) declaration, localScope);
            }

        }

    }

    private void analyzeBlockStatement(StmtBlock blockStatement, Scope parentScope) {
        Scope blockScope = new Scope(parentScope);
        analyzeDeclarations(blockStatement.getStatements(), blockScope);
    }

    //Función para analizar las Declaraciones de Variables
    private void analyzeVarDeclaration(StmtVar varDeclaration, Scope localScope) {

        //Variable en la que se almacena el nombre de la variable
        String varName = varDeclaration.getName().getLexema();

        //Se verifica si el nombre de la variable ya está en nuestro Hashmap
        if (localScope.isDefined(varName)) {
            reportarError("Variable '" + varName + "' ya declarada en este ámbito.");
        } else {
             //Si no está en el Hashmap, se agrega a este su nombre y su valor
            localScope.define(varName, varDeclaration.getInitializer(), localScope);
        }

    }

    //Función para analizar las declaraciones de Funciones 
    private void analyzeFunctionDeclaration(StmtFunction functionDeclaration, Scope localScope) {
        
         //Variable auxiliar en la que se almacena el nombre de la función
        String funcName = functionDeclaration.getName().getLexema();

        //Se verifica si el nombre de la función se encuentra en el Hashmap
        if (localScope.resolve(funcName) != null) {
            reportarError("Función '" + funcName + "' ya declarada en este ámbito.");
        } else {
            //Si no está en el Hashmap, se agrega el nombre de la función junto con todos sus datos
            localScope.define(funcName, functionDeclaration, localScope);
        }

    }

     //Función para analizar los Statement de Expresiones
    private void analyzeExpressionStatement(StmtExpression expressionStatement, Scope localScope) {

        //Se manda a llamar la función para analizar la expresión
        analyzeExpression(expressionStatement.getExpression(), localScope);

    }

     //Función para analizar los If
    private void analyzeIfStatement(StmtIf ifStatement, Scope localScope) {

        //Se manda a llamar la función para analizar la expresión para la condición
        analyzeExpression(ifStatement.getCondition(), localScope);

        if(analyzeConditions(ifStatement.getCondition(), localScope)){
            //Se manda a llamar la función para analizar el cuerpo del If
            analyzeStatement(ifStatement.getThenBranch(), localScope);
        }else{
            //Se valida si tiene un else el if para también analizarlo
            if (ifStatement.getElseBranch() != null) {
                analyzeStatement(ifStatement.getElseBranch(), localScope);
            }
        }
        

    }

    //Función para analizar condiciones y ver si son true o false
    private boolean analyzeConditions(Expression expression, Scope localScope){

        //Si es una expresión Binaria
        if(expression instanceof ExprBinary){
            ExprBinary binaryExpression = (ExprBinary) expression;
            Expression leftExpr = binaryExpression.getLeft();
            Expression rightExpr = binaryExpression.getRight();

            // Obtener los valores de las expresiones izquierda y derecha
            Object leftValue = getValue(leftExpr, localScope);
            Object rightValue = getValue(rightExpr, localScope);

            // Realizar la operación binaria según el operador
            switch (binaryExpression.getOperator().getType()) {
                case BANG_EQUAL:return valoresDiferentes(leftValue, rightValue);
                case EQUAL_EQUAL: return valoresIguales(leftValue, rightValue);
                case GREATER: return mayorQue(leftValue, rightValue);
                case GREATER_EQUAL: return mayorIgual(leftValue, rightValue);
                case LESS: return menorQue(leftValue, rightValue);
                case LESS_EQUAL: return menorIgual(leftValue, rightValue);

                default:
                    reportarError("Operador no compatible en la expresión binaria");
                    return false;
            }

        }else if(expression instanceof ExprLogical){

            ExprLogical logicalExpression = (ExprLogical) expression;
            Expression leftExpr = logicalExpression.getLeft();
            Expression rightExpr = logicalExpression.getRight();

            //Se obtienen los booleanos de ambas expresiones para compararlas
            boolean leftBoolean, rightBoolean;
            leftBoolean = analyzeConditions(leftExpr, localScope);
            rightBoolean = analyzeConditions(rightExpr, localScope);

            switch (logicalExpression.getOperator().getType()) {
                case AND:
                    return And(leftBoolean, rightBoolean);
                case OR:
                    return Or(leftBoolean, rightBoolean);
                default:
                 reportarError("Operador no compatible en la expresión lógica");
                return false;
            }
        }else if(expression instanceof ExprLiteral){
            ExprLiteral expr = (ExprLiteral) expression;
            Object exprValue = expr.getValue();
            boolean exprBoolean = (Boolean) exprValue;
            if(exprBoolean == true){
                return true;
            }
            return false;
        }
       return false;
}

    //Función para verificar si ambos valores son diferentes
    private boolean valoresDiferentes(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        double leftDouble = ((Number) leftValue).doubleValue();
        double rightDouble = ((Number) rightValue).doubleValue();
        if(leftDouble != rightDouble){
            return true;
        }else{
            return false;
        }
    } else {
        // Manejo de error para tipos no compatibles
        reportarError("No se pueden comparar dos valores que no sean números");
        return false;
    }
}

    //Función para verificar si ambos valores son iguales
    private boolean valoresIguales(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            double leftDouble = ((Number) leftValue).doubleValue();
        double rightDouble = ((Number) rightValue).doubleValue();
            if(leftDouble == rightDouble){
                return true;
            }else{
                return false;
            }
        } else {
            // Manejo de error para tipos no compatibles
            reportarError("No se pueden comparar dos valores que no sean números");
            return false;
        }
    }

    //Función para validar que el primer número es mayor que el segundo
    private boolean mayorQue(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        double leftDouble = ((Number) leftValue).doubleValue();
        double rightDouble = ((Number) rightValue).doubleValue();

        if(leftDouble > rightDouble){
            return true;
        }else{
            return false;
        }
    } else {
        // Manejo de error para tipos no compatibles
        reportarError("No se pueden comparar dos valores que no sean números");
        return false;
    }
}

    //Función para validar que el primer número es mayor o igual que el segundo
    private boolean mayorIgual(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            double leftDouble = ((Number) leftValue).doubleValue();
            double rightDouble = ((Number) rightValue).doubleValue();

            if(leftDouble >= rightDouble){
                return true;
            }else{
                return false;
            }
        } else {
            // Manejo de error para tipos no compatibles
            reportarError("No se pueden comparar dos valores que no sean números");
            return false;
        }
    }

    //Función para validar que el primer número es menor que el segundo
    private boolean menorQue(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        double leftDouble = ((Number) leftValue).doubleValue();
        double rightDouble = ((Number) rightValue).doubleValue();

        if(leftDouble < rightDouble){
            return true;
        }else{
            return false;
        }
    } else {
        // Manejo de error para tipos no compatibles
        reportarError("No se pueden comparar dos valores que no sean números");
        return false;
    }
}

    //Función para validar que el primer número es menor o igual que el segundo
    private boolean menorIgual(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            double leftDouble = ((Number) leftValue).doubleValue();
            double rightDouble = ((Number) rightValue).doubleValue();

            if(leftDouble <= rightDouble){
                return true;
            }else{
                return false;
            }
        } else {
            // Manejo de error para tipos no compatibles
            reportarError("No se pueden comparar dos valores que no sean números");
            return false;
        }
    }

    //Función para analizar los Loop (For o while)
    private void analyzeLoopStatement(StmtLoop loopStatement, Scope localScope) {

        //Manda a llamar la función de analizar Expresión para la condición del Loop
        analyzeExpression(loopStatement.getCondition(), localScope);

        //Se crea un Scope para el loop
        Scope blockScope = new Scope(localScope);

        //Se verifica si se cumple la condición para entrar al While
        if(analyzeConditions(loopStatement.getCondition(), localScope)){

            //Se realiza el análisis del loopBody porque la condición se cumplió
            analyzeBlockStatement2((StmtBlock)loopStatement.getBody(), blockScope);

            //Se va a analizar el número de veces que se cumpla la condción del While
            while(analyzeConditions(loopStatement.getCondition(), blockScope)){
                analyzeBlockStatement2((StmtBlock)loopStatement.getBody(), blockScope);
            }
            
        }

    }

    //Función para analizar los BlockStatement de los Loop
    private void analyzeBlockStatement2(StmtBlock blockStatement, Scope blockScope) {

        //Se saca la lista de Statements del blockStatement
        List<Statement> statements = blockStatement.getStatements();
    
        if (!statements.isEmpty()) {

            //Se asigna el primer valor de los Statement
            Statement firstStatement = statements.get(0);
    
            //Si el primer Statement es un StmtBlock es que es un ciclo for y para evitar el error se ingresa a su Lista de Statements que contiene
            if (firstStatement instanceof StmtBlock) {
                List<Statement> blockStatements= ((StmtBlock) firstStatement).getStatements();
                analyzeDeclarations(blockStatements, blockScope);
            }else{
                //Si no, es que era un While y se puede analizar directo
                analyzeDeclarations(statements, blockScope);
            }
        }
    }

    //Función para analizar los Prints
    private void analyzePrintStatement(StmtPrint printStatement, Scope localScope) {

        //Manda a llamar la función para analizar la Expresión que tenga el Print
        analyzeExpression(printStatement.getExpression(), localScope);

        // Obtén el valor de la expresión a imprimir
        Object printValue = getValue(printStatement.getExpression(), localScope);

        // Imprime en la consola el valor de la expresión
        if(printValue instanceof Number){
            double doubleValue = ((Number) printValue).doubleValue();
        if (doubleValue % 1 == 0) {
            // Si el número tiene decimales 0, imprímelo como entero
            System.out.println(((Number) printValue).longValue());
        } else {
            // Si el número tiene decimales, imprímelo como double
            System.out.println(printValue);
        }
        }else if(printValue instanceof String){
            System.out.println(printValue);
        }
        if(printValue instanceof ExprLiteral){
            ExprLiteral valor = (ExprLiteral) printValue;
            System.out.println(valor.getValue());
        }else if(printValue instanceof ExprBinary){
            Expression expr = (Expression) printValue;
            printValue = getValue(expr, localScope);
            System.out.println(printValue);
        }
        

    }

    //Función para analizar los return
    private void analyzeReturnStatement(StmtReturn returnStatement, Scope localScope) {

        //Se valida si el return tiene expresión o es null
        if (returnStatement.getExpression() != null) {

             //Si sí tiene expresión el return, se manda a llamar la función para analizarla
            analyzeExpression(returnStatement.getExpression(), localScope);

        }

    }

    //Función para analizar qué tipo de Stmt es
    private void analyzeStatement(Statement statement, Scope localScope) {

        if (statement instanceof StmtExpression) {
            analyzeExpressionStatement((StmtExpression) statement, localScope);
        } else if (statement instanceof StmtIf) {
            analyzeIfStatement((StmtIf) statement, localScope);
        } else if (statement instanceof StmtLoop) {
            analyzeLoopStatement((StmtLoop) statement, localScope);
        } else if (statement instanceof StmtPrint) {
            analyzePrintStatement((StmtPrint) statement, localScope);
        } else if (statement instanceof StmtReturn) {
            analyzeReturnStatement((StmtReturn) statement, localScope);
        } else if (statement instanceof StmtBlock) {
            analyzeBlockStatement((StmtBlock) statement, localScope);
        }

    }

     //Función para analizar las Expresiones con los tipos que se tienen
    private void analyzeExpression(Expression expression, Scope localScope) {

        if (expression instanceof ExprLiteral) {
           
        } else if (expression instanceof ExprAssign) {
            analyzeAssignExpression((ExprAssign) expression, localScope);
        } else if (expression instanceof ExprLogical) {
            analyzeLogicalExpression((ExprLogical) expression, localScope);
        } else if (expression instanceof ExprBinary) {
            analyzeBinaryExpression((ExprBinary) expression, localScope);
        } else if (expression instanceof ExprUnary) {
            analyzeUnaryExpression((ExprUnary) expression, localScope);
        } else if (expression instanceof ExprCallFunction) {
            analyzeCallFunctionExpression((ExprCallFunction) expression, localScope);
        } else if (expression instanceof ExprVariable) {
            analyzeVariableExpression((ExprVariable) expression, localScope);
        }

    }

    //Función para analizar Expresiones de asignación
    private void analyzeAssignExpression(ExprAssign assignExpression, Scope localScope) {

        //Se obtiene el nombre de la variable a la que se está asignando
        String varName = assignExpression.getName().getLexema();
        //Se obtiene el valor asociado con ese nombre de variable
        Object value = localScope.resolve(varName);
        //Se comprueba si el nombre de la variable está en el Hashmap
        boolean definido =localScope.isDefined(varName);

        //Se verifica si esta variable tiene un valor null y no está definido en el Hashmap
        if (value == null && !definido) {
            reportarError("Error en la asignación: Variable '" + varName + "' no declarada previamente.");
        } else {

            //Se manda a analizar el valor de la asignación
            analyzeExpression(assignExpression.getValue(), localScope);
            // Actualizar el valor en el ámbito local
            localScope.define(varName, assignExpression.getValue(),localScope);


        }
    }

    //Función para analizar Expresiones Lógicas (or, and)
    private void analyzeLogicalExpression(ExprLogical logicalExpression, Scope localScope) {
        //Se obtiene la expresión de cada parte, de la Iquierda y Derecha
        Expression leftExpr = logicalExpression.getLeft();
        Expression rightExpr = logicalExpression.getRight();

        //Se manda a llamar la función para analizar la expresión del lado Izquierdo
        analyzeExpression(leftExpr, localScope);

        //Se manda a llamar la función para analizar la expresión del lado Derecho
        analyzeExpression(rightExpr, localScope);
       
    }
    
    //Función para analizar Expresiones Binarias (Operaciones aritméticas)
private void analyzeBinaryExpression(ExprBinary binaryExpression, Scope localScope) {

    //Se obtiene la expresión de cada parte, de la Iquierda y Derecha
    Expression leftExpr = binaryExpression.getLeft();
    Expression rightExpr = binaryExpression.getRight();

    //Se manda a llamar la función para analizar la expresión del lado Izquierdo
    analyzeExpression(leftExpr, localScope);

    //Se manda a llamar la función para analizar la expresión del lado Derecho
    analyzeExpression(rightExpr, localScope);


}

//Función para obtener el valor de una expresión (ExprVariable o ExprLiteral).
private Object getValue(Expression expression, Scope localScope) {
    if (expression instanceof ExprVariable) {
        String varName = getVariableName(expression);
        Object value = localScope.resolve(varName);
        if(value instanceof String || value instanceof Number){
            return value;
        }else if(value instanceof ExprVariable){
            Expression var = (Expression) value;
            String variableName = getVariableName(var);
            return localScope.resolve(variableName);
        }else if(value instanceof ExprBinary){
            Expression expr = (Expression) value;
            value = getValue(expr, localScope);
            return value;
        }else{
            return value;
        }
    } else if (expression instanceof ExprLiteral) {
        return ((ExprLiteral) expression).getValue();
    } else if (expression instanceof ExprBinary) {
        return evaluateBinaryExpression((ExprBinary) expression, localScope);
    }
    return null;
}

//Función para verificar si ambos booleanos son true
private boolean And(boolean leftBoolean, boolean rightBoolean) {
        if(leftBoolean && rightBoolean){
            return true;
        }else{
            return false;
        }
}

//Función para verificar si al menos uno de los booleanos es true
private boolean Or(boolean leftBoolean, boolean rightBoolean) {
        if(leftBoolean==true || rightBoolean==true){
            return true;
        }else{
            return false;
        }
}

// Función para evaluar una expresión binaria y retornar el resultado
private Object evaluateBinaryExpression(ExprBinary binaryExpression, Scope localScope) {
    Expression leftExpr = binaryExpression.getLeft();
    Expression rightExpr = binaryExpression.getRight();

    // Obtener los valores de las expresiones izquierda y derecha
    Object leftValue = getValue(leftExpr, localScope);
    Object rightValue = getValue(rightExpr, localScope);

    // Realizar la operación binaria según el operador
    switch (binaryExpression.getOperator().getType()) {
        case PLUS:return sumarValores(leftValue, rightValue);
        case MINUS: return restarValores(leftValue, rightValue);
        case STAR: return multiplicarValores(leftValue, rightValue);
        case SLASH: return dividirValores(leftValue, rightValue);

        default:
            // Manejo de error para operadores no compatibles
            reportarError("Operador no compatible en la expresión binaria");
            return null;
    }
}

// Función para sumar dos valores, tratando de manejar diferentes tipos
private Object sumarValores(Object leftValue, Object rightValue) {
    if (leftValue instanceof String || rightValue instanceof String) {
        // Si al menos uno de los valores es una cadena, concatenarlos
        return String.valueOf(leftValue) + String.valueOf(rightValue);
    } else if (leftValue instanceof Number && rightValue instanceof Number) {
        // Si ambos valores son números, sumarlos
        return ((Number) leftValue).doubleValue() + ((Number) rightValue).doubleValue();
    } else {
        // Manejo de error para tipos no compatibles
        reportarError("No se pueden sumar valores de tipos no compatibles");
        return null;
    }
}

// Función para sumar dos valores, tratando de manejar diferentes tipos
private Object restarValores(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        return ((Number) leftValue).doubleValue() - ((Number) rightValue).doubleValue();
    } else {
        reportarError("No se pueden restar valores de tipos no compatibles");
        return null;
    }
}

// Función para sumar dos valores, tratando de manejar diferentes tipos
private Object multiplicarValores(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        return ((Number) leftValue).doubleValue() * ((Number) rightValue).doubleValue();
    } else {
        reportarError("No se pueden multiplicar valores de tipos no compatibles");
        return null;
    }
}

// Función para sumar dos valores, tratando de manejar diferentes tipos
private Object dividirValores(Object leftValue, Object rightValue) {
    if (leftValue instanceof Number && rightValue instanceof Number) {
        // Si ambos valores son números, sumarlos
        return ((Number) leftValue).doubleValue() / ((Number) rightValue).doubleValue();
    } else {
        reportarError("No se pueden dividir valores de tipos no compatibles");
        return null;
    }
}


    //Función para obtener el Nombre de la Variable
    private String getVariableName(Expression expression) {

        if (expression instanceof ExprVariable) {
            return ((ExprVariable) expression).getName().getLexema();
        }
        return null;

    }

    //Función para analizar Expresiones Unarias
    private void analyzeUnaryExpression(ExprUnary unaryExpression, Scope localScope) {

         //Se manda a analizar la expresión 
        analyzeExpression(unaryExpression.getOperand(), localScope);

    }

    //Función para analizar Expresiones de llamadas a Funciones
    private void analyzeCallFunctionExpression(ExprCallFunction callFunctionExpression, Scope localScope) {

    //Se obtiene el nombre de la función
    String funcName = ((ExprVariable) callFunctionExpression.getCallee()).getName().getLexema();

    //Se comprueba si el nombre de la función está en el Hashmap
    if (localScope.resolve(funcName) == null) {
        reportarError("Función '" + funcName + "' no declarada en este ámbito.");
    } else {

        // Se obtiene la declaración de la función del Scope.
        StmtFunction functionDeclaration = (StmtFunction) localScope.resolve(funcName);

        // Verificar si se tiene el número correcto de argumentos entre la función declarada y la llamada a esta.
        int expectedArgs = functionDeclaration.getParameters().size();
        int actualArgs = callFunctionExpression.getArguments().size();

        // Se comprueba si no son los mismos números de argumentos, marca error.
        if (expectedArgs != actualArgs) {
            reportarError("Error de llamada a función: La función '" + funcName +
                    "' espera " + expectedArgs + " argumento(s), pero se proporcionaron " + actualArgs + ".");
        }else{
            // Obtener la lista de parámetros de la función
            List<Token> parameters = functionDeclaration.getParameters();
            // Crear un nuevo ámbito para la asignación local de variables
            Scope functionScope = new Scope(localScope);

            // Asignar los valores de los argumentos a las variables correspondientes
            for (int i = 0; i < actualArgs; i++) {
                String paramName = parameters.get(i).getLexema();
                Expression argValue = callFunctionExpression.getArguments().get(i);

                // Analizar la expresión del argumento
                analyzeExpression(argValue, functionScope);

                // Asignar el valor al parámetro en el nuevo ámbito
                functionScope.define(paramName, argValue, functionScope);
            }

            // Llamada a la función de Block para crear su hashmap y analizar el cuerpo de la función
            analyzeBlockStatement(functionDeclaration.getBody(), functionScope);

                }

        
    }

}

    //Función para analizar Expresiones de Variables
    private void analyzeVariableExpression(ExprVariable variableExpression, Scope localScope) {

        //Se obtiene el Nombre de la variable
        String varName = variableExpression.getName().getLexema();
        //Se obtiene el valor asociado al nombre de la variable
        Object value = localScope.resolve(varName);
        //Se obtiene si está definida en el Hashmap o no
        boolean definido =localScope.isDefined(varName);

        //Se verifica si el valor de la variable es null y si no está definida
        if (value == null && !definido) {
            reportarError("Variable '" + varName + "' no declarada.");
        }

    }

}
