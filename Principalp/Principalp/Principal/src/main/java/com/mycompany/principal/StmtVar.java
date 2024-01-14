package com.mycompany.principal;



public class StmtVar extends Statement {

    final Token name;
    final Expression initializer;
    
    public StmtVar(Token name, Expression initializer) {
      this.name = name;
      this.initializer = initializer;
    }
  
    public void declarar(Tabla tabla) {
      String nombre = this.name.getLexema();
      Object valor = this.initializer.evaluar(); // evaluar la expresión
  
      if (tabla.existe(nombre)) {
        // Reportar error semántico 
      } else {
        // Declarar la variable en la tabla
        tabla.asignar(nombre, valor);
      }
    }

    @Override
    Object ejecutar(Tabla tabla) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ejecutar'");
    }
  
  }

    /*public void analizarSemantica(Tabla tabla) {

        if (tabla.existeIdentificador(name)) {
          // Error semántico, variable ya declarada
        }
    
        if (name != null) {
          inicializador.analizarSemantica(tabla); 
        }
    
        tabla.asignar(name, name);
    
      }
     void ejecutar(Tabla tabla) {
        try {
            if (initializer != null) {
                value = initializer.ejecutar(tabla);
            }

            if (!tabla.existeIdentificador(name.lexema)) {
                tabla.declarar(name.lexema, value);
            }
        } catch (Exception e) {
            System.err.println("Error al ejecutar: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

