# Inicio Rápido - Go Interpreter

Guía rápida para comenzar a usar el intérprete en minutos.

---

## Instalación

### Requisitos

- Java 21 o superior
- Maven 3.8+

### Verificar Instalación

```bash
java -version  # Debe mostrar version 21+
mvn -version   # Debe mostrar Maven 3.8+
```

### Compilar

```bash
cd "/home/emanuel/Universidad/Cuarto/Lenguajes y Compiladores/Final"
mvn clean compile
```

### Ejecutar REPL

```bash
mvn exec:java -Dexec.mainClass="com.interprete.Main"
```

Deberías ver:
```
Welcome to Go Interpreter!
Type 'exit' or 'end' to quit.
>>> 
```

---

## Primeros Pasos

### 1. Operaciones Básicas

```
>>> let x = 5
5
>>> let y = 10
10
>>> x + y
15
```

### 2. Operaciones Aritméticas

```
>>> 2 + 3 * 4
14
>>> (2 + 3) * 4
20
>>> 10 / 2
5
```

### 3. Comparaciones

```
>>> 5 > 3
true
>>> 5 == 5
true
>>> 10 != 5
true
```

### 4. Variables y Operadores

```
>>> let edad = 25
25
>>> edad * 2
50
>>> edad > 18
true
```

### 5. Primeras Funciones

```
>>> let suma = function(x, y) {
...     return x + y;
... }
function(...)
>>> suma(3, 5)
8
```

**Nota**: En el REPL, cuando una expresión tiene varias líneas, verás `...` para continuar.

### 6. Condicionales

```
>>> let mayor = function(a, b) {
...     if (a > b) {
...         return a;
...     } else {
...         return b;
...     }
... }
function(...)
>>> mayor(10, 5)
10
```

### 7. While Loop

```
>>> let i = 0
0
>>> let suma = 0
0
>>> while (i < 5) {
...     suma = suma + i;
...     i = i + 1;
... }
null
>>> suma
10
```

### 8. Factorial Recursivo

```
>>> let factorial = function(n) {
...     if (n <= 1) {
...         return 1;
...     } else {
...         return n * factorial(n - 1);
...     }
... }
function(...)
>>> factorial(5)
120
```

---

## Comandos del REPL

### Salir

```
>>> exit
```

o

```
>>> end
```

### Ejecutar Múltiples Statements

Cada línea debe terminar con `;`:

```
>>> let a = 5; let b = 10; a + b
15
```

### Ver Resultados

El REPL imprime automáticamente el resultado de cada expresión.

```
>>> 5 + 3
8
>>> true
true
>>> 10 > 5
true
```

---

## Ejemplos Completos para Copiar y Pegar

### Calculadora Básica

```
let suma = function(a, b) { return a + b; };
let resta = function(a, b) { return a - b; };
let multiplicacion = function(a, b) { return a * b; };
let division = function(a, b) { return a / b; };

suma(10, 5);      // 15
resta(10, 5);     // 5
multiplicacion(10, 5);  // 50
division(10, 5);  // 2
```

### Verificar si es Par

```
let esPar = function(n) {
    return (n % 2) == 0;
};

esPar(4);   // true
esPar(5);   // false
```

### Sumar Números del 1 al N

```
let sumarHasta = function(n) {
    let suma = 0;
    let i = 1;
    
    while (i <= n) {
        suma = suma + i;
        i = i + 1;
    }
    
    return suma;
};

sumarHasta(10);  // 55
```

### Máximo de Tres Números

```
let max = function(a, b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
};

let maximoDeTres = function(x, y, z) {
    return max(max(x, y), z);
};

maximoDeTres(5, 10, 7);  // 10
```

### Fibonacci

```
let fibonacci = function(n) {
    if (n <= 1) {
        return n;
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
};

fibonacci(10);  // 55
```

---

## Errores Comunes

### 1. Falta punto y coma

```
>>> let x = 5
5
>>> let y = 10  // Error sin ;
```

**Solución**: Usa `;` al final cuando sea necesario.

### 2. Usar variable no declarada

```
>>> z
null
```

Las variables no declaradas retornan `null`.

### 3. Condición no booleana en if

```
>>> if (5) { return 1; }  // Error: 5 no es boolean
```

**Solución**: Usa expresiones booleanas:

```
>>> if (5 != 0) { return 1; }
```

---

## Consejos

1. **Usa paréntesis** para clarificar precedencia:
   ```go
   (2 + 3) * 4
   ```

2. **Funciones pequeñas**: Divide la lógica en funciones pequeñas y reutilizables.

3. **Nombres descriptivos**: Usa nombres que describan el propósito:
   ```go
   let calcularArea = function(largo, ancho) { ... }
   ```

4. **Documenta con comentarios** (cuando los soporte el intérprete).

---

## Siguiente Paso

Lee la [Guía Completa del Lenguaje](LANGUAGE_GUIDE.md) para:
- Gramática formal (EBNF)
- Precedencia de operadores completa
- Ejemplos avanzados
- Detalles de implementación
- Arquitectura del intérprete

---

**¿Problemas?** Verifica que:
- ✅ Java 21 esté instalado
- ✅ Maven 3.8+ esté instalado
- ✅ El proyecto se compiló correctamente (`mvn clean compile`)
