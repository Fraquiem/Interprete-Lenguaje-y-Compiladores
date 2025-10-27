# Intérprete de Go en Java

Este es un intérprete completo del lenguaje Go implementado en Java 21.

**¿Nuevo en el lenguaje?** 
- [Inicio Rápido](QUICK_START.md) - Comienza en 5 minutos
- [Guía Completa del Lenguaje](LANGUAGE_GUIDE.md) - Documentación detallada

## Características

- **Lexer completo** - Tokeniza el código fuente en tokens reconocibles
- **Parser con Pratt algorithm** - Construye un AST (Abstract Syntax Tree) con precedencia correcta
- **Evaluator** - Evalúa el AST y ejecuta el programa
- **REPL interactivo** - Consola interactiva para ejecutar código
- **Soporte completo de características**:
  - Operadores aritméticos: `+`, `-`, `*`, `/`
  - Operadores de comparación: `==`, `!=`, `<`, `>`, `<=`, `>=`
  - Operadores lógicos: `!`
  - Variables: `let x = 5;`
  - Funciones: `function(x) { return x + 1; }`
  - Recursión completa
  - Condicionales: `if/else`
  - Bucles: `while` y `for`
  - Booleanos y enteros

## Estructura del Proyecto

```
src/main/java/com/interprete/
├── Main.java              # Punto de entrada
├── token/                 # Token y TokenType
├── lexer/                 # Analizador léxico
├── ast/                   # Nodos del AST
├── parser/                # Analizador sintáctico
├── object/                # Objetos runtime
├── evaluator/             # Evaluador del AST
└── repl/                  # REPL interactivo
```

## Uso

### Compilar con Maven

```bash
mvn clean compile
```

### Ejecutar

```bash
mvn exec:java -Dexec.mainClass="com.interprete.Main"
```

### Ejemplos

```go
// Variables
let x = 5;
let y = 10;

// Operaciones
let suma = x + y * 2;

// Funciones y recursión
let factorial = function(n) {
    if (n == 0) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
};

factorial(5); // 120

// Bucles
let i = 0;
while (i < 5) {
    i = i + 1;
}

// For loops
for (let j = 0; j < 10; j = j + 1) {
    // cuerpo
}
```

## Dependencias

- Java 21
- Maven 3.8+
- JUnit 5.10.1 (para tests)
- Lombok 1.18.30 (reducción de boilerplate)

## Arquitectura

El intérprete sigue la arquitectura clásica:

1. **Lexer** → Convierte código fuente en tokens
2. **Parser** → Convierte tokens en AST usando algoritmo Pratt
3. **Evaluator** → Evalúa el AST y ejecuta el programa
4. **Environment** → Mantiene tabla de símbolos con scopes anidados
5. **REPL** → Interfaz interactiva que integra todos los componentes

El Environment utiliza scopes anidados (encadenamiento) para soportar:
- Variables con scope correcto
- Closures
- Recursión
- Parámetros de funciones

## Testing

Los tests están en `src/test/java/com/interprete/`:
- `LexerTest.java` - Tests del lexer
- `ParserTest.java` - Tests del parser
- `EvaluatorTest.java` - Tests del evaluator

Ejecutar tests:
```bash
mvn test
```

## Notas

Este intérprete es una implementación pedagógica completa que demuestra:
- Parsing de expresiones con precedencia
- Evaluación de expresiones recursivas
- Manejo de scopes y closures
- Recursión en funciones
- Estructuras de control (if/while/for)

Basado en la implementación Python de referencia pero expandido y completado en Java.
