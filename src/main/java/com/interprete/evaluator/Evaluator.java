package com.interprete.evaluator;

import com.interprete.ast.*;
import com.interprete.object.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Evalúa el AST generado por el parser.
 * Ejecuta el programa y retorna los resultados.
 */
public class Evaluator {
    private static final MBoolean TRUE = new MBoolean(true);
    private static final MBoolean FALSE = new MBoolean(false);
    private static final MNull NULL = MNull.getInstance();
    
    /**
     * Evalúa un nodo AST recursivamente.
     */
    public MObject eval(ASTNode node, Environment env) {
        if (node instanceof Program) {
            return evalProgram((Program) node, env);
        } else if (node instanceof LetStatement) {
            return evalLetStatement((LetStatement) node, env);
        } else if (node instanceof ReturnStatement) {
            return evalReturnStatement((ReturnStatement) node, env);
        } else if (node instanceof ExpressionStatement) {
            return eval(((ExpressionStatement) node).getExpression(), env);
        } else if (node instanceof BlockStatement) {
            return evalBlockStatement((BlockStatement) node, env);
        } else if (node instanceof WhileStatement) {
            return evalWhileStatement((WhileStatement) node, env);
        } else if (node instanceof ForStatement) {
            return evalForStatement((ForStatement) node, env);
        } else if (node instanceof IntegerLiteral) {
            return new MInteger(((IntegerLiteral) node).getValue());
        } else if (node instanceof BooleanLiteral) {
            return nativeBoolToBooleanObject(((BooleanLiteral) node).getValue());
        } else if (node instanceof Identifier) {
            return evalIdentifier((Identifier) node, env);
        } else if (node instanceof PrefixExpression) {
            PrefixExpression exp = (PrefixExpression) node;
            MObject right = eval(exp.getRight(), env);
            return evalPrefixExpression(exp.getOperator(), right);
        } else if (node instanceof InfixExpression) {
            InfixExpression exp = (InfixExpression) node;
            MObject left = eval(exp.getLeft(), env);
            MObject right = eval(exp.getRight(), env);
            return evalInfixExpression(exp.getOperator(), left, right);
        } else if (node instanceof IfExpression) {
            return evalIfExpression((IfExpression) node, env);
        } else if (node instanceof FunctionLiteral) {
            return evalFunctionLiteral((FunctionLiteral) node, env);
        } else if (node instanceof CallExpression) {
            return evalCallExpression((CallExpression) node, env);
        }
        
        return NULL;
    }
    
    /**
     * Evalúa un programa (secuencia de statements).
     */
    private MObject evalProgram(Program program, Environment env) {
        MObject result = NULL;
        
        for (Statement stmt : program.getStatements()) {
            result = eval(stmt, env);
            
            // Si encontramos un return, propagarlo
            if (result instanceof MReturnValue) {
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Evalúa un block statement.
     */
    private MObject evalBlockStatement(BlockStatement block, Environment env) {
        MObject result = NULL;
        
        for (Statement stmt : block.getStatements()) {
            result = eval(stmt, env);
            
            // Si encontramos un return, propagarlo inmediatamente
            if (result instanceof MReturnValue) {
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Evalúa un let statement (declaración de variable).
     */
    private MObject evalLetStatement(LetStatement stmt, Environment env) {
        MObject value = eval(stmt.getValue(), env);
        env.set(stmt.getName().getValue(), value);
        return value;
    }
    
    /**
     * Evalúa un return statement.
     */
    private MObject evalReturnStatement(ReturnStatement stmt, Environment env) {
        MObject value = eval(stmt.getReturnValue(), env);
        return new MReturnValue(value);
    }
    
    /**
     * Evalúa un while statement.
     */
    private MObject evalWhileStatement(WhileStatement stmt, Environment env) {
        MObject result = NULL;
        
        while (isTruthy(eval(stmt.getCondition(), env))) {
            result = eval(stmt.getBody(), env);
            
            // Si encontramos un return, propagarlo
            if (result instanceof MReturnValue) {
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * Evalúa un for statement.
     */
    private MObject evalForStatement(ForStatement stmt, Environment env) {
        MObject result = NULL;
        
        // Inicialización
        if (stmt.getInitialization() != null) {
            eval(stmt.getInitialization(), env);
        }
        
        // Condición, cuerpo e incremento
        while (stmt.getCondition() == null || isTruthy(eval(stmt.getCondition(), env))) {
            result = eval(stmt.getBody(), env);
            
            // Si encontramos un return, propagarlo
            if (result instanceof MReturnValue) {
                return result;
            }
            
            // Incremento
            if (stmt.getIncrement() != null) {
                eval(stmt.getIncrement(), env);
            }
        }
        
        return result;
    }
    
    /**
     * Evalúa un identificador (variable).
     */
    private MObject evalIdentifier(Identifier node, Environment env) {
        MObject value = env.get(node.getValue());
        return value != null ? value : NULL;
    }
    
    /**
     * Evalúa una expresión prefija: !true, -5
     */
    private MObject evalPrefixExpression(String operator, MObject right) {
        return switch (operator) {
            case "!" -> evalBangOperatorExpression(right);
            case "-" -> evalMinusPrefixOperatorExpression(right);
            default -> NULL;
        };
    }
    
    /**
     * Evalúa el operador !
     */
    private MObject evalBangOperatorExpression(MObject right) {
        if (right == TRUE) return FALSE;
        if (right == FALSE) return TRUE;
        if (right == NULL) return TRUE;
        return FALSE;
    }
    
    /**
     * Evalúa el operador - (negativo).
     */
    private MObject evalMinusPrefixOperatorExpression(MObject right) {
        if (!(right instanceof MInteger)) {
            return NULL;
        }
        
        Integer value = ((MInteger) right).getValue();
        return new MInteger(-value);
    }
    
    /**
     * Evalúa una expresión infija: 5 + 3, x == y
     */
    private MObject evalInfixExpression(String operator, MObject left, MObject right) {
        if (left instanceof MInteger && right instanceof MInteger) {
            return evalIntegerInfixExpression(operator, left, right);
        } else if (operator.equals("==")) {
            return nativeBoolToBooleanObject(left == right);
        } else if (operator.equals("!=")) {
            return nativeBoolToBooleanObject(left != right);
        } else {
            return NULL;
        }
    }
    
    /**
     * Evalúa expresiones infijas con enteros.
     */
    private MObject evalIntegerInfixExpression(String operator, MObject left, MObject right) {
        Integer leftVal = ((MInteger) left).getValue();
        Integer rightVal = ((MInteger) right).getValue();
        
        return switch (operator) {
            case "+" -> new MInteger(leftVal + rightVal);
            case "-" -> new MInteger(leftVal - rightVal);
            case "*" -> new MInteger(leftVal * rightVal);
            case "/" -> new MInteger(leftVal / rightVal);
            case "<" -> nativeBoolToBooleanObject(leftVal < rightVal);
            case ">" -> nativeBoolToBooleanObject(leftVal > rightVal);
            case "==" -> nativeBoolToBooleanObject(leftVal.equals(rightVal));
            case "!=" -> nativeBoolToBooleanObject(!leftVal.equals(rightVal));
            case "<=" -> nativeBoolToBooleanObject(leftVal <= rightVal);
            case ">=" -> nativeBoolToBooleanObject(leftVal >= rightVal);
            default -> NULL;
        };
    }
    
    /**
     * Evalúa una expresión if.
     */
    private MObject evalIfExpression(IfExpression exp, Environment env) {
        MObject condition = eval(exp.getCondition(), env);
        
        if (isTruthy(condition)) {
            return eval(exp.getConsequence(), env);
        } else if (exp.getAlternative() != null) {
            return eval(exp.getAlternative(), env);
        } else {
            return NULL;
        }
    }
    
    /**
     * Evalúa un literal de función.
     */
    private MObject evalFunctionLiteral(FunctionLiteral node, Environment env) {
        return new MFunction(node.getParameters(), node.getBody(), env);
    }
    
    /**
     * Evalúa una llamada a función.
     * Soporta recursión mediante nuevos environments.
     */
    private MObject evalCallExpression(CallExpression node, Environment env) {
        MObject function = eval(node.getFunction(), env);
        
        if (!(function instanceof MFunction)) {
            return NULL;
        }
        
        MFunction fn = (MFunction) function;
        
        // Evaluar argumentos
        List<MObject> args = evalExpressions(node.getArguments(), env);
        if (args.size() != fn.getParameters().size()) {
            return NULL;
        }
        
        // Crear nuevo environment extendido con los parámetros
        Environment extendedEnv = extendFunctionEnv(fn, args);
        
        // Evaluar el cuerpo de la función
        MObject result = eval(fn.getBody(), extendedEnv);
        
        // Desenvolver el return value
        if (result instanceof MReturnValue) {
            return ((MReturnValue) result).getValue();
        }
        
        return result;
    }
    
    /**
     * Evalúa una lista de expresiones (argumentos).
     */
    private List<MObject> evalExpressions(List<Expression> exps, Environment env) {
        List<MObject> results = new ArrayList<>();
        
        for (Expression exp : exps) {
            MObject evaluated = eval(exp, env);
            results.add(evaluated);
        }
        
        return results;
    }
    
    /**
     * Extiende el environment de una función con los valores de los argumentos.
     */
    private Environment extendFunctionEnv(MFunction fn, List<MObject> args) {
        Environment env = new Environment(fn.getEnv());
        
        for (int i = 0; i < fn.getParameters().size(); i++) {
            env.set(fn.getParameters().get(i).getValue(), args.get(i));
        }
        
        return env;
    }
    
    /**
     * Convierte un objeto a verdadero/falso.
     */
    private boolean isTruthy(MObject obj) {
        if (obj == NULL) return false;
        if (obj == TRUE) return true;
        if (obj == FALSE) return false;
        return true;
    }
    
    /**
     * Convierte un booleano nativo en MBoolean.
     */
    private MBoolean nativeBoolToBooleanObject(boolean input) {
        return input ? TRUE : FALSE;
    }
}
