package com.interprete.repl;

import com.interprete.ast.Program;
import com.interprete.evaluator.Environment;
import com.interprete.evaluator.Evaluator;
import com.interprete.lexer.Lexer;
import com.interprete.parser.Parser;
import com.interprete.object.MObject;

import java.util.Scanner;

/**
 * Read-Eval-Print Loop (REPL).
 * Lee código del usuario, lo parsea y evalúa, mostrando el resultado.
 */
public class REPL {
    private static final String PROMPT = ">>> ";
    
    public static void start() {
        System.out.println("Bienvenido al interprete TPE!");
        System.out.println("Escribe 'salir' o 'end' para terminar.");
        
        Scanner scanner = new Scanner(System.in);
        Environment env = new Environment();
        
        while (true) {
            System.out.print(PROMPT);
            
            if (!scanner.hasNextLine()) {
                break;
            }
            
            String line = scanner.nextLine();
            
            if (line.trim().equalsIgnoreCase("exit") || line.trim().equalsIgnoreCase("end") || 
                line.trim().equalsIgnoreCase("salir")) {
                System.out.println("Hasta luego!");
                break;
            }
            
            if (line.trim().isEmpty()) {
                continue;
            }
            
            try {
                Lexer lexer = new Lexer(line);
                Parser parser = new Parser(lexer);
                
                Program program = parser.parseProgram();
                
                if (parser.getErrors().size() > 0) {
                    System.out.println("Errores del parser:");
                    for (String error : parser.getErrors()) {
                        System.out.println("  " + error);
                    }
                    continue;
                }
                
                Evaluator evaluator = new Evaluator();
                MObject evaluated = evaluator.eval(program, env);
                
                if (evaluated != null) {
                    System.out.println(evaluated.inspect());
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
}
