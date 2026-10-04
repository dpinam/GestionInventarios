# Gestión de Inventarios — Primer avance

Universidad CENFOTEC · SOFT-10 Estructuras de Datos · SCV3 · C3-2026

Aplicación de consola en Java para gestionar los productos de una tienda en línea mediante una
`ListaProductos` implementada como **lista enlazada simple**.

## Estructura del proyecto

```
ProyectoInventario/
├── imagenes/                          # Imágenes de los productos (rutas guardadas en listImagenes)
├── docs/                              # Notas de integración de los integrantes
└── src/inventario/
    ├── Main.java                      # menu() de consola y rutina main()          (Integrante 3)
    ├── estructuras/
    │   ├── Producto.java              # Entidad Producto                          (Integrante 1)
    │   ├── Nodo.java                  # Nodo de la lista enlazada                  (Integrante 1)
    │   └── ListaProductos.java        # Lista enlazada simple + reporte de costos  (Integrantes 1 y 2)
    └── pruebas/
        └── PruebasListaProductos.java # Pruebas de los métodos de la lista         (Integrante 2)
```

## Opciones del menú

| # | Opción | Método de `ListaProductos` utilizado |
|---|--------|--------------------------------------|
| 1 | Agregar producto al inicio | `insertarInicio()` |
| 2 | Agregar producto al final | `insertarFinal()` |
| 3 | Ver todos los productos | `mostrarProductos()` |
| 4 | Buscar un producto | `buscarProducto()` |
| 5 | Modificar un producto (Enter conserva el valor actual) | `modificarProducto()` |
| 6 | Agregar imagen a un producto | `agregarImagenAProducto()` |
| 7 | Eliminar un producto (con confirmación) | `eliminarProducto()` |
| 8 | Reporte de costos por producto y total acumulado | `imprimirReporteCostos()` |
| 9 | Cargar productos de ejemplo | `insertarFinal()` |
| 0 | Salir | — |

El menú valida todas las entradas: números, precios no negativos, fechas en formato dd/mm/aaaa,
nombres duplicados y que la imagen exista dentro de la carpeta `imagenes/`.

## Cómo ejecutar

**IntelliJ IDEA / NetBeans / VS Code:** abrir la carpeta del proyecto, marcar `src` como carpeta de
fuentes y ejecutar `inventario.Main`. El directorio de trabajo debe ser la raíz del proyecto para que
el programa encuentre la carpeta `imagenes/`.

**Terminal (JDK 11 o superior):**
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out inventario.Main                        # programa
java -cp out inventario.pruebas.PruebasListaProductos   # pruebas
```

## Distribución del trabajo
- **Integrante 1:** modelo de datos (`Producto`, `Nodo`, `ListaProductos`).
- **Integrante 2:** lógica de negocio, reporte de costos y pruebas.
- **Integrante 3:** menú de consola, clase `Main`, integración y entrega.
