/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.principal;


public class Token {
    public TipoToken tipo;
    public String lexema;
    public Object literal;

    public Token(){
        this.literal = null;
    }

    public Object getLiteral() {
        this.literal =literal;
        return literal;
    }

    public Token(TipoToken tipo, String lexema) {
        this();
        this.tipo = tipo;
        this.lexema = lexema;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }

    public String toString() {
        return "-->" + tipo + " " + lexema + " " + literal ;
    }
}