package com.interprete.parser;

import com.interprete.ast.*;
import com.interprete.lexer.Lexer;
import com.interprete.token.Token;
import com.interprete.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Parser completo que utiliza el algoritmo de Pratt para parsear expresiones.
 * Convierte tokens en un AST (Abstract Syntax Tree).
 */
public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private Token peekToken;
    private final List<String> errors;
    
    // Maps para funciones de parseo de prefijo e infijo
    private final Map<TokenType, Function<Token, Expression>> prefixParseFns;
    private final Map<TokenType, Function<Expression, Expression>> infixParseFns;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.errors = new ArrayList<>();
        this.prefixParseFns = new HashMap<>();
        this.infixParseFns = new HashMap<>();
        
        // Inicializar tokens
        advanceTokens();
        advanceTokens();
        
        // Registrar funciones de parseo prefijo
        registerPrefix(TokenType.IDENT, this::parseIdentifier);
        registerPrefix(TokenType.INT, this::parseIntegerLiteral);
        registerPrefix(TokenType.TRUE, this::parseBooleanLiteral);
        registerPrefix(TokenType.FALSE, this::parseBooleanLiteral);
        registerPrefix(TokenType.NOT, this::parsePrefixExpression);
        registerPrefix(TokenType.MINUS, this::parsePrefixExpression);
        registerPrefix(TokenType.LPAREN, this::parseGroupedExpression);
        registerPrefix(TokenType.IF, this::parseIfExpression);
        registerPrefix(TokenType.FUNCTION, this::parseFunctionLiteral);
        
        // Registrar funciones de parseo infijo
        registerInfix(TokenType.PLUS, this::parseInfixExpression);
        registerInfix(TokenType.MINUS, this::parseInfixExpression);
        registerInfix(TokenType.MULTIPLICATION, this::parseInfixExpression);
        registerInfix(TokenType.DIVISION, this::parseInfixExpression);
        registerInfix(TokenType.EQ, this::parseInfixExpression);
        registerInfix(TokenType.NOE, this::parseInfixExpression);
        registerInfix(TokenType.LT, this::parseInfixExpression);
        registerInfix(TokenType.GT, this::parseInfixExpression);
        registerInfix(TokenType.LTE, this::parseInfixExpression);
        registerInfix(TokenType.GTE, this::parseInfixExpression);
        registerInfix(TokenType.LPAREN, this::parseCallExpression);
    }
    
    private void registerPrefix(TokenType tokenType, Function<Token, Expression> fn) {
        prefixParseFns.put(tokenType, fn);
    }
    
    private void registerInfix(TokenType tokenType, Function<Expression, Expression> fn) {
        infixParseFns.put(tokenType, fn);
    }
    
    private void advanceTokens() {
        currentToken = peekToken;
        peekToken = lexer.nextToken();
    }
    
    private boolean expectPeek(TokenType expected) {
        if (peekToken.tokenType() == expected) {
            advanceTokens();
            return true;
        } else {
            peekError(expected, peekToken.tokenType());
            return false;
        }
    }
    
    private void peekError(TokenType expected, TokenType actual) {
        errors.add(String.format("Expected next token to be %s, got %s instead", expected, actual));
    }
    
    /**
     * Parsea un programa completo (secuencia de statements).
     */
    public Program parseProgram() {
        List<Statement> statements = new ArrayList<>();
        
        while (currentToken.tokenType() != TokenType.EOF) {
            Statement stmt = parseStatement();
            if (stmt != null) {
                statements.add(stmt);
            }
            advanceTokens();
        }
        
        return new Program(statements);
    }
    
    /**
     * Parsea un statement según el tipo de token actual.
     */
    private Statement parseStatement() {
        return switch (currentToken.tokenType()) {
            case LET -> parseLetStatement();
            case RETURN -> parseReturnStatement();
            case WHILE -> parseWhileStatement();
            case FOR -> parseForStatement();
            default -> parseExpressionStatement();
        };
    }
    
    /**
     * Parsea un let statement: let x = 5;
     */
    private LetStatement parseLetStatement() {
        Token token = currentToken;
        advanceTokens();
        
        Identifier name = new Identifier(peekToken, currentToken.literal());
        
        if (!expectPeek(TokenType.ASSIGN)) return null;
        advanceTokens(); // skip '='
        
        Expression value = parseExpression(Precedence.LOWEST);
        
        if (peekToken.tokenType() == TokenType.SEMICOLON) {
            advanceTokens(); // skip ';'
        }
        
        return new LetStatement(token, name, value);
    }
    
    /**
     * Parsea un return statement: return 5;
     */
    private ReturnStatement parseReturnStatement() {
        Token token = currentToken;
        advanceTokens();
        
        Expression value = parseExpression(Precedence.LOWEST);
        
        if (peekToken.tokenType() == TokenType.SEMICOLON) {
            advanceTokens(); // skip ';'
        }
        
        return new ReturnStatement(token, value);
    }
    
    /**
     * Parsea un while statement: while (condición) { ... }
     */
    private WhileStatement parseWhileStatement() {
        Token token = currentToken;
        
        if (!expectPeek(TokenType.LPAREN)) return null;
        
        advanceTokens();
        Expression condition = parseExpression(Precedence.LOWEST);
        
        if (!expectPeek(TokenType.RPAREN)) return null;
        advanceTokens(); // skip ')'
        
        if (!expectPeek(TokenType.LBRACE)) return null;
        
        BlockStatement body = parseBlockStatement();
        
        return new WhileStatement(token, condition, body);
    }
    
    /**
     * Parsea un for statement: for (init; cond; post) { ... }
     */
    private ForStatement parseForStatement() {
        Token token = currentToken;
        
        if (!expectPeek(TokenType.LPAREN)) return null;
        
        advanceTokens();
        Statement initialization = currentToken.tokenType() == TokenType.SEMICOLON 
            ? null 
            : parseStatement();
        
        expectPeek(TokenType.SEMICOLON);
        advanceTokens();
        
        Expression condition = currentToken.tokenType() == TokenType.SEMICOLON
            ? new BooleanLiteral(new Token(TokenType.TRUE, "true"), true)
            : parseExpression(Precedence.LOWEST);
        
        expectPeek(TokenType.SEMICOLON);
        advanceTokens();
        
        Statement increment = currentToken.tokenType() == TokenType.RPAREN
            ? null
            : parseStatement();
        
        expectPeek(TokenType.RPAREN);
        advanceTokens();
        
        if (!expectPeek(TokenType.LBRACE)) return null;
        BlockStatement body = parseBlockStatement();
        
        return new ForStatement(token, initialization, condition, increment, body);
    }
    
    /**
     * Parsea un expression statement (expresión como statement).
     */
    private ExpressionStatement parseExpressionStatement() {
        Token token = currentToken;
        
        Expression expression = parseExpression(Precedence.LOWEST);
        
        if (peekToken.tokenType() == TokenType.SEMICOLON) {
            advanceTokens(); // skip ';'
        }
        
        return new ExpressionStatement(token, expression);
    }
    
    /**
     * Parsea un bloque de código: { ... }
     */
    private BlockStatement parseBlockStatement() {
        List<Statement> statements = new ArrayList<>();
        
        advanceTokens(); // skip '{'
        
        while (currentToken.tokenType() != TokenType.RBRACE && currentToken.tokenType() != TokenType.EOF) {
            Statement stmt = parseStatement();
            if (stmt != null) {
                statements.add(stmt);
            }
            advanceTokens();
        }
        
        return new BlockStatement(new Token(TokenType.LBRACE, "{"), statements);
    }
    
    /**
     * Parsea una expresión usando el algoritmo de Pratt.
     */
    private Expression parseExpression(Precedence precedence) {
        Function<Token, Expression> prefix = prefixParseFns.get(currentToken.tokenType());
        
        if (prefix == null) {
            noPrefixParseFnError(currentToken.tokenType());
            return null;
        }
        
        Expression leftExp = prefix.apply(currentToken);
        
        while (peekToken.tokenType() != TokenType.SEMICOLON && 
               precedence.getValue() < Precedence.getPrecedence(peekToken.tokenType()).getValue()) {
            advanceTokens();
            
            Function<Expression, Expression> infix = infixParseFns.get(currentToken.tokenType());
            if (infix == null) {
                return leftExp;
            }
            
            leftExp = infix.apply(leftExp);
        }
        
        return leftExp;
    }
    
    /**
     * Parsea un identificador.
     */
    private Expression parseIdentifier(Token token) {
        return new Identifier(token, token.literal());
    }
    
    /**
     * Parsea un literal entero.
     */
    private Expression parseIntegerLiteral(Token token) {
        try {
            int value = Integer.parseInt(token.literal());
            return new IntegerLiteral(token, value);
        } catch (NumberFormatException e) {
            errors.add(String.format("Could not parse %s as integer", token.literal()));
            return null;
        }
    }
    
    /**
     * Parsea un literal booleano.
     */
    private Expression parseBooleanLiteral(Token token) {
        return new BooleanLiteral(token, token.tokenType() == TokenType.TRUE);
    }
    
    /**
     * Parsea una expresión prefijo: !true, -5
     */
    private Expression parsePrefixExpression(Token token) {
        String operator = token.literal();
        
        advanceTokens();
        
        Expression right = parseExpression(Precedence.PREFIX);
        
        return new PrefixExpression(token, operator, right);
    }
    
    /**
     * Parsea una expresión infija: 5 + 3
     */
    private Expression parseInfixExpression(Expression left) {
        Token token = currentToken;
        String operator = token.literal();
        
        Precedence precedence = Precedence.getPrecedence(token.tokenType());
        advanceTokens();
        
        Expression right = parseExpression(precedence);
        
        return new InfixExpression(token, left, operator, right);
    }
    
    /**
     * Parsea una expresión agrupada: (5 + 3) * 2
     */
    private Expression parseGroupedExpression(Token token) {
        advanceTokens();
        
        Expression exp = parseExpression(Precedence.LOWEST);
        
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        
        return exp;
    }
    
    /**
     * Parsea una expresión if: if (condición) { ... } else { ... }
     */
    private Expression parseIfExpression(Token token) {
        if (!expectPeek(TokenType.LPAREN)) {
            return null;
        }
        
        advanceTokens();
        
        Expression condition = parseExpression(Precedence.LOWEST);
        
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        
        if (!expectPeek(TokenType.LBRACE)) {
            return null;
        }
        
        BlockStatement consequence = parseBlockStatement();
        
        BlockStatement alternative = null;
        if (peekToken.tokenType() == TokenType.ELSE) {
            advanceTokens();
            
            if (!expectPeek(TokenType.LBRACE)) {
                return null;
            }
            
            alternative = parseBlockStatement();
        }
        
        return new IfExpression(token, condition, consequence, alternative);
    }
    
    /**
     * Parsea un literal de función: function(x, y) { ... }
     */
    private Expression parseFunctionLiteral(Token token) {
        if (!expectPeek(TokenType.LPAREN)) {
            return null;
        }
        
        List<Identifier> parameters = parseFunctionParameters();
        
        if (!expectPeek(TokenType.LBRACE)) {
            return null;
        }
        
        BlockStatement body = parseBlockStatement();
        
        return new FunctionLiteral(token, parameters, body);
    }
    
    /**
     * Parsea los parámetros de una función: (x, y, z)
     */
    private List<Identifier> parseFunctionParameters() {
        List<Identifier> params = new ArrayList<>();
        
        if (peekToken.tokenType() == TokenType.RPAREN) {
            advanceTokens();
            return params;
        }
        
        advanceTokens();
        
        Identifier ident = new Identifier(currentToken, currentToken.literal());
        params.add(ident);
        
        while (peekToken.tokenType() == TokenType.COMMA) {
            advanceTokens();
            advanceTokens();
            
            ident = new Identifier(currentToken, currentToken.literal());
            params.add(ident);
        }
        
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        
        return params;
    }
    
    /**
     * Parsea una llamada a función: suma(1, 2)
     */
    private Expression parseCallExpression(Expression function) {
        List<Expression> arguments = parseCallArguments();
        return new CallExpression(currentToken, function, arguments);
    }
    
    /**
     * Parsea los argumentos de una llamada a función.
     */
    private List<Expression> parseCallArguments() {
        List<Expression> args = new ArrayList<>();
        
        if (peekToken.tokenType() == TokenType.RPAREN) {
            advanceTokens();
            return args;
        }
        
        advanceTokens();
        
        Expression arg = parseExpression(Precedence.LOWEST);
        if (arg != null) {
            args.add(arg);
        }
        
        while (peekToken.tokenType() == TokenType.COMMA) {
            advanceTokens();
            advanceTokens();
            
            arg = parseExpression(Precedence.LOWEST);
            if (arg != null) {
                args.add(arg);
            }
        }
        
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        
        return args;
    }
    
    private void noPrefixParseFnError(TokenType tokenType) {
        errors.add(String.format("No prefix parse function for %s found", tokenType));
    }
    
    public List<String> getErrors() {
        return errors;
    }
}