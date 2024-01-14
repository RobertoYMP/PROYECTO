/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.principal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("while", TipoToken.WHILE);
        palabrasReservadas.put("var",TipoToken.VAR);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("false",TipoToken.FALSE);
        palabrasReservadas.put("true",TipoToken.TRUE);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("and",TipoToken.AND);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("print",TipoToken.PRINT);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("EOF", TipoToken.EOF);
        palabrasReservadas.put("or", TipoToken.OR);


    }

    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scanTokens(){
        int estado = 0;
        char c;
        String lexema = "";

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);
//Automatas para los distintos caracteres
            switch (estado){
                case 0:
                    if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;

                        /*while(Character.isDigit(c)){
                            lexema += c;
                            i++;
                            c = source.charAt(i);
                        }
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        lexema = "";
                        estado = 0;
                        tokens.add(t);
                        */

                    }else if(c == '>'){
                        estado = 1;
                        lexema += c;
                    }else if(c == '<'){
                        estado = 4;
                        lexema += c;
                    }else if(c == '='){
                        estado = 7;
                        lexema += c;
                    }else if(c == '!'){
                        estado = 10;
                        lexema += c;
                    }else if(c == '"'){
                        estado = 24;
                        lexema += c;
                    }else if(c == '/'){
                        estado = 26;
                        lexema += c;
                    }else if(c == '+'){
                        Token t = new Token(TipoToken.PLUS,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == '-'){
                        Token t = new Token(TipoToken.MINUS,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == ','){
                        Token t = new Token(TipoToken.COMMA,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == ')'){
                        Token t = new Token(TipoToken.RIGHT_PAREN,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == '('){
                        Token t = new Token(TipoToken.LEFT_PAREN,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == '}'){
                        Token t = new Token(TipoToken.RIGHT_BRACE,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == '{'){
                        Token t = new Token(TipoToken.LEFT_BRACE,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == '*'){
                        Token t = new Token(TipoToken.STAR,lexema+c);
                        tokens.add(t);
                        lexema = "";

                    }else if(c == ';'){
                        Token t = new Token(TipoToken.SEMICOLON,lexema+c);
                        tokens.add(t);
                    }
                    break;

                case 1:
                    if(c == '='){
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                    }else{
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";

                    break;
                case 4:
                    if(c == '='){
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                    }else{
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);
                        i--;
                    }
                    estado = 0;
                    lexema = "";
                    break;
                case 7:
                    if(c == '='){
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                    }else{
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;
                case 10:
                    if(c == '='){
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                    }else{
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;


                case 13:
                    if(Character.isLetterOrDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }else if(c == '.'){
                        estado = 16;
                        lexema += c;
                    }else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

                case 16:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }

                    break;

                case 17:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }

                    break;

                case 18:
                    if (c == '+' || c == '-') {
                        //Token t = new Token(TipoToken.PLUS,lexema);
                        //tokens.add(t);

                        //Token t = new Token(TipoToken.MINUS,lexema);
                        //tokens.add(t);
                        lexema += c;
                        estado = 19;
                    }else if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    break;
                case 19:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    break;

                case 20:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }else{
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.NUMBER, lexema);
                            tokens.add(t);
                        }else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;
                case 24:
                    if(Character.isJavaIdentifierPart(c)){
                        estado = 24;
                        lexema += c;
                    }else if(c == '"' ){
                        lexema += c;
                        estado = 0;
                        //String valorToken = lexema.substring(1, lexema.length() - 1); // Elimina las comillas al principio y al final
                        Token t = new Token(TipoToken.STRING, lexema, lexema.replace('\"', ' ').trim());
                        tokens.add(t);
                        lexema = "";
                    }else{
                        lexema += c;
                    }
                    break;
                case 26:
                    if(c == '*'){
                        estado = 27;
                        lexema += c;
                    }else if(c == '/'){
                        estado = 30;
                        lexema += c;
                    }else{
                        Token t = new Token(TipoToken.SLASH, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 27:
                    if(c == '*'){
                        estado = 28;
                        lexema += c;
                    }else{
                        estado = 27;
                        lexema += c;
                    }
                    break;
                case 28:
                    if(c == '*'){
                        estado = 28;
                        lexema += c;
                    }else if(c == '/'){
                        estado = 0;
                        lexema = "";
                    }else{
                        estado = 27;
                        lexema += c;
                    }
                    break;
                case 30:
                    if(c == '\n'){
                        estado = 0;
                        lexema = "";
                    }else{
                        estado = 30;
                        lexema += c;
                    }

                    break;

            }
        }
        if(estado == 24 || estado == 27){
            System.out.println("Error");
        }
        return tokens;
    }

}