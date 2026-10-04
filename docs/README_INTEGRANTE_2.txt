PARTE DEL INTEGRANTE 2 - LOGICA DE NEGOCIO Y REPORTES

Contenido:
- inventario/estructuras/ListaProductos.java
  Implementa la lógica de gestión de la lista y el reporte de costos.
- inventario/estructuras/Producto.java
  Copia compatible con el proyecto. Solo se agregó el package correcto para
  que compile con Nodo y ListaProductos.
- inventario/pruebas/PruebasListaProductos.java
  Pruebas básicas sin librerías externas.

METODO PRINCIPAL DEL INTEGRANTE 2:
generarReporteCostos()

Este método recorre la lista enlazada y calcula para cada producto:
precio unitario * cantidad

También acumula el costo total de todos los productos.

COMPATIBILIDAD CON MAIN:
El método imprimirReporteCostos() se conserva para que el Main pueda llamar:
inventario.imprimirReporteCostos();

INTEGRACION:
1. Copiar ListaProductos.java sobre la versión anterior.
2. Mantener Producto.java con package inventario.estructuras.
3. Copiar la carpeta pruebas si se desean ejecutar las pruebas.
4. El Integrante 3 puede conservar el Main actual porque el método
   imprimirReporteCostos() sigue disponible.

No se utilizan librerías externas.
