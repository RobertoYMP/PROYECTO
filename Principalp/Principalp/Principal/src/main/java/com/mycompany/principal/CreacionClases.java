/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.principal;
import java.util.List;
/**
 *
 * @author user
 */
public class CreacionClases {
    private final String name;
    private final List<StmtFunction> methods;
    
    public CreacionClases(String name, List<StmtFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public List<StmtFunction> getMethods() {
        return methods;
    }
}
