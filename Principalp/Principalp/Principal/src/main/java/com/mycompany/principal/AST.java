package com.mycompany.principal;

import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;


public class AST implements Parser{

    public int i=0;
    public Token preanalisis;
    public final List<Token> tokens;
    public boolean hayerrores = false;

    public AST(List<Token> tokens){
        this.tokens=tokens;
        preanalisis=this.tokens.get(i);
    }

    @Override
    public boolean parse(){
        PROGRAM();

        if(!hayerrores) {
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
        if(preanalisis.tipo == TipoToken.FUN || preanalisis.tipo == TipoToken.VAR || preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER ||  preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT ||  preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
            DECLARATION(statements);
        }
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

    // FUN_DECL - fun FUNCTION
    public Statement FUN_DECL(){
        if(preanalisis.tipo == TipoToken.FUN){
            match(TipoToken.FUN);
            Statement funDecl = FUNCTION();
            return funDecl;
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'fun')");
            return null;
        }
    }

    // VAR_DECL - var id VAR_INIT ;
    public Statement VAR_DECL(){
        Expression initialziaer = null;
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            if(preanalisis.tipo == TipoToken.EQUAL){
                initialziaer = VAR_INIT(initialziaer);
            }
            match(TipoToken.SEMICOLON);
            return new StmtVar(name, initialziaer);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba un 'var')");
            return null;
        }
    }

    /* VAR_INIT - = EXPRESSION
                - E
    */
    public Expression VAR_INIT(Expression initializer){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            initializer = EXPRESSION();
        }
        return initializer;
    }

    //Sentencias

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
            hayerrores=true;
            System.out.println("Error en: "+preanalisis.lexema);
            return null;
        }
    }

    // EXPR_STMT - EXPRESSION ;
    public Statement EXPR_STMT(){
        Expression expr = EXPRESSION();
        match(TipoToken.SEMICOLON);
        return new StmtExpression(expr);
    }
    // FOR_STMT - for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    public Statement FOR_STMT(){
        if(preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            match(TipoToken.LEFT_PAREN);
            Statement initializer = FOR_STMT_1();
            Expression condition = FOR_STMT_2();
            Expression increase = FOR_STMT_3();
            match(TipoToken.RIGHT_PAREN);
            List<Statement> statements = new ArrayList<>();
            statements.add(initializer);
            Statement loopBody = STATEMENT();
            statements.add(new StmtLoop( condition, new StmtBlock(Arrays.asList(loopBody, new StmtExpression(increase)))));
            return new StmtBlock(statements);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'for')");
            return null;
        }
    }
    //FOR PRIMER ELEMENTO
    /*FOR_STMT_1 - VAR_DECL
             - EXPR_STMT
             - ; 
    */
    public Statement FOR_STMT_1(){
        if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        }else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else{
            hayerrores=true;
            System.out.println("Error en for "+preanalisis.lexema);
        }
        return null;
    }
    //FOR SEGUNDO ELEMENTO
    /*FOR_STMT_2 -EXPRESSION;
               - ;
    */
    public Expression FOR_STMT_2(){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS ||
                preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new ExprGrouping(expr);
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return new ExprGrouping(null);
        }else{
            hayerrores=true;
            System.out.println("Error en for"+ preanalisis.lexema);
            return null;
        }
    }

    /*FOR_STMT_3 - EXPRESSION
                 - E
    */
    public Expression FOR_STMT_3(){
       EXPRESSION();
        
           return null;
    }

    // IF_STMT - if (EXPRESSION) STATEMENT ELSE_STATEMENT
    public Statement IF_STMT(){
        Statement elseBranch = null;
        if(preanalisis.tipo==TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);
            Expression condition = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            Statement thenBranch =STATEMENT();
            if(preanalisis.tipo == TipoToken.ELSE){
                elseBranch= ELSE_STATEMENT(elseBranch);
            }
            return new StmtIf(condition, thenBranch, elseBranch);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'if')");
            return null;
        }

    }

    /*ELSE_STATEMENT - else STATEMENT
                     - E 
    */
    public Statement ELSE_STATEMENT(Statement elseBranch){
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            elseBranch = STATEMENT();
            return elseBranch;
        }
        return elseBranch;
    }

    // PRINT_STMT - print EXPRESSION ;
    public Statement PRINT_STMT(){
        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new StmtPrint(expr);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'print')");
            return null;
        }
    }

    // RETURN_STMT - return RETURN_EXP_OPC ;
    public Statement RETURN_STMT(){
        Expression value=null;
        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
                value = RETURN_EXP_OPC(value);
            }
            match(TipoToken.SEMICOLON);
            return new StmtReturn(value);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba'return')");
            return null;
        }
    }

    /*RETURN_EXP_OPC - EXPRESSION
                    - E 
    */
    public Expression RETURN_EXP_OPC(Expression value){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            value = EXPRESSION();
            return value;
        }
        return value;
    }

    // WHILE_STMT - while ( EXPRESSION ) STATEMENT
    public Statement WHILE_STMT(){
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            Expression condition = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT();
            return new StmtLoop(condition, body);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ": (esperaba 'while')");
            return null;
        }
    }

    // BLOCK - { DECLARATION }
    public Statement BLOCK(){
        List <Statement> statements =new ArrayList<>();
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION(statements);
            match(TipoToken.RIGHT_BRACE);
            return new StmtBlock(statements);
        }else{
            hayerrores=true;
            System.out.println("Error en:  "+ preanalisis.lexema + ": (esperaba un '{')");
            return null;
        }
    }

    //EXPRESSION
    // EXPRESSION - ASSIGNMENT
    public Expression EXPRESSION(){
        return ASSIGNMENT();
    }

    // ASSIGNMENT - LOGIC_OR ASSIGNMENT_OPC
    public Expression ASSIGNMENT(){
        Expression expr = LOGIC_OR();
        if(preanalisis.tipo == TipoToken.EQUAL){
            expr = ASSIGNMENT_OPC(expr);
        }
        return expr;
    }

    /*ASSIGNMENT_OPC - = EXPRESSION
                     - E
    */
    public Expression ASSIGNMENT_OPC(Expression value){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Token name = previous();
            value = EXPRESSION();
            return new ExprAssign(name, value);
        }
        return value;
    }

    // LOGIC_OR - LOGIC_AND LOGIC_OR_2
    public Expression LOGIC_OR(){
        Expression expr = LOGIC_AND();
        if(preanalisis.tipo == TipoToken.OR){
            expr = LOGIC_OR_2(expr);
        }
        return expr;
    }

    /*LOGIC_OR_2 - or LOGIC_AND LOGIC_OR_2
                 - E
    */
    public Expression LOGIC_OR_2(Expression expr){
        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            Token operador = previous();
            Expression expr2 = LOGIC_AND();
            Expression expb = new ExprLogical(expr, operador, expr2);
            return LOGIC_OR_2(expb);
        }
        return expr;
    }

    // LOGIC_AND - EQUALITY LOGIC_AND_2
    public Expression LOGIC_AND(){
        Expression expr = EQUALITY();
        if(preanalisis.tipo == TipoToken.AND){
            expr =  LOGIC_AND_2(expr);
        }
        return expr;
    }

    /*LOGIC_AND_2 - and EQUALITY LOGIC_AND_2
                   - E 
    */
    public Expression LOGIC_AND_2(Expression expr){
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operador = previous();
            Expression expr2 = EQUALITY();
            Expression expb = new ExprLogical(expr, operador, expr2);
            return LOGIC_AND_2(expb);
        }
        return expr;
    }

    // EQUALITY - COMPARISON EQUALITY_2
    public Expression EQUALITY(){
        Expression expr = COMPARISON();
        if(preanalisis.tipo == TipoToken.BANG_EQUAL || preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            expr = EQUALITY_2(expr);;
        }
        return expr;
    }

    /*EQUALITY_2 - != COMPARISON EQUALITY_2
                 - == COMPARISON EQUALITY_2
                 - E
    */
    public Expression EQUALITY_2(Expression expr){
        if(preanalisis.tipo == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            Token operador = previous();
            Expression expr2= COMPARISON();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return EQUALITY_2(expb);
        }else if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            Token operador = previous();
            Expression expr2= COMPARISON();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return EQUALITY_2(expb);
        }
        return expr;
    }

    // COMPARISON - TERM COMPARISON_2
    public Expression COMPARISON(){
        Expression expr = TERM();
        if(preanalisis.tipo == TipoToken.GREATER ||preanalisis.tipo == TipoToken.GREATER_EQUAL || preanalisis.tipo == TipoToken.LESS || preanalisis.tipo == TipoToken.LESS_EQUAL){
            expr =  COMPARISON_2(expr);
        }
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
            ExprBinary expb= new ExprBinary(expr, operador, expr2);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.LESS){
            match(TipoToken.LESS);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2);
            return COMPARISON_2(expb);
        }else if(preanalisis.tipo == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);
            Token operador = previous();
            Expression expr2= TERM();
            ExprBinary expb= new ExprBinary(expr, operador, expr2);
            return COMPARISON_2(expb);
        }
        return expr;
    }

    // TERM - FACTOR TERM_2
    public Expression TERM(){
        Expression expr = FACTOR();
        if(preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.PLUS){
            expr = TERM_2(expr);
        }
        return expr;
    }

    /*TERM_2 - - FACTOR TERM_2
            - + FACTOR TERM_2
            - E */
    public Expression TERM_2(Expression expr){
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operador = previous();
            Expression expr2=FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return TERM_2(expb);
        }else if(preanalisis.tipo == TipoToken.PLUS){
            match(TipoToken.PLUS);
            Token operador = previous();
            Expression expr2 = FACTOR();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return TERM_2(expb);
        }
        return expr;
    }

    // FACTOR - UNARY FACTOR_2
    public Expression FACTOR(){
        Expression expr = UNARY();
        if(preanalisis.tipo == TipoToken.STAR || preanalisis.tipo == TipoToken.SLASH){
            expr = FACTOR_2(expr);
        }
        return expr;
    }

    /*FACTOR_2 - / UNARY FACTOR_2
                - * UNARY FACTOR_2
                - E 
    */
    public Expression FACTOR_2(Expression expr){
        if(preanalisis.tipo == TipoToken.SLASH){
            match(TipoToken.SLASH);
            Token operador = previous();
            Expression expr2 = UNARY();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return FACTOR_2(expb);
        }else if(preanalisis.tipo == TipoToken.STAR){
            match(TipoToken.STAR);
            Token operador = previous();
            Expression expr2= UNARY();
            ExprBinary expb = new ExprBinary(expr, operador, expr2);
            return FACTOR_2(expb);
        }
        return expr;
    }

    /*UNARY - ! UNARY
            - - UNARY
            - CALL 
    */
    public Expression UNARY(){
        if(preanalisis.tipo == TipoToken.BANG){
            match(TipoToken.BANG);
            Token operador = previous();
            Expression expr = UNARY();
            return new ExprUnary(operador, expr);
        }else if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operador = previous();
            Expression expr = UNARY();
            return new ExprUnary(operador, expr);
        }else if(preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            return CALL();
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema);
            return null;
        }
    }

    // CALL - PRIMARY CALL_2
    public Expression CALL(){
        Expression expr = PRIMARY();
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            expr = CALL_2(expr);
        }
        return expr;
    }

    /*CALL_2 - ( ARGUMENTS_OPC ) CALL_2
            - E 
    */
    public Expression CALL_2(Expression expr){
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            List <Expression> arguments = new ArrayList<>();
            arguments = ARGUMENTS_OPC();
            //Argumentos que retorna Arguments_opc()
            match(TipoToken.RIGHT_PAREN);
            ExprCallFunction ecf = new ExprCallFunction(expr, arguments);
            CALL_2(ecf);

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
    public Expression PRIMARY(){
        if(preanalisis.tipo == TipoToken.TRUE){
            match(TipoToken.TRUE);
            return new ExprLiteral(true);
        }else if(preanalisis.tipo == TipoToken.FALSE){
            match(TipoToken.FALSE);
            return new ExprLiteral(false);
        }else if(preanalisis.tipo == TipoToken.NULL){
            match(TipoToken.NULL);
            return new ExprLiteral(null);
        }else if(preanalisis.tipo == TipoToken.NUMBER){
            match(TipoToken.NUMBER);
            Token numero= previous();
            return new ExprLiteral(numero.getLiteral());
        }else if(preanalisis.tipo == TipoToken.STRING){
            match(TipoToken.STRING);
            Token cadena = previous();
            return new ExprLiteral(cadena.getLiteral());
        }else if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token id = previous();
            return new ExprVariable(id);
        }else if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            Expression expression =EXPRESSION();
            //Tiene que ser cachado lo que retorna
            match(TipoToken.RIGHT_PAREN);
            return new ExprGrouping(expression);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema);
            return null;
        }
    }
    // FUNCTION - id ( PARAMETERS_OPC ) BLOCK
    public Statement FUNCTION(){
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token name=previous();
            match(TipoToken.LEFT_PAREN);
            List <Token> params = PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            Statement body = BLOCK();
            return new StmtFunction(name, params, (StmtBlock) body);
        }else{
            hayerrores=true;
            System.out.println("Error en:  "+ preanalisis.lexema + ": (esperaba un id)");
            return null;
        }
    }

    /*FUNCTIONS - FUN_DECL FUNCTIONS
                - E 
    */
    // public void FUNCTIONS(){
    //     if(preanalisis.tipo == TipoToken.FUN){
    //         FUN_DECL();
    //         FUNCTIONS();
    //     }
    // }

    /*PARAMETERS_OPC - PARAMETERS
                    - E 
    */
    public List <Token> PARAMETERS_OPC(){
        List <Token> params = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            PARAMETERS(params);
        }
        return params;
    }

    // PARAMETERS - id PARAMETERS_2
    public void PARAMETERS(List <Token> params){
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            Token paramToken=preanalisis;
            match(TipoToken.IDENTIFIER);
            params.add(paramToken);
            PARAMETERS_2(params);
        }else{
            hayerrores=true;
            System.out.println("Error en: "+ preanalisis.lexema + ":(esperaba un id)");
        }
    }

    /*PARAMETERS_2 - , id PARAMETERS_2
                - E 
    */
    public void PARAMETERS_2(List <Token> params){
        while(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            if(preanalisis.tipo == TipoToken.IDENTIFIER){
                Token additionalParamToken=preanalisis;
                match(TipoToken.IDENTIFIER);
                params.add(additionalParamToken);
            }
        }
    }

    /*ARGUMENTS_OPC - EXPRESSION ARGUMENTS
                    - E
    */
    public List<Expression> ARGUMENTS_OPC(){
        List <Expression> arguments = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            arguments.add(EXPRESSION());
            ARGUMENTS(arguments);
        }
        return arguments;
    }

    /*ARGUMENTS - , EXPRESSION ARGUMENTS
                - E 
    */
    public void ARGUMENTS(List <Expression> arguments){
        while(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            arguments.add(EXPRESSION());
        }
    }

    void match(TipoToken tt) {
        if (preanalisis.tipo == tt) {
            i++;
            if (i < tokens.size()) {
                preanalisis = tokens.get(i);
            }
        } else {
            hayerrores=true;
            System.out.println("Error en:  " +preanalisis.lexema+": Se esperaba " + tt);
        }
    }

    private Token previous(){
        return this.tokens.get(i-1);
    }
}