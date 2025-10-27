# Guía de Compiladores e Intérpretes

Una explicación detallada de cómo funcionan los compiladores e intérpretes, con enfoque en los componentes que implementamos en este proyecto.

---

## Tabla de Contenidos

1. [¿Qué es un Compilador vs Intérprete?](#qué-es-un-compilador-vs-intérprete)
2. [Fases de un Compilador/Intérprete](#fases-de-un-compiladorintérprete)
3. [El Lexer (Análisis Léxico)](#el-lexer-análisis-léxico)
4. [El Parser (Análisis Sintáctico)](#el-parser-análisis-sintáctico)
5. [El AST (Abstract Syntax Tree)](#el-ast-abstract-syntax-tree)
6. [El Evaluator (Análisis Semántico y Ejecución)](#el-evaluator-análisis-semántico-y-ejecución)
7. [La Tabla de Símbolos (Environment)](#la-tabla-de-símbolos-environment)
8. [El REPL (Read-Eval-Print Loop)](#el-repl-read-eval-print-loop)
9. [Flujo Completo: De Código a Ejecución](#flujo-completo-de-código-a-ejecución)
10. [Comparación con Compiladores Reales](#comparación-con-compiladores-reales)

---

## ¿Qué es un Compilador vs Intérprete?

### Compilador

Un **compilador** traduce código de un lenguaje de alto nivel a código de máquina o bytecode.

**Características:**
- Traduce TODO el programa antes de ejecutarlo
- Genera código objeto/máquina
- Más rápido en tiempo de ejecución (ya compilado)
- Ejemplos: GCC (C/C++), javac (Java → bytecode)

**Proceso:**
```
Código fuente → Análisis → Árbol AST → Generación de código → Código máquina
```

### Intérprete

Un **intérprete** ejecuta el código línea por línea, directamente.

**Características:**
- Ejecuta el código mientras lo analiza
- No genera código objeto
- Más lento en tiempo de ejecución (analiza todo el tiempo)
- Más flexible para REPL y desarrollo
- Ejemplos: Python, JavaScript, Ruby

**Proceso:**
```
Código fuente → Análisis línea por línea → Ejecución inmediata
```

### ¿Qué es Nuestro Proyecto?

Nuestro intérprete es un **tree-walking interpreter** (intérprete que recorre árboles):
- Analiza el código completo
- Construye un AST
- Recorre el AST ejecutando cada nodo

**Ventaja:** Más fácil de implementar y entender que compiladores reales.

---

## Fases de un Compilador/Intérprete

Un compilador o intérprete moderno pasa por **múltiples fases** para transformar código fuente en ejecución:

```
┌─────────────────────────────────────────────────────────────┐
│ CÓDIGO FUENTE                                                │
│ "let x = 5 + 3;"                                             │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 1. ANÁLISIS LÉXICO (Lexer)                                   │
│ Tokeniza el código en unidades significativas                │
│ [LET, IDENT("x"), ASSIGN, INT(5), PLUS, INT(3), SEMICOLON]  │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. ANÁLISIS SINTÁCTICO (Parser)                               │
│ Construye el árbol de sintaxis (AST)                        │
│ Program → LetStatement → Identifier + InfixExpression       │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. ANÁLISIS SEMÁNTICO (Evaluator)                            │
│ Verifica tipos, scopes, ejecuta el código                  │
│ Environment: { "x": 8 }                                      │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ EJECUCIÓN                                                     │
│ Resultado: 8                                                  │
└─────────────────────────────────────────────────────────────┘
```

### Fase 1: Análisis Léxico (Lexer)

Convierte texto plano en **tokens** (palabras reconocibles).

### Fase 2: Análisis Sintáctico (Parser)

Construye la **estructura sintáctica** del programa (AST).

### Fase 3: Análisis Semántico (Evaluator)

Verifica **significado y tipos**, ejecuta el código.

---

## El Lexer (Análisis Léxico)

### ¿Qué es el Lexer?

El **Lexer** (o tokenizer/scanner) es la primera fase del proceso de compilación/interpretación. Su trabajo es convertir el código fuente (una secuencia de caracteres) en una secuencia de **tokens** (palabras significativas).

### ¿Por qué es Necesario?

Cuando escribes:
```go
let x = 5 + 3;
```

La computadora ve:
```
['l', 'e', 't', ' ', 'x', ' ', '=', ' ', '5', ' ', '+', ' ', '3', ';']
```

Necesitamos agrupar esto en **palabras significativas**:
```
[LET, IDENT("x"), ASSIGN, INT(5), PLUS, INT(3), SEMICOLON]
```

### ¿Cómo Funciona?

El lexer lee carácter por carácter y decide qué tipo de token está viendo:

```
┌──────────────────────────────────────────────────┐
│ Método: nextToken()                               │
└──────────────────────────────────────────────────┘
                ↓
    ┌───────────────────────┐
    │ Lee siguiente carácter│
    └───────────────────────┘
                ↓
    ┌──────────────────────────────────┐
    │ ¿Es letra? → Lee identificador  │
    │ ¿Es dígito? → Lee número        │
    │ ¿Es '='? → Leo siguiente        │
    │   - Si es '=' → Token EQ (==)   │
    │   - Si no → Token ASSIGN (=)    │
    └──────────────────────────────────┘
                ↓
    ┌───────────────────────┐
    │ Retorna el token      │
    └───────────────────────┘
```

### Ejemplo Paso a Paso

**Input:** `let x = 5;`

| Paso | Carácter | Token Generado | Notas |
|------|----------|---------------|-------|
| 1 | `l` | - | Empieza identificador |
| 2 | `e` | - | Continúa identificador |
| 3 | `t` | - | Termina identificador |
| 4 | ` ` | `LET` | Retorna keyword "let" |
| 5 | `x` | - | Empieza nuevo identificador |
| 6 | ` ` | `IDENT("x")` | Retorna identificador |
| 7 | `=` | - | Léxico simple |
| 8 | ` ` | `ASSIGN` | Retorna operador |
| 9 | `5` | `INT(5)` | Lee número |
| 10 | `;` | `SEMICOLON` | Retorna delimitador |

### Implementación en Nuestro Proyecto

```java
public class Lexer {
    private String source;           // Código fuente
    private int currentPos;           // Posición actual
    private String currentChar;       // Carácter actual
    private int readCurrentPos;       // Posición de lectura
    
    public Token nextToken() {
        skipWhitespace();  // Omite espacios
        
        if (currentChar.equals("let")) {
            return new Token(TokenType.LET, "let");
        }
        // ... más casos
    }
}
```

### Tipos de Tokens que Reconoce

| Tipo | Ejemplo | Tokens Generados |
|------|---------|-----------------|
| Keyword | `let`, `function` | `LET`, `FUNCTION` |
| Identificador | `x`, `edad` | `IDENT` |
| Literal entero | `5`, `100` | `INT` |
| Operador | `+`, `-`, `*`, `/` | `PLUS`, `MINUS`, etc. |
| Comparador | `==`, `!=`, `<` | `EQ`, `NOE`, `LT` |
| Delimitador | `(`, `)`, `{`, `}` | `LPAREN`, `RPAREN`, etc. |
| Puntuación | `;`, `,` | `SEMICOLON`, `COMMA` |

### Reglas Especiales

#### 1. Identificadores con Regex

```java
private boolean isLetter(String character) {
    if (character.isEmpty()) return false;
    char c = character.charAt(0);
    return Character.isLetter(c) || c == '_';
}
```

**Soporta:**
- Letras: `a-z`, `A-Z`
- Caracteres especiales: `ñ`, `Ñ` (español)
- Guion bajo: `_`

#### 2. Operadores de Dos Caracteres

```java
if (currentChar.equals("=")) {
    if (peekCharacter().equals("=")) {
        // Es '=='
        return new Token(TokenType.EQ, "==");
    } else {
        // Es '='
        return new Token(TokenType.ASSIGN, "=");
    }
}
```

### Ventajas del Lexer

✅ **Separación de responsabilidades**: El parser no necesita preocuparse por caracteres individuales
✅ **Eficiencia**: Lee de una sola vez, agrupa caracteres
✅ **Reutilización**: Un token puede representar múltiples caracteres (`==` en lugar de `=`, `=`)

### Desafíos Comunes

❌ **Whitespace**: Determinar cuándo ignorar espacios
❌ **Lookahead**: Necesidad de ver el siguiente carácter sin consumirlo
❌ **Comentarios**: Manejo de líneas completas comentadas (no implementado aquí)
❌ **Strings multi-línea**: Cadenas que ocupan varias líneas

---

## El Parser (Análisis Sintáctico)

### ¿Qué es el Parser?

El **Parser** (analizador sintáctico) toma los tokens del lexer y construye una **estructura de árbol** que representa la sintaxis del programa. Este árbol se llama **AST** (Abstract Syntax Tree).

### ¿Por qué es Necesario?

Los tokens por sí solos no son suficientes. Necesitamos entender la **estructura jerárquica** del código:

```
Tokens: [LET, IDENT("x"), ASSIGN, INT(5), PLUS, INT(3), SEMICOLON]

Significado posible 1: let x = (5 + 3);
Significado posible 2: let x = 5; + 3 (ilegal)

Necesitamos un AST para saber cuál es correcto.
```

### ¿Cómo Funciona?

El parser implementa la **gramática** del lenguaje mediante **reglas de producción**:

```
Program = Statement*

Statement = LetStatement | ReturnStatement | IfStatement | ...

LetStatement = "let" Identifier "=" Expression ";"

Expression = Expression InfixOp Expression
           | PrefixOp Expression
           | Literal
           | Identifier
```

### Algoritmo Pratt

Nuestro parser usa el **Pratt Parser** (nombrado por Vaughan Pratt), excelente para manejar **precedencia de operadores**.

#### ¿Qué es Precedencia?

Es el orden en que se evalúan los operadores:

```go
2 + 3 * 4   // ¿Cuál es el resultado?

Si evaluamos de izquierda a derecha: (2 + 3) * 4 = 20
Si respetamos precedencia: 2 + (3 * 4) = 14
```

El Pratt parser resuelve esto automáticamente usando **niveles de precedencia**.

#### Niveles de Precedencia en Nuestro Lenguaje

| Nivel | Operadores | Ejemplo |
|-------|-----------|---------|
| 7 | `()` (llamada función) | `func(5)` |
| 6 | `!`, `-` (prefijo) | `!true`, `-5` |
| 5 | `*`, `/` | `3 * 4` |
| 4 | `+`, `-` | `2 + 3` |
| 3 | `<`, `>`, `<=`, `>=` | `5 < 10` |
| 2 | `==`, `!=` | `5 == 5` |
| 1 | (más baja) | - |

#### ¿Cómo Funciona el Pratt Parser?

```java
private Expression parseExpression(Precedence precedence) {
    // 1. Parsea la expresión de mayor precedencia posible
    Function<Token, Expression> prefixFn = prefixParseFns.get(currentToken.tokenType());
    Expression left = prefixFn.apply(currentToken);
    
    // 2. Mientras haya operadores de MAYOR precedencia, continúa
    while (precedence.getValue() < Precedence.getPrecedence(peekToken.tokenType()).getValue()) {
        advanceTokens();
        
        // 3. Parsea el operador infijo
        Function<Expression, Expression> infixFn = infixParseFns.get(currentToken.tokenType());
        left = infixFn.apply(left);
    }
    
    return left;
}
```

#### Ejemplo Visual

Input: `2 + 3 * 4`

```
Posición del cursor: ↓
                     ↓
Tokens: [INT(2), PLUS, INT(3), MULTIPLICATION, INT(4)]

Paso 1: Parsea 2
  left = IntegerLiteral(2)
  
Paso 2: ¿Siguiente operador? PLUS (precedencia 4)
  ¿Es mayor que LOWEST (1)? SÍ
  → Parsea infijo: left + siguiente_expresión
  
  Ahora necesito parsear "3 * 4"
  → ¡Recursión!
     Parsea 3
     ¿Siguiente operador? MULTIPLICATION (precedencia 5)
     ¿Es mayor que 4? SÍ
     → Parsea infijo: 3 * 4
     Resultado: InfixExpression(3, "*", 4)
  
Paso 3: Composición final
  → InfixExpression(2, "+", InfixExpression(3, "*", 4))
```

**Resultado AST:**
```
     InfixExpression
      /   |   \
   2    "+"  InfixExpression
                /  |   \
             3   "*"   4
```

Esto asegura que `3 * 4` se evalúe antes que `2 +`.

### Métodos del Parser

#### 1. parseLetStatement()

```java
private LetStatement parseLetStatement() {
    Token letToken = currentToken;  // "let"
    advanceTokens();
    
    Identifier name = parseIdentifier();  // "x"
    
    expectPeek(TokenType.ASSIGN);  // "="
    advanceTokens();
    
    Expression value = parseExpression(Precedence.LOWEST);  // "5 + 3"
    
    return new LetStatement(letToken, name, value);
}
```

#### 2. parseIfExpression()

```java
private Expression parseIfExpression() {
    // "if"
    if (!expectPeek(TokenType.LPAREN)) return null;  // "("
    
    advanceTokens();
    Expression condition = parseExpression(Precedence.LOWEST);  // "(...)"
    
    expectPeek(TokenType.RPAREN);  // ")"
    expectPeek(TokenType.LBRACE);  // "{"
    
    BlockStatement consequence = parseBlockStatement();  // { ... }
    
    BlockStatement alternative = null;
    if (peekToken.tokenType() == TokenType.ELSE) {
        advanceTokens();
        expectPeek(TokenType.LBRACE);
        alternative = parseBlockStatement();
    }
    
    return new IfExpression(token, condition, consequence, alternative);
}
```

### Construcción del AST

El parser construye un **Árbol de Sintaxis Abstracta (AST)**:

```
Input: let factorial = function(n) { if (n <= 1) { return 1; } else { return n * factorial(n - 1); } };
```

**AST Generado:**

```
Program
└── LetStatement
    ├── Identifier("factorial")
    └── FunctionLiteral
        ├── Parameters: [Identifier("n")]
        └── Body: BlockStatement
            └── IfExpression
                ├── Condition: InfixExpression(n, "<=", 1)
                ├── Consequence: BlockStatement
                │   └── ReturnStatement(IntegerLiteral(1))
                └── Alternative: BlockStatement
                    └── ReturnStatement(InfixExpression(...))
```

### Manejo de Errores

El parser debe **recover** de errores y continuar parseando:

```java
private void peekError(TokenType expected, TokenType actual) {
    errors.add(String.format(
        "Expected next token to be %s, got %s instead", 
        expected, actual
    ));
}

public List<String> getErrors() {
    return errors;
}
```

Esto permite reportar múltiples errores en un solo pass.

---

## El AST (Abstract Syntax Tree)

### ¿Qué es un AST?

El **AST** (Abstract Syntax Tree) es una **representación en árbol** de la estructura sintáctica del código fuente. Es "abstracta" porque omite detalles sintácticos irrelevantes (como paréntesis).

### ¿Por qué un Árbol?

Porque el código tiene **estructura jerárquica** natural:

```go
let x = 5 + 3 * 2;
```

Representación en árbol:

```
      =
     / \
   x   +
      / \
     5   *
        / \
       3   2
```

### Estructura de Nuestro AST

#### Clases Base

```java
public interface ASTNode {
    String tokenLiteral();  // Nombre del token
    String toString();      // Representación
}

public abstract class Statement implements ASTNode {
    protected Token token;
    // Statements son sentencias completas: let, return, if, etc.
}

public abstract class Expression implements ASTNode {
    protected Token token;
    // Expressions producen valores: 5, x + y, function(), etc.
}
```

#### Nodos del AST

##### Statements (Sentencias)

**LetStatement**: Declaración de variable
```java
public class LetStatement extends Statement {
    private Identifier name;
    private Expression value;
}

// AST: LetStatement(name: "x", value: 5 + 3)
```

**ReturnStatement**: Retorno de función
```java
public class ReturnStatement extends Statement {
    private Expression returnValue;
}

// AST: ReturnStatement(returnValue: 10)
```

**WhileStatement**: Loop while
```java
public class WhileStatement extends Statement {
    private Expression condition;
    private BlockStatement body;
}

// AST: WhileStatement(condition: i < 10, body: { ... })
```

**ForStatement**: Loop for
```java
public class ForStatement extends Statement {
    private Statement initialization;
    private Expression condition;
    private Statement increment;
    private BlockStatement body;
}

// AST: ForStatement(init: let i = 0, cond: i < 5, inc: i = i + 1, body: {...})
```

##### Expressions (Expresiones)

**Identifier**: Nombres de variables
```java
public class Identifier extends Expression {
    private String value;
}

// AST: Identifier(value: "x")
```

**IntegerLiteral**: Literales enteros
```java
public class IntegerLiteral extends Expression {
    private Integer value;
}

// AST: IntegerLiteral(value: 42)
```

**BooleanLiteral**: Literales booleanos
```java
public class BooleanLiteral extends Expression {
    private Boolean value;
}

// AST: BooleanLiteral(value: true)
```

**PrefixExpression**: Operadores prefijo
```java
public class PrefixExpression extends Expression {
    private String operator;  // "!", "-"
    private Expression right;
}

// AST: PrefixExpression(operator: "!", right: true)
```

**InfixExpression**: Operadores infijos
```java
public class InfixExpression extends Expression {
    private Expression left;
    private String operator;  // "+", "-", "==", etc.
    private Expression right;
}

// AST: InfixExpression(left: 5, operator: "+", right: 3)
```

**IfExpression**: Expresiones condicionales
```java
public class IfExpression extends Expression {
    private Expression condition;
    private BlockStatement consequence;
    private BlockStatement alternative;
}

// AST: IfExpression(condition: x > 0, consequence: {...}, alternative: {...})
```

**FunctionLiteral**: Definiciones de función
```java
public class FunctionLiteral extends Expression {
    private List<Identifier> parameters;
    private BlockStatement body;
}

// AST: FunctionLiteral(parameters: [x, y], body: {return x + y;})
```

**CallExpression**: Llamadas a función
```java
public class CallExpression extends Expression {
    private Expression function;
    private List<Expression> arguments;
}

// AST: CallExpression(function: Identifier("suma"), arguments: [5, 3])
```

**BlockStatement**: Bloques de código
```java
public class BlockStatement extends Statement {
    private List<Statement> statements;
}

// AST: BlockStatement(statements: [Statement1, Statement2, ...])
```

### Ventajas del AST

✅ **Independiente de sintaxis concreta**: El AST representa el significado, no los detalles sintácticos
✅ **Fácil de manipular**: Se puede optimizar, transformar
✅ **Intermedio natural**: Entre parser y evaluator
✅ **Representación estándar**: Muchos compiladores usan ASTs

### Visualización del AST

```go
let factorial = function(n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
};
```

**AST Visual:**

```
Program
└── LetStatement
    ├── token: LET
    ├── name: Identifier("factorial")
    └── value: FunctionLiteral
        ├── token: FUNCTION
        ├── parameters: [Identifier("n")]
        └── body: BlockStatement
            └── statements: [
                └── IfExpression
                    ├── token: IF
                    ├── condition: InfixExpression
                    │   ├── left: Identifier("n")
                    │   ├── operator: "<="
                    │   └── right: IntegerLiteral(1)
                    ├── consequence: BlockStatement
                    │   └── statements: [
                    │       └── ReturnStatement
                    │           ├── token: RETURN
                    │           └── returnValue: IntegerLiteral(1)
                    │   ]
                    └── alternative: BlockStatement
                        └── statements: [
                            └── ReturnStatement
                                ├── token: RETURN
                                └── returnValue: InfixExpression
                                    ├── left: Identifier("n")
                                    ├── operator: "*"
                                    └── right: CallExpression
                                        ├── function: Identifier("factorial")
                                        └── arguments: [
                                            └── InfixExpression
                                                ├── left: Identifier("n")
                                                ├── operator: "-"
                                                └── right: IntegerLiteral(1)
                                        ]
                        ]
            ]
```

---

## El Evaluator (Análisis Semántico y Ejecución)

### ¿Qué es el Evaluator?

El **Evaluator** es la fase que **evalúa** el AST y **ejecuta** el código. Es el "cerebro" del intérprete que hace que el programa haga algo útil.

### ¿Por qué es Necesario?

El AST solo representa estructura, pero no **ejecuta** nada. El evaluator:
1. **Camina** por el AST
2. **Evalúa** cada nodo recursivamente
3. **Retorna** valores concretos

### Cómo Funciona

El evaluator usa **dispatch pattern**: según el tipo de nodo, ejecuta una función diferente:

```java
public MObject eval(ASTNode node, Environment env) {
    if (node instanceof Program) {
        return evalProgram((Program) node, env);
    } else if (node instanceof LetStatement) {
        return evalLetStatement((LetStatement) node, env);
    } else if (node instanceof InfixExpression) {
        return evalInfixExpression((InfixExpression) node, env);
    }
    // ... más casos
}
```

### Objetos en Tiempo de Ejecución

El evaluator trabaja con **objetos runtime** (`MObject`):

```java
public interface MObject {
    ObjectType type();
    String inspect();
}
```

**Tipos disponibles:**
- `MInteger`: Valores enteros
- `MBoolean`: Valores booleanos
- `MNull`: Valor nulo
- `MString`: Cadenas de texto
- `MFunction`: Funciones
- `MReturnValue`: Valores de retorno (para propagación)

### Evaluación de Literales

```java
private MObject evalIntegerLiteral(IntegerLiteral node) {
    return new MInteger(node.getValue());
}

private MObject evalBooleanLiteral(BooleanLiteral node) {
    return node.getValue() ? TRUE : FALSE;
}
```

Simple: convierte el literal del AST en un objeto runtime.

### Evaluación de Expresiones Aritméticas

```java
private MObject evalInfixExpression(InfixExpression exp, Environment env) {
    MObject left = eval(exp.getLeft(), env);
    MObject right = eval(exp.getRight(), env);
    String operator = exp.getOperator();
    
    if (left instanceof MInteger && right instanceof MInteger) {
        return evalIntegerInfixExpression(operator, left, right);
    }
    
    return NULL;  // Error
}

private MObject evalIntegerInfixExpression(String op, MObject left, MObject right) {
    int leftVal = ((MInteger) left).getValue();
    int rightVal = ((MInteger) right).getValue();
    
    return switch (op) {
        case "+" -> new MInteger(leftVal + rightVal);
        case "-" -> new MInteger(leftVal - rightVal);
        case "*" -> new MInteger(leftVal * rightVal);
        case "/" -> new MInteger(leftVal / rightVal);
        case "==" -> nativeBoolToBooleanObject(leftVal == rightVal);
        // ... más operadores
    };
}
```

### Evaluación de If/Else

```java
private MObject evalIfExpression(IfExpression exp, Environment env) {
    MObject condition = eval(exp.getCondition(), env);
    
    if (isTruthy(condition)) {
        return eval(exp.getConsequence(), env);
    } else if (exp.getAlternative() != null) {
        return eval(exp.getAlternative(), env);
    } else {
        return NULL;  // Sin else, retorna null
    }
}

private boolean isTruthy(MObject obj) {
    if (obj == NULL) return false;
    if (obj == TRUE) return true;
    if (obj == FALSE) return false;
    return true;  // Cualquier otro valor es truthy
}
```

**Flujo:**
1. Evalúa la condición
2. Convierte a boolean (truthy/falsy)
3. Evalúa la rama apropiada

### Evaluación de Funciones

```java
private MObject evalFunctionLiteral(FunctionLiteral node, Environment env) {
    return new MFunction(node.getParameters(), node.getBody(), env);
}
```

**Nota importante:** La función guarda el `Environment` donde fue definida (closure).

#### Llamada de Funciones

```java
private MObject evalCallExpression(CallExpression node, Environment env) {
    MObject function = eval(node.getFunction(), env);
    
    if (!(function instanceof MFunction)) {
        return NULL;  // Error
    }
    
    MFunction fn = (MFunction) function;
    
    // 1. Evaluar argumentos
    List<MObject> args = evalExpressions(node.getArguments(), env);
    
    // 2. Crear nuevo environment extendido
    Environment extendedEnv = extendFunctionEnv(fn, args);
    
    // 3. Evaluar el cuerpo de la función
    MObject result = eval(fn.getBody(), extendedEnv);
    
    // 4. Desenvolver MReturnValue
    if (result instanceof MReturnValue) {
        return ((MReturnValue) result).getValue();
    }
    
    return result;
}
```

**Proceso detallado:**

1. **Evaluar argumentos**: Los argumentos se evalúan en el environment actual
2. **Extender environment**: Crea un nuevo environment con los parámetros bindeados
3. **Evaluar cuerpo**: Ejecuta el cuerpo de la función en el nuevo environment
4. **Propagar return**: Desenvuelve `MReturnValue` para retornar correctamente

### Evaluación de While

```java
private MObject evalWhileStatement(WhileStatement stmt, Environment env) {
    MObject result = NULL;
    
    while (isTruthy(eval(stmt.getCondition(), env))) {
        result = eval(stmt.getBody(), env);
        
        // Si hay return, propagarlo inmediatamente
        if (result instanceof MReturnValue) {
            return result;
        }
    }
    
    return result;
}
```

**Comportamiento:**
- Evalúa la condición antes de cada iteración
- Si es falsa, salta el cuerpo
- Si es verdadera, ejecuta el cuerpo y repite

### Valores Truthy/Falsy

```java
private boolean isTruthy(MObject obj) {
    if (obj == NULL) return false;
    if (obj == TRUE) return true;
    if (obj == FALSE) return false;
    return true;  // Cualquier otro valor es truthy
}
```

**Tabla truthy/falsy:**

| Valor | Es Truthy? |
|-------|-----------|
| `false` | ❌ |
| `true` | ✅ |
| `null` | ❌ |
| `0` | ✅ |
| `-1` | ✅ |
| `42` | ✅ |

### Recursión

El evaluator soporta recursión natural porque:
1. Cada llamada a función crea un **nuevo environment**
2. Los environment están **encadenados**
3. La función puede acceder a sí misma en el scope externo

**Ejemplo: Factorial**

```go
let factorial = function(n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);  // ← Recursión
    }
};
```

**En cada llamada recursiva:**
```
Environment actual: { "n": 5 }
  ↓
Llamada factorial(4)
  Environment nuevo: { "n": 4, outer: {...} }
    ↓
  Llamada factorial(3)
    Environment nuevo: { "n": 3, outer: {...} }
      ↓
    Llamada factorial(2)
      Environment nuevo: { "n": 2, outer: {...} }
        ...
```

Cada llamada tiene su propio `n`, permitiendo la recursión.

---

## La Tabla de Símbolos (Environment)

### ¿Qué es Environment?

El **Environment** (o tabla de símbolos/symbol table) mantiene un **mapeo** de nombres de variables a sus valores en tiempo de ejecución.

### ¿Por qué es Necesario?

Necesitamos recordar el valor de las variables:

```go
let x = 5;      // x → 5
let y = x + 3;  // x → ?, necesitamos buscar x
```

Sin una tabla de símbolos, no sabríamos que `x = 5`.

### Implementación Básica

```java
public class Environment {
    private Map<String, MObject> store;
    private Environment outer;  // Environment padre (scope exterior)
    
    public Environment() {
        this.store = new HashMap<>();
        this.outer = null;
    }
    
    public MObject get(String name) {
        MObject obj = store.get(name);
        if (obj == null && outer != null) {
            obj = outer.get(name);  // Busca en el scope exterior
        }
        return obj;
    }
    
    public void set(String name, MObject value) {
        store.put(name, value);
    }
}
```

### Scopes Anidados

El environment soporta **scopes anidados** (clases internas, funciones anidadas):

```java
public class Environment {
    private Map<String, MObject> store;
    private Environment outer;  // ← Environment padre
    
    public Environment(Environment outer) {
        this.store = new HashMap<>();
        this.outer = outer;  // ← Encadenamiento
    }
}
```

#### Ejemplo de Scopes

```go
let x = 10;

let f = function(y) {
    let z = 20;
    return x + y + z;  // x viene del scope exterior
};

f(5);
```

**Estados de Environment:**

```
Global Environment:
  { "x": 10, "f": MFunction(...) }
      ↓
Function Environment (f):
  { "y": 5, "z": 20, outer: Global Environment }
```

Cuando `f` accede a `x`:
1. Busca en su propio store → No encuentra
2. Busca en `outer` (Global) → Encuentra `x = 10`
3. Retorna el valor

### Closures

Un **closure** es cuando una función "captura" variables de su scope externo:

```go
let contador = 0;

let incrementar = function() {
    contador = contador + 1;
    return contador;
};

incrementar();  // 1
incrementar();  // 2
```

**¿Cómo funciona?**
- La función `incrementar` captura el environment donde fue definida
- Incluye la variable `contador`
- Cada llamada modifica el mismo `contador`

### Evaluación de LetStatement

```java
private MObject evalLetStatement(LetStatement stmt, Environment env) {
    MObject value = eval(stmt.getValue(), env);
    env.set(stmt.getName().getValue(), value);
    return value;
}
```

**Proceso:**
1. Evalúa el valor (expresión a la derecha de `=`)
2. Asigna el nombre de la variable al valor en el environment
3. Retorna el valor

### Evaluación de Identifier

```java
private MObject evalIdentifier(Identifier node, Environment env) {
    MObject value = env.get(node.getValue());
    return value != null ? value : NULL;
}
```

**Proceso:**
1. Busca el nombre de la variable en el environment
2. Retorna el valor o `NULL` si no existe

---

## El REPL (Read-Eval-Print Loop)

### ¿Qué es un REPL?

**REPL** significa **Read-Eval-Print Loop**:
1. **Read** (Leer): Lee el input del usuario
2. **Eval** (Evaluar): Evalúa el código
3. **Print** (Imprimir): Muestra el resultado
4. **Loop** (Loop): Repite

### ¿Por qué es Útil?

- **Feedback inmediato**: Ve resultados al instante
- **Exploración interactiva**: Prueba ideas rápidamente
- **Debug**: Ejecuta código fragmento por fragmento
- **Educación**: Perfecto para aprender

### Implementación

```java
public static void start() {
    System.out.println("Welcome to Go Interpreter!");
    System.out.println("Type 'exit' or 'end' to quit.");
    
    Scanner scanner = new Scanner(System.in);
    Environment env = new Environment();
    
    while (true) {
        System.out.print(">>> ");
        
        if (!scanner.hasNextLine()) {
            break;
        }
        
        String line = scanner.nextLine();
        
        if (line.trim().equalsIgnoreCase("exit") || line.trim().equalsIgnoreCase("end")) {
            System.out.println("Goodbye!");
            break;
        }
        
        if (line.trim().isEmpty()) {
            continue;
        }
        
        try {
            // 1. LEXER: Tokenizar
            Lexer lexer = new Lexer(line);
            
            // 2. PARSER: Construir AST
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();
            
            if (parser.getErrors().size() > 0) {
                System.out.println("Parser errors:");
                for (String error : parser.getErrors()) {
                    System.out.println("  " + error);
                }
                continue;
            }
            
            // 3. EVALUATOR: Ejecutar
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
```

### Flujo del REPL

```
┌─────────────────────────────────────────────┐
│ Usuario escribe: "let x = 5 + 3;"         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│ LEXER: Tokeniza                             │
│ [LET, IDENT("x"), ASSIGN, ...]             │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│ PARSER: Construye AST                       │
│ LetStatement(name: x, value: 5 + 3)       │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│ EVALUATOR: Ejecuta                          │
│ Environment: { "x": 8 }                     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│ Resultado: 8                                 │
│ >>>                                          │
└─────────────────────────────────────────────┘
```

### Persistencia de Estado

El REPL mantiene el **mismo environment** entre iteraciones:

```go
>>> let x = 5
5
>>> let y = x * 2
10
>>> y
10
```

**Esto funciona porque:**
- `env` se crea una vez antes del loop
- Se reutiliza en cada iteración
- Las variables persisten entre comandos

---

## Flujo Completo: De Código a Ejecución

Veamos el flujo completo de principio a fin con un ejemplo real:

### Input

```go
let factorial = function(n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
};

factorial(5);
```

### Fase 1: Lexer

**Tokens generados:**

```
LET, FUNCTION, LPAREN, IDENT("n"), RPAREN, LBRACE, IF, LPAREN, IDENT("n"), LTE, INT(1), 
RPAREN, LBRACE, RETURN, INT(1), SEMICOLON, RBRACE, ELSE, LBRACE, RETURN, IDENT("n"), 
MULTIPLICATION, IDENT("factorial"), LPAREN, IDENT("n"), MINUS, INT(1), RPAREN, SEMICOLON, 
RBRACE, RBRACE, SEMICOLON, IDENT("factorial"), LPAREN, INT(5), RPAREN, SEMICOLON, EOF
```

### Fase 2: Parser

**AST construido:**

```
Program
├── LetStatement
│   ├── name: Identifier("factorial")
│   └── value: FunctionLiteral
│       ├── parameters: [Identifier("n")]
│       └── body: BlockStatement
│           └── IfExpression
│               ├── condition: InfixExpression(n, "<=", 1)
│               ├── consequence: BlockStatement
│               │   └── ReturnStatement(IntegerLiteral(1))
│               └── alternative: BlockStatement
│                   └── ReturnStatement(InfixExpression(...))
└── ExpressionStatement(CallExpression(Identifier("factorial"), [IntegerLiteral(5)]))
```

### Fase 3: Evaluator - Primera Statement

**Evalúa el LetStatement:**

1. Evalúa el value (FunctionLiteral):
   - Crea `MFunction` con:
     - Parameters: `[Identifier("n")]`
     - Body: `BlockStatement{...}`
     - Env: `Global Environment`

2. Asigna al environment:
   ```java
   env.set("factorial", MFunction{...});
   ```

### Fase 4: Evaluator - Segunda Statement

**Evalúa la llamada `factorial(5)`:**

1. **Evalúa la función:**
   ```java
   MFunction fn = env.get("factorial")
   ```

2. **Evalúa los argumentos:**
   ```java
   List<MObject> args = [MInteger(5)]
   ```

3. **Extiende el environment:**
   ```java
   Environment extendedEnv = new Environment(globalEnv);
   extendedEnv.set("n", MInteger(5))
   // extendedEnv = { "n": 5, outer: globalEnv }
   ```

4. **Evalúa el cuerpo en el nuevo environment:**
   ```
   n = 5, condición: 5 <= 1 → false
   → Evalúa rama else: return n * factorial(n - 1)
   ```

5. **Evalúa la recursión:**
   ```
   Llamada factorial(4)
     n = 4, condición: 4 <= 1 → false
     → return 4 * factorial(3)
   
   Llamada factorial(3)
     n = 3, condición: 3 <= 1 → false
     → return 3 * factorial(2)
   
   Llamada factorial(2)
     n = 2, condición: 2 <= 1 → false
     → return 2 * factorial(1)
   
   Llamada factorial(1)
     n = 1, condición: 1 <= 1 → true
     → return 1
   ```

6. **Desenrolla la recursión:**
   ```
   1
   → 2 * 1 = 2
   → 3 * 2 = 6
   → 4 * 6 = 24
   → 5 * 24 = 120
   ```

7. **Resultado final:**
   ```
   MInteger(120)
   ```

### Output

```
120
```

---

## Comparación con Compiladores Reales

### ¿Qué No Implementamos?

#### 1. Optimizaciones

**Compiladores reales:**
- Dead code elimination
- Constant folding
- Loop unrolling
- Inline functions
- Register allocation

**Nuestro intérprete:**
- Sin optimizaciones (naive evaluation)

#### 2. Tipos Estáticos

**Compiladores reales:**
- Verificación de tipos en tiempo de compilación
- Type checking exhaustivo
- Generics (Java, TypeScript)

**Nuestro intérprete:**
- Dinámico (todo se resuelve en runtime)

#### 3. Generación de Código

**Compiladores reales:**
- GCC compila C → ASM → binario
- javac compila Java → bytecode
- TypeScript compila TS → JavaScript

**Nuestro intérprete:**
- Sin generación de código (interpreta directamente)

#### 4. Manejo de Errores Robusto

**Compiladores reales:**
- Error recovery avanzado
- Sugerencias de errores
- Múltiples mensajes de error en un pass
- Warnings

**Nuestro intérprete:**
- Manejo básico de errores

#### 5. Estructuras de Datos Complejas

**Compiladores reales:**
- Soportan arrays, structs, clases, interfaces
- Herencia, polimorfismo
- Manejo de memoria (malloc, free)

**Nuestro intérprete:**
- Solo tipos básicos (int, bool, function, null)

### ¿Qué Sí Implementamos Correctamente?

✅ **Lexer completo** - Tokenización correcta
✅ **Parser con Pratt** - Precedencia de operadores correcta
✅ **AST bien estructurado** - Separación de concerns
✅ **Environment con scopes** - Variables funcionan correctamente
✅ **Closures** - Funciones capturan variables externas
✅ **Recursión** - Funciones se llaman a sí mismas
✅ **REPL funcional** - Interfaz interactiva

---

## Conclusión

Este intérprete es un **ejemplo pedagógico completo** que demuestra:

1. **Análisis léxico** - Dividir código en tokens
2. **Análisis sintáctico** - Construir AST con precedencia
3. **Análisis semántico** - Ejecutar código correctamente
4. **Scopes y closures** - Manejo de variables
5. **Recursión** - Funciones que se llaman a sí mismas

**Aprendizaje obtenido:**
- Cómo funcionan los compiladores e intérpretes
- Por qué cada fase es necesaria
- Cómo se integran todas las partes
- Fundamento para compiladores más complejos

**Próximos pasos posibles:**
- Agregar arrays y listas
- Implementar sistemas de tipos
- Optimizaciones (constant folding, etc.)
- Generación de bytecode (JVM, LLVM)
- Compilador completo

---

*Última actualización: 27/10/2025*
