# Guía del Lenguaje Go Interpreter

Esta guía explica el lenguaje implementado en este intérprete, basado en sintaxis similar a Go pero simplificado para propósitos educativos.

---

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Tipos de Datos](#tipos-de-datos)
3. [Variables](#variables)
4. [Expresiones y Operadores](#expresiones-y-operadores)
5. [Estructuras de Control](#estructuras-de-control)
6. [Funciones](#funciones)
7. [Recursión](#recursión)
8. [Ejemplos Completos](#ejemplos-completos)
9. [Gramática Formal (EBNF)](#gramática-formal-ebnf)
10. [Precedencia de Operadores](#precedencia-de-operadores)

---

## Introducción

Este lenguaje es un intérprete de tipo "toy language" que implementa características fundamentales de programación:

- **Variables inmutables** (`let`)
- **Funciones de primera clase** (se pueden pasar como valores)
- **Recursión completa**
- **Scopes anidados** (closure support)
- **Estructuras de control** (if, while, for)

### Características NO Soportadas

Este intérprete NO soporta:
- Arrays o listas
- Structs o objetos
- Punteros
- Concurrencia
- Manejo de errores formal (try-catch)
- Operadores lógicos `&&` y `||`
- Imports o módulos

---

## Tipos de Datos

El lenguaje soporta 4 tipos de datos básicos:

### 1. Enteros (`INT`)

Números enteros sin decimales.

```go
let edad = 25;
let temperatura = -10;
let cero = 0;
```

### 2. Booleanos (`BOOLEAN`)

Valores lógicos: `true` o `false`.

```go
let esVerdadero = true;
let esFalso = false;
let comparacion = 5 > 3;  // true
```

### 3. Cadenas de Texto (`STRING`)

Secuencias de caracteres (en procesamiento pero no completamente implementado).

```go
let nombre = "Emanuel";
let saludo = "Hola mundo";
```

**Nota**: El soporte de strings está parcialmente implementado en el lexer pero no en el evaluator final.

### 4. Funciones (`FUNCTION`)

Las funciones son valores de primera clase que se pueden asignar a variables.

```go
let suma = function(x, y) {
    return x + y;
};
```

### 5. Valor Nulo (`NULL`)

Representa la ausencia de valor.

```go
let vacio = null;
```

---

## Variables

### Declaración

Las variables se declaran con la palabra clave `let`:

```go
let x = 5;
let nombre = "Juan";
let activo = true;
```

**Características importantes:**

1. **Inmutabilidad**: Una vez asignado, el valor no puede cambiar.
2. **Scope lexical**: Las variables existen dentro del bloque donde se declaran.
3. **Tipado dinámico**: El tipo se infiere del valor asignado.

### Ejemplos de Scope

```go
let global = 10;

let funcion = function() {
    let local = 5;
    return global + local;  // 15
};
```

**Regla**: Las variables internas pueden "ver" las externas (closure).

---

## Expresiones y Operadores

### Operadores Aritméticos

```go
let suma = 5 + 3;        // 8
let resta = 10 - 4;      // 6
let multiplicacion = 3 * 4;  // 12
let division = 15 / 3;  // 5
```

**Precedencia**: Multiplicación y división se evalúan antes que suma y resta.

```go
let resultado1 = 2 + 3 * 4;  // 14 (no 20)
let resultado2 = (2 + 3) * 4; // 20
```

### Operadores de Comparación

```go
let igualdad = 5 == 5;     // true
let desigualdad = 5 != 5;  // false
let menor = 3 < 5;         // true
let mayor = 10 > 5;        // true
let menorIgual = 5 <= 5;   // true
let mayorIgual = 10 >= 5; // true
```

**Tipos retornados**: Todos los operadores de comparación retornan `boolean`.

### Operadores Lógicos

El lenguaje soporta solo el operador NOT (`!`):

```go
let esVerdadero = true;
let esFalso = !esVerdadero;    // false

let numero = 5;
let mayorQueDiez = !(numero < 10);  // false (porque 5 < 10 es true)
```

**Nota**: `&&` y `||` NO están implementados. Use `if/else` anidados.

### Operador de Negación Aritmética

```go
let positivo = 5;
let negativo = -positivo;  // -5
let dobleNegativo = -(-5); // 5
```

### Expresiones Agrupadas con Paréntesis

Los paréntesis cambian el orden de evaluación:

```go
let sinParentesis = 2 + 3 * 4;    // 14
let conParentesis = (2 + 3) * 4;  // 20
```

---

## Estructuras de Control

### If/Else

Permite ejecutar código condicionalmente.

**Sintaxis:**
```go
if (condicion) {
    // código si condicion es true
} else {
    // código si condicion es false
}
```

**Ejemplos:**

```go
let x = 10;

if (x > 5) {
    return "grande";
} else {
    return "pequeño";
}

// If sin else
if (x == 0) {
    return "cero";
}
```

**Regla**: La condición debe ser una expresión que retorne `boolean`.

### While Loop

Ejecuta un bloque mientras la condición sea verdadera.

**Sintaxis:**
```go
while (condicion) {
    // cuerpo del loop
}
```

**Ejemplo:**

```go
let contador = 0;
let suma = 0;

while (contador < 5) {
    suma = suma + contador;
    contador = contador + 1;
}
// suma = 0+1+2+3+4 = 10
```

**Comportamiento:**
1. Evalúa la condición antes de cada iteración.
2. Si es `true`, ejecuta el bloque.
3. Repite hasta que la condición sea `false`.

### For Loop

Loop con inicialización, condición e incremento.

**Sintaxis:**
```go
for (inicializacion; condicion; incremento) {
    // cuerpo
}
```

**Ejemplo:**

```go
for (let i = 0; i < 5; i = i + 1) {
    // iteración i
}
```

**Equivalente a:**

```go
let i = 0;
while (i < 5) {
    // código aquí
    i = i + 1;
}
```

**Detalles de cada parte:**

- **Inicialización**: Se ejecuta una vez al inicio.
- **Condición**: Se evalúa antes de cada iteración.
- **Incremento**: Se ejecuta al final de cada iteración.

**Todos son opcionales:**

```go
// Solo condición
for (; i < 10; ) { ... }

// Vacío (loop infinito)
for (;;) { ... }
```

---

## Funciones

Las funciones son **valores de primera clase**: se pueden asignar, pasar como argumentos y retornar.

### Definición de Funciones

```go
let suma = function(x, y) {
    return x + y;
};
```

**Componentes:**
- `function`: keyword para definir
- `(x, y)`: parámetros (pueden estar vacíos)
- `{ ... }`: cuerpo de la función
- `return`: retorna un valor

### Llamada de Funciones

```go
let resultado = suma(3, 5);  // 8
```

### Funciones sin Parámetros

```go
let saludo = function() {
    return "¡Hola!";
};

saludo();  // "¡Hola!"
```

### Funciones sin Return

Si no hay `return`, la función retorna `null`:

```go
let imprimir = function(x) {
    // no retorna nada
};

let resultado = imprimir(5);  // null
```

### Funciones como Valores

Las funciones son valores que se pueden asignar:

```go
let original = function(x) { return x * 2; };
let copia = original;

copia(5);  // 10
```

### Higher-Order Functions

Funciones que reciben funciones como argumentos:

```go
let aplicar = function(fn, x) {
    return fn(x);
};

let doble = function(n) { return n * 2; };

aplicar(doble, 5);  // 10
```

---

## Recursión

El lenguaje soporta **recursión completa**, permitiendo funciones que se llaman a sí mismas.

### Factorial

```go
let factorial = function(n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
};

factorial(5);  // 120
```

**Explicación del flujo:**
1. `factorial(5)` → `5 * factorial(4)`
2. `factorial(4)` → `4 * factorial(3)`
3. `factorial(3)` → `3 * factorial(2)`
4. `factorial(2)` → `2 * factorial(1)`
5. `factorial(1)` → `1`
6. Desenrolla: `1 * 2 * 3 * 4 * 5 = 120`

### Fibonacci

```go
let fibonacci = function(n) {
    if (n <= 1) {
        return n;
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
};

fibonacci(10);  // 55
```

### Suma de Números

```go
let sumar = function(n) {
    if (n <= 0) {
        return 0;
    } else {
        return n + sumar(n - 1);
    }
};

sumar(10);  // 55 (1+2+3+...+10)
```

---

## Ejemplos Completos

### Ejemplo 1: Calculadora de Área

```go
let calcularAreaRectangulo = function(largo, ancho) {
    return largo * ancho;
};

let calcularAreaCirculo = function(radio) {
    // aproximación de π
    return radio * radio * 3.14;
};

let rectangulo = calcularAreaRectangulo(5, 3);  // 15
let circulo = calcularAreaCirculo(10);          // 314
```

### Ejemplo 2: Contador con While

```go
let contador = 0;
let suma = 0;

while (contador < 10) {
    if (contador % 2 == 0) {
        suma = suma + contador;
    }
    contador = contador + 1;
}
// suma = 0+2+4+6+8 = 20
```

### Ejemplo 3: Buscar Máximo

```go
let maximo = function(a, b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
};

let maxDeTres = function(x, y, z) {
    return maximo(maximo(x, y), z);
};

maxDeTres(5, 10, 7);  // 10
```

### Ejemplo 4: Pow Recursivo

```go
let pow = function(base, exponente) {
    if (exponente == 0) {
        return 1;
    } else if (exponente < 0) {
        return 0;  // simplificación
    } else {
        return base * pow(base, exponente - 1);
    }
};

pow(2, 8);  // 256
```

### Ejemplo 5: GCD (Máximo Común Divisor)

```go
let gcd = function(a, b) {
    if (b == 0) {
        return a;
    } else {
        return gcd(b, a % b);  // Euclides
    }
};

gcd(48, 18);  // 6
```

---

## Gramática Formal (EBNF)

Esta es la gramática formal del lenguaje:

```
Program = Statement*

Statement = LetStatement | ReturnStatement | ExpressionStatement | WhileStatement | ForStatement

LetStatement = "let" Identifier "=" Expression ";"
ReturnStatement = "return" Expression ";"
ExpressionStatement = Expression ";"
WhileStatement = "while" "(" Expression ")" Block
ForStatement = "for" "(" [Statement] ";" [Expression] ";" [Statement] ")" Block

Block = "{" Statement* "}"

Expression = PrefixExpression | InfixExpression | IfExpression | FunctionLiteral | CallExpression | Literal

PrefixExpression = ("!" | "-") Expression
InfixExpression = Expression InfixOperator Expression
IfExpression = "if" "(" Expression ")" Block ["else" Block]
FunctionLiteral = "function" "(" [Identifier ("," Identifier)*] ")" Block
CallExpression = Expression "(" [Expression ("," Expression)*] ")"

Literal = IntegerLiteral | BooleanLiteral | Identifier

InfixOperator = "+" | "-" | "*" | "/" | "==" | "!=" | "<" | ">" | "<=" | ">="

Identifier = [a-zA-Z_ñÑ][a-zA-Z0-9_ñÑ]*
IntegerLiteral = [0-9]+
BooleanLiteral = "true" | "false"
```

**Nota:** Los identificadores pueden comenzar con letra (incluyendo ñ) o `_`, y contener dígitos.

---

## Precedencia de Operadores

Los operadores se evalúan en este orden (de mayor a menor precedencia):

| Nivel | Operadores | Asociatividad | Ejemplo |
|-------|-----------|---------------|---------|
| 1     | `()`      | Izquierda-Derecha | `(5 + 3) * 2` |
| 2     | `!`, `-`  | Derecha | `!true`, `-5` |
| 3     | `*`, `/`  | Izquierda-Derecha | `10 / 2 * 3` → `15` |
| 4     | `+`, `-`  | Izquierda-Derecha | `5 + 3 - 2` → `6` |
| 5     | `<`, `>`, `<=`, `>=` | Izquierda-Derecha | `5 < 10 > 3` |
| 6     | `==`, `!=` | Izquierda-Derecha | `5 == 5 != false` |

### Ejemplos de Precedencia

```go
let ejemplo1 = 2 + 3 * 4;       // 14 (multiplicación primero)
let ejemplo2 = (2 + 3) * 4;      // 20 (paréntesis primero)
let ejemplo3 = !true && false;   // Error: && no existe
let ejemplo4 = !(5 < 10);       // false
let ejemplo5 = -5 * 2;         // -10 (negativo primero)
```

---

## Errores Comunes

### 1. Variables sin declarar

```go
let y = x + 5;  // Error: x no está definido
```

**Solución**: Declarar variables antes de usarlas.

### 2. Condiciones no booleanas en if/while

```go
if (5) { ... }  // Error: 5 no es boolean
```

**Solución**: Usar expresiones booleanas:

```go
if (5 != 0) { ... }  // correcto
```

### 3. Faltar punto y coma

```go
let x = 5  // Error: se espera ';'
```

**Solución**: Usar `;` al final de cada statement.

### 4. Paréntesis en funciones

```go
let fn = function(x) { return x * 2; };  // Correcto
let fn = function x { return x * 2; };     // Error: falta ()
```

---

## Flujo de Compilación y Ejecución

El intérprete procesa el código en estas etapas:

### 1. Análisis Léxico (Lexer)

Convierte el código fuente en tokens:

```
Input: "let x = 5;"
Output: [LET, IDENT("x"), ASSIGN, INT(5), SEMICOLON, EOF]
```

### 2. Análisis Sintáctico (Parser)

Construye un AST (Abstract Syntax Tree):

```
Program
└── LetStatement
    ├── Identifier("x")
    └── IntegerLiteral(5)
```

### 3. Evaluación (Evaluator)

Evalúa el AST y ejecuta el código:

```
Environment: { "x" → 5 }
Result: 5
```

### 4. REPL (Read-Eval-Print Loop)

Loop interactivo que repite estos pasos hasta que el usuario salga.

---

## Notas de Implementación

### Variables Inmutables

Aunque se usa `=` en la sintaxis, las variables NO pueden ser reasignadas en este intérprete simplificado. Cada `let` crea una nueva variable (o entra en un nuevo scope).

### Scopes y Closures

El Environment maneja scopes mediante encadenamiento:

```go
let x = 10;

let f = function() {
    return x;  // accede al x del scope externo
};

f();  // 10
```

**Implementación interna**: Cada función guarda una referencia al Environment donde fue definida (closure).

### Recursión y Stack

El evaluator soporta recursión ilimitada usando el stack de Java. En la práctica, esto puede causar `StackOverflowError` en casos extremos.

---

## Conclusión

Este lenguaje es un intérprete educativo completo que demuestra:

✅ Parsing de expresiones con precedencia  
✅ Evaluación recursiva del AST  
✅ Scopes anidados y closures  
✅ Funciones de primera clase  
✅ Recursión completa  
✅ Estructuras de control modernas  

**Propósito**: Aprender cómo funcionan los intérpretes y compiladores desde cero.

**Próximos pasos** (no implementados):
- Arrays y listas
- Structs y objetos
- Operadores lógicos `&&` y `||`
- Mejor manejo de errores
- Sistema de tipos más robusto

---

*Última actualización: 27/10/2025*
