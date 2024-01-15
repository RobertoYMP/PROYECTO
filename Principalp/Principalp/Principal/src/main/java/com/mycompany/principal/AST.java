package com.mycompany.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<Statement> statements; // Agregado para almacenar las declaraciones
    private Tabla tabla = new Tabla();
    
    public AST(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        statements = new ArrayList<>(); // Inicialización de la lista de declaraciones
    }

    @Override
            public boolean parse(){
                  PROGRAM();

                  if(!hayErrores) {
                      System.out.println("No se encontraron errores");
                  }else if (preanalisis.tipo == TipoToken.EOF) {
                      System.out.println("No se encontraron errores");
                  } else {
                      System.out.println("Hay errores");
                      return true;
                  }

                  return false;
    }
     // PROGRAM - DECLARATION
  public List<Statement> PROGRAM(){
        List<Statement> statements = new ArrayList<>();
        if(preanalisis.tipo != TipoToken.EOF){
            DECLARATION(statements);
            return statements;
        }
        return null;
    }
    
    public List<Statement> getStatements(){
        return statements;
    }
          //Declaraciones

    /*DECLARATION   - FUN_DECL DECLARATION
                    - VAR_DECL DECLARATION
                    - STATEMENT DECLARATION
                    - E
    */
        public void DECLARATION(List <Statement> statements){
                if(preanalisis.tipo== TipoToken.FUN){
                    Statement funDecl = FUN_DECL();
                    statements.add(funDecl);
                    DECLARATION(statements);
                }else if(preanalisis.tipo == TipoToken.VAR){
                    Statement varDecl = VAR_DECL();
                    statements.add(varDecl);
                    DECLARATION(statements);
                }else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN|| preanalisis.tipo== TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT || preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
                    Statement stmt = STATEMENT();
                    statements.add(stmt);
                    DECLARATION(statements);
                }
            }


    //DECLARACIONES
     // FUN_DECL - fun FUNCTION
    private Statement FUN_DECL() {
        if (preanalisis.tipo == TipoToken.FUN) {
            match(TipoToken.FUN);
            Statement funDecl = FUNCTION();
            return funDecl;
        } else {
            hayErrores = true;
           System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'fun')");
            return null;
        }
    }
    // VAR_DECL - var id VAR_INIT ;
    private Statement VAR_DECL() {
        if (preanalisis.tipo == TipoToken.VAR) {
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            Expression initialziaer = VAR_INIT();
            match(TipoToken.SEMICOLON);
           //tablaSimbolos.asignar(name.lexema, initialziaer);
            return new StmtVar(name, initialziaer);
        }
        return null;
    }

   private Expression VAR_ASSIGN2() {
    if (preanalisis.tipo == TipoToken.IDENTIFIER) {
        Token name = preanalisis;
        
        match(TipoToken.IDENTIFIER);
        match(TipoToken.EQUAL);
        Expression value = expression(); // Método que analiza el lado derecho de la asignación
        return new ExprAssign(name, value);
    } 
    return null;
}
  /* VAR_INIT - = EXPRESSION
                - E
    */
    private Expression VAR_INIT() {
        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            Expression initializer = expression();
            return initializer;
        }
        return null;
    }

    //SENTENCIAS
    
    /*STATEMENT - EXPR_STMT
            - FOR_STMT
            - IF_STMT
            - PRINT_STMT
            - RETURN_STMT
            - WHILE_STMT
            - BLOCK
    */
     public Statement STATEMENT(){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Statement expr = EXPR_STMT();
            return expr;
        }else if(preanalisis.tipo == TipoToken.FOR){
            Statement forStmt = FOR_STMT();
            return forStmt;
        }else if(preanalisis.tipo == TipoToken.IF){
            Statement ifsmt = IF_STMT();
            return ifsmt;
        }else if(preanalisis.tipo == TipoToken.PRINT){
            Statement print  = PRINT_STMT();
            return print;
        }else if(preanalisis.tipo == TipoToken.RETURN){
            Statement returnstm = RETURN_STMT();
            return returnstm;
        }else if(preanalisis.tipo == TipoToken.WHILE){
            Statement whilestm = WHILE_STMT();
            return whilestm;
        }else if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            Statement block = BLOCK();
            return block;
        }else{
            hayErrores=true;
            System.out.println("Error en: "+preanalisis.lexema);
            return null;
        }
    }
    // EXPR_STMT - EXPRESSION ;
    private Statement EXPR_STMT() {
        Expression expr = expression();
        match(TipoToken.SEMICOLON);
        return new StmtExpression(expr);
    }
    // FOR_STMT - for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT() {
        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        Statement initializer = FOR_STMT_1();
        Expression condition = FOR_STMT_2();
        Expression increment = FOR_STMT_3();
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();
        if (increment != null) {
            body = new StmtBlock(Arrays.asList(body, new StmtExpression(increment)));
        }
        if (condition == null) {
            condition = new ExprLiteral(true);
        }
        body = new StmtLoop(condition, body);

        if (initializer != null) {
            body = new StmtBlock(Arrays.asList(initializer, body));
        }
        return body;
    }

    private Statement FOR_STMT_1() {
         if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        }else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Statement stmt = EXPR_STMT();
                return stmt;
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
                return null;
        }else{
            hayErrores=true;
            System.out.println("Error en for "+preanalisis.lexema);
        }
        return null;
    }
    
 //FOR SEGUNDO ELEMENTO
    /*FOR_STMT_2 -EXPRESSION;
               - ;
    */
    private Expression FOR_STMT_2() {
     if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS ||
            preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = expression();
            match(TipoToken.SEMICOLON);
            return expr;
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        }else{
            hayErrores=true;
            System.out.println("Error en for"+ preanalisis.lexema);
            return null;
        }
    }
    /*FOR_STMT_3 - EXPRESSION
                 - E
    */
    private Expression FOR_STMT_3() {
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE ||
                preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.NULL ||
                preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
                return expression();
        }
        return null;
    }
    // IF_STMT - if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private Statement IF_STMT() {
        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        Expression condition = expression();
        match(TipoToken.RIGHT_PAREN);
        Statement thenBranch = STATEMENT();
        Statement elseBranch = ELSE_STATEMENT();
        return new StmtIf(condition, thenBranch, elseBranch);
    }

    /*ELSE_STATEMENT - else STATEMENT
                     - E 
    */
    private Statement ELSE_STATEMENT() {
        if (preanalisis.tipo == TipoToken.ELSE) {
            match(TipoToken.ELSE);
            return STATEMENT();
        }
        return null;
    }
 // PRINT_STMT - print EXPRESSION ;
    private Statement PRINT_STMT() {
        match(TipoToken.PRINT);
        Expression expr = expression();
        match(TipoToken.SEMICOLON);
        return new StmtPrint(expr);
    }
   // RETURN_STMT - return RETURN_EXP_OPC ;
        private Statement RETURN_STMT() {
            if (preanalisis.tipo == TipoToken.RETURN) {
                match(TipoToken.RETURN);
                Expression expr = null;
                expr = RETURN_EXP_OPC(expr);
                match(TipoToken.SEMICOLON);
                return new StmtReturn(expr);
            } else {
                hayErrores = true;
                System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba'return')");
                return null;
            }
        }
   /*RETURN_EXP_OPC - EXPRESSION
                    - E 
    */
        private Expression RETURN_EXP_OPC(Expression expr) {
            expr = expression();
            return expr;
        }
 // WHILE_STMT - while ( EXPRESSION ) STATEMENT
    private Statement WHILE_STMT() {
        if (preanalisis.tipo == TipoToken.WHILE) {
        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        Expression condition = expression();
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();
        return new StmtLoop(condition, body);
        }
        return null;
    }
 // BLOCK - { DECLARATION }
    private Statement BLOCK() {
        if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            List<Statement> stmts = new ArrayList<>();
            match(TipoToken.LEFT_BRACE);
            DECLARATION(stmts);
            match(TipoToken.RIGHT_BRACE);
            return new StmtBlock(stmts);
        }
        return null;
    }
    //EXPRESSION
    // EXPRESSION - ASSIGNMENT
    private Expression expression() {
        if (preanalisis.tipo == TipoToken.IDENTIFIER && next().tipo == TipoToken.EQUAL) {
        return VAR_ASSIGN2();
    }
            Expression expr = ASSIGNMENT();
            return expr;      
    }
      // ASSIGNMENT - LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT() {
        Expression expr = LOGIC_OR();
        expr = ASSIGNMENT_OPC(expr);
        return expr;
    }
        /*ASSIGNMENT_OPC - = EXPRESSION
                             - E
            */
    private Expression ASSIGNMENT_OPC(Expression expr) {
        if (preanalisis.tipo == TipoToken.EQUAL) {
            if (!(expr instanceof ExprVariable)) {
            throw new RuntimeException("El lado izquierdo de una asignación debe ser una variable.");
        }   
            match(TipoToken.EQUAL);
            Token operadorL = previous();
            Expression expr1 = expression();
            return new ExprAssign(operadorL, expr1);
        }
        return expr;
    }

    
      // LOGIC_OR - LOGIC_AND LOGIC_OR_2
    private Expression LOGIC_OR() {
        Expression expr = LOGIC_AND();
        expr = LOGIC_OR_2(expr);
        return expr;
    }
   /*LOGIC_OR_2 - or LOGIC_AND LOGIC_OR_2
                 - E
    */
    private Expression LOGIC_OR_2(Expression expr) {
        if (preanalisis.tipo == TipoToken.OR) {
            match(TipoToken.OR);
            Token operadorL = previous();
            Expression expr2 = LOGIC_AND();
            ExprLogical expl = new ExprLogical(expr, operadorL, expr2);
            return LOGIC_OR_2(expl);
        }
        return expr;
    }
 // LOGIC_AND - EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND() {
        Expression expr = EQUALITY();
        expr = LOGIC_AND_2(expr);
        return expr;
    }

    /*LOGIC_AND_2 - and EQUALITY LOGIC_AND_2
                   - E 
    */
    private Expression LOGIC_AND_2(Expression expr) {
        if (preanalisis.tipo == TipoToken.AND) {
            match(TipoToken.AND);
            Token operadorL = previous();
            Expression expr2 = EQUALITY();
            ExprLogical expl = new ExprLogical(expr, operadorL, expr2);
            return LOGIC_AND_2(expl);
        }
        return expr;
    }
  // EQUALITY - COMPARISON EQUALITY_2
    private Expression EQUALITY() {
        Expression expr = COMPARISON();
        expr = EQUALITY_2(expr);
        return expr;
    }
    /*EQUALITY_2 - != COMPARISON EQUALITY_2
                 - == COMPARISON EQUALITY_2
                 - E
    */
    private Expression EQUALITY_2(Expression expr) {
            if (preanalisis.tipo == TipoToken.BANG_EQUAL) {
                match(TipoToken.BANG_EQUAL);
                Token operador = previous();
                Expression expr2 = COMPARISON();
                ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
                return EQUALITY_2(expb);
            } else if (preanalisis.tipo == TipoToken.EQUAL_EQUAL) {
                match(TipoToken.EQUAL_EQUAL);
                Token operador = previous();
                Expression expr2 = COMPARISON();
                ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
                return EQUALITY_2(expb);
            }
        return expr;
    }
 // COMPARISON - TERM COMPARISON_2
    private Expression COMPARISON() {
        Expression expr = TERM();
        expr = COMPARISON_2(expr);
        return expr;
    }
  /*COMPARISON_2 - > TERM COMPARISON_2
                    - >= TERM COMPARISON_2
                    - < TERM COMPARISON_2
                    - <= TERM COMPARISON_2
                    - E 
    */
  public Expression COMPARISON_2(Expression expr){
        if(preanalisis.tipo == TipoToken.GREATER){
            match(TipoToken.GREATER);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2,tabla);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2, tabla);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.LESS){
            match(TipoToken.LESS);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2,tabla);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2,tabla);
            return COMPARISON_2(expb);
        }
        return expr;
    }

    // TERM - FACTOR TERM_2
    private Expression TERM() {
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }
  /*TERM_2 - - FACTOR TERM_2
            - + FACTOR TERM_2
            - E */
  private Expression TERM_2(Expression expr) {
        if (preanalisis.tipo == TipoToken.MINUS) {
            match(TipoToken.MINUS);
            Token operador = previous();
            Expression expr2 = FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
            return TERM_2(expb);
        } else if (preanalisis.tipo == TipoToken.PLUS) {
            match(TipoToken.PLUS);
            Token operador = previous();
            Expression expr2 = FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
            return TERM_2(expb);
        }
        return expr;
    }

  // FACTOR - UNARY FACTOR_2
    private Expression FACTOR() {
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }
/*FACTOR_2 - / UNARY FACTOR_2
                - * UNARY FACTOR_2
                - E 
    */
        private Expression FACTOR_2(Expression expr) {
            if (preanalisis.tipo == TipoToken.SLASH) {
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = UNARY();
                ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
                return FACTOR_2(expb);
            } else if (preanalisis.tipo == TipoToken.STAR) {
                match(TipoToken.STAR);
                Token operador = previous();
                Expression expr2 = UNARY();
                ExprBinary expb = new ExprBinary(expr, operador, expr2, tabla);
                return FACTOR_2(expb);
            }
            return expr;
        }
    /*UNARY - ! UNARY
            - - UNARY
            - CALL 
    */
            private Expression UNARY() {
                if (preanalisis.tipo == TipoToken.BANG) {
                    match(TipoToken.BANG);
                    Token operador = previous();
                    Expression expr = UNARY();
                    return new ExprUnary(operador, expr);
                } else if (preanalisis.tipo == TipoToken.MINUS) {
                    match(TipoToken.MINUS);
                    Token operador = previous();
                    Expression expr = UNARY();
                    return new ExprUnary(operador, expr);
                } else {
                    return CALL();
                }
            }

    // CALL - PRIMARY CALL_2
    private Expression CALL() {
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }
/*CALL_2 - ( ARGUMENTS_OPC ) CALL_2
            - E 
    */
        private Expression CALL_2(Expression expr) {
            if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = ARGUMENTS_OPC();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments, tabla);
                return ecf;
            }
            return expr;
        }
    /*PRIMARY - true
            - false
            - null
            - number
            - string
            - id
            - ( EXPRESSION )
    */
            private Expression PRIMARY() {
                if (preanalisis.tipo == TipoToken.TRUE) {
                    match(TipoToken.TRUE);
                    return new ExprLiteral(true);
                } else if (preanalisis.tipo == TipoToken.FALSE) {
                    match(TipoToken.FALSE);
                    return new ExprLiteral(false);
                } else if (preanalisis.tipo == TipoToken.NULL) {
                    match(TipoToken.NULL);
                    return new ExprLiteral(null);
                } else if (preanalisis.tipo == TipoToken.NUMBER) {
                    match(TipoToken.NUMBER);
                    Token numero = previous();
                    return new ExprLiteral(numero.literal);
                } else if (preanalisis.tipo == TipoToken.STRING) {
                    match(TipoToken.STRING);
                    Token cadena = previous();
                    return new ExprLiteral(cadena.literal);
                } else if (preanalisis.tipo == TipoToken.IDENTIFIER) {
                    match(TipoToken.IDENTIFIER);
                    Token id = previous();
                    return new ExprVariable(id, tabla);
                } else if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
                    match(TipoToken.LEFT_PAREN);
                    Expression expr = expression();
                    match(TipoToken.RIGHT_PAREN);
                    return new ExprGrouping(expr);
                }
                return null;
            }

 // FUNCTION - id ( PARAMETERS_OPC ) BLOCK
    private Statement FUNCTION() {
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            match(TipoToken.LEFT_PAREN);
            List<Token> params = PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            Statement body = BLOCK();
            return new StmtFunction(name, params, (StmtBlock) body);
        } else {
            hayErrores = true;
            System.out.println("Error en:  "+ preanalisis.lexema + ": (esperaba un id)");
            return null;
        }
    }
   /*FUNCTIONS - FUN_DECL FUNCTIONS
                - E 
    */
    private void FUNCTIONS() {
        if (preanalisis.tipo == TipoToken.FUN) {
            FUN_DECL();
            FUNCTIONS();
        }
    }

    /*PARAMETERS_OPC - PARAMETERS
                    - E 
    */
    private List<Token> PARAMETERS_OPC() {
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            List<Token> params = new ArrayList<>();
            PARAMETERS(params);
            return params;
        }
        return null;
    }
    // PARAMETERS - id PARAMETERS_2
    private List<Token> PARAMETERS(List<Token> params) {
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            Token paramToken = preanalisis;
            match(TipoToken.IDENTIFIER);
            params.add(paramToken);
            PARAMETERS_2(params);
        } else {
            hayErrores = true;
            System.out.println("Error en: "+ preanalisis.lexema + ":(esperaba un id)");
        }
        return null;
    }
        /*PARAMETERS_2 - , id PARAMETERS_2
                - E 
    */
    private List<Token> PARAMETERS_2(List<Token> parametros) {
        if (preanalisis.tipo == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            parametros.add(name);
            PARAMETERS_2(parametros);
        } 
        return parametros;
    }
    /*ARGUMENTS_OPC - EXPRESSION ARGUMENTS
                    - E
    */
   public List<Expression> ARGUMENTS_OPC(){
        List <Expression> arguments = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            arguments.add(expression());
            ARGUMENTS(arguments);
        }
        return arguments;
    }

      /*ARGUMENTS - , EXPRESSION ARGUMENTS
                - E 
    */
    private List<Expression> ARGUMENTS(List <Expression> arguments){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            arguments.add(expression());
            ARGUMENTS(arguments);
         }
        return arguments;
    }

    private void match(TipoToken tt) {
        if (preanalisis.tipo == tt) {
            i++;
            preanalisis = tokens.get(i);
        } else {
            hayErrores = true;
            System.out.println("Error encontrado");
        }
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }
   
    private Token next() {
    if (i < tokens.size() - 1) {
        return tokens.get(i + 1);
    } else {
        return new Token(TipoToken.EOF, "");
    }
}


   private void evaluateStatements(List<Statement> statements) {
    for (Statement stmt : statements) {
            stmt.evaluar(tabla); 
    }
}
   

}
    
