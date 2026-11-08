# Gestión de Inventarios — Segundo avance

Universidad CENFOTEC · SOFT-10 Estructuras de Datos · SCV3 · C3-2026

Aplicación de consola en Java para gestionar una tienda en línea. Este avance construye sobre el
primer avance (`Producto`, `Nodo`, `ListaProductos`) e incorpora el inventario en un **árbol binario
de búsqueda**, la **cola de clientes con prioridad** y la compra mediante un **carrito** por cliente.

## Estructura del proyecto

```
GestionInventarios/
├── imagenes/                          # Imágenes de los productos
├── docs/                              # Notas de integración del primer avance
└── src/inventario/
    ├── Main.java                      # menu() de consola y rutina main()
    ├── estructuras/
    │   ├── Producto.java              # Entidad Producto (primer avance)
    │   ├── Nodo.java                  # Nodo de la lista enlazada (primer avance)
    │   ├── ListaProductos.java        # Lista enlazada: carrito de cada Cliente
    │   ├── NodoArbol.java             # Nodo del árbol binario de búsqueda
    │   ├── ArbolProductos.java        # ABB de productos (llave: nombre) = inventario
    │   ├── Cliente.java               # Cliente: nombre, prioridad (1-3) y carrito
    │   ├── NodoCliente.java           # Nodo de la cola de clientes
    │   ├── ColaClientes.java          # Cola de prioridad de clientes
    │   └── Tienda.java                # Integra ArbolProductos + ColaClientes
    └── pruebas/
        ├── PruebasListaProductos.java # Pruebas del primer avance
        └── PruebasSegundoAvance.java  # Pruebas de árbol, cola, cliente y tienda
```

## Cómo se integran las estructuras de datos

| Clase | Estructura | Rol |
|-------|-----------|-----|
| `ArbolProductos` | Árbol binario de búsqueda (llave = nombre del `Producto`) | Inventario de la `Tienda`. Insertar, buscar y eliminar en O(h); recorrido in-orden para listar alfabéticamente |
| `ColaClientes` | Cola de prioridad sobre lista enlazada | Clientes en espera. Atiende primero la prioridad más alta; en empate, quien esté más cerca del frente |
| `ListaProductos` | Lista enlazada simple | `carrito` de cada `Cliente` |
| `Tienda` | — | Tiene un `ArbolProductos` y una `ColaClientes`; descuenta del inventario lo que cada cliente agrega al carrito |

Prioridades: **1** cliente básico · **2** afiliado a la tienda · **3** premium.

## Opciones del menú

| # | Opción |
|---|--------|
| 1 | Agregar producto al inventario (árbol) |
| 2 | Ver inventario en orden alfabético |
| 3 | Buscar un producto |
| 4 | Modificar un producto (Enter conserva el valor actual) |
| 5 | Agregar imagen a un producto |
| 6 | Eliminar un producto (con confirmación) |
| 7 | Registrar cliente en la cola: pide nombre y prioridad, llena el carrito desde el inventario |
| 8 | Ver la cola de clientes en el orden de atención |
| 9 | Atender al siguiente cliente: imprime la factura de su carrito con el costo total |
| 10 | Cargar productos y clientes de ejemplo |
| 0 | Salir |

El menú valida números, precios no negativos, fechas dd/mm/aaaa, nombres duplicados, existencias
disponibles al comprar y que la imagen exista en `imagenes/`. Un cliente con el carrito vacío no se
agrega a la cola.

## Cómo ejecutar

**IntelliJ IDEA / NetBeans / VS Code:** abrir la carpeta del proyecto, marcar `src` como carpeta de
fuentes y ejecutar `inventario.Main`. El directorio de trabajo debe ser la raíz del proyecto para que
el programa encuentre la carpeta `imagenes/`.

**Terminal:**
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out inventario.Main                              # programa
java -cp out inventario.pruebas.PruebasListaProductos     # pruebas primer avance
java -cp out inventario.pruebas.PruebasSegundoAvance      # pruebas segundo avance
```
