package inventario.pruebas;

import inventario.estructuras.ArbolProductos;
import inventario.estructuras.Cliente;
import inventario.estructuras.ColaClientes;
import inventario.estructuras.Producto;
import inventario.estructuras.Tienda;

/**
 * Pruebas del segundo avance: ArbolProductos, ColaClientes, Cliente y Tienda.
 *
 * No utiliza librerías externas.
 */
public class PruebasSegundoAvance {

    public static void main(String[] args) {
        probarArbolInsercionYBusqueda();
        probarArbolOrdenYEliminacion();
        probarColaPrioridad();
        probarColaEmpatePorLlegada();
        probarClienteYFactura();
        probarTiendaCompra();
        probarTiendaModificarNombre();

        System.out.println("\nTodas las pruebas del segundo avance finalizaron correctamente.");
    }

    private static Producto producto(String nombre, double precio, int cantidad) {
        return new Producto(nombre, precio, "General", "N/A", cantidad);
    }

    private static void probarArbolInsercionYBusqueda() {
        ArbolProductos arbol = new ArbolProductos();

        comprobar(arbol.estaVacio(), "El árbol nuevo debe estar vacío.");
        comprobar(arbol.insertar(producto("Leche", 900, 10)), "Debe insertar el primer producto.");
        comprobar(arbol.insertar(producto("Arroz", 1200, 5)), "Debe insertar un producto menor.");
        comprobar(arbol.insertar(producto("Pan", 1000, 3)), "Debe insertar un producto mayor.");
        comprobar(!arbol.insertar(producto("LECHE", 1, 1)), "No debe permitir nombres duplicados.");

        comprobar(arbol.getTamano() == 3, "El tamaño debe ser 3.");
        comprobar(arbol.buscar("pan") != null, "La búsqueda no distingue mayúsculas.");
        comprobar(arbol.buscar("Queso") == null, "Un producto inexistente devuelve null.");
    }

    private static void probarArbolOrdenYEliminacion() {
        ArbolProductos arbol = new ArbolProductos();
        String[] nombres = {"M", "F", "T", "C", "H", "P", "W"};
        for (String n : nombres) {
            arbol.insertar(producto(n, 1, 1));
        }

        String[] orden = arbol.obtenerNombresOrdenados();
        comprobar(String.join("", orden).equals("CFHMPTW"), "El recorrido in-orden debe ser alfabético.");

        comprobar(arbol.eliminar("C"), "Debe eliminar una hoja.");
        comprobar(arbol.eliminar("T"), "Debe eliminar un nodo con dos hijos.");
        comprobar(arbol.eliminar("F"), "Debe eliminar un nodo con un hijo.");
        comprobar(arbol.eliminar("M"), "Debe eliminar la raíz.");
        comprobar(!arbol.eliminar("Z"), "Eliminar un inexistente devuelve false.");

        comprobar(String.join("", arbol.obtenerNombresOrdenados()).equals("HPW"),
                "Tras eliminar, el orden debe conservarse.");
        comprobar(arbol.getTamano() == 3, "El tamaño debe actualizarse al eliminar.");
    }

    private static void probarColaPrioridad() {
        ColaClientes cola = new ColaClientes();
        comprobar(cola.desencolar() == null, "Desencolar una cola vacía devuelve null.");

        cola.encolar(new Cliente("Basico", 1));
        cola.encolar(new Cliente("Premium", 3));
        cola.encolar(new Cliente("Afiliado", 2));

        comprobar(cola.getTamano() == 3, "La cola debe tener 3 clientes.");
        comprobar(cola.desencolar().getNombre().equals("Premium"), "Primero el de prioridad 3.");
        comprobar(cola.desencolar().getNombre().equals("Afiliado"), "Luego el de prioridad 2.");
        comprobar(cola.desencolar().getNombre().equals("Basico"), "Al final el de prioridad 1.");
        comprobar(cola.estaVacia(), "La cola debe quedar vacía.");
    }

    private static void probarColaEmpatePorLlegada() {
        ColaClientes cola = new ColaClientes();
        cola.encolar(new Cliente("A", 2));
        cola.encolar(new Cliente("B", 2));
        cola.encolar(new Cliente("C", 3));
        cola.encolar(new Cliente("D", 3));
        cola.encolar(new Cliente("E", 1));

        String atencion = "";
        while (!cola.estaVacia()) {
            atencion += cola.desencolar().getNombre();
        }
        comprobar(atencion.equals("CDABE"),
                "En empate gana quien llegó primero. Obtenido: " + atencion);
    }

    private static void probarClienteYFactura() {
        Cliente cliente = new Cliente("Ana", 2);
        cliente.agregarAlCarrito(producto("Arroz", 1000, 2));
        cliente.agregarAlCarrito(producto("Leche", 500, 3));
        cliente.agregarAlCarrito(producto("Arroz", 1000, 1)); // se suma, no se duplica

        comprobar(Math.abs(cliente.calcularTotal() - 4500.0) < 0.001, "El total debe ser 4500.");
        comprobar(cliente.getCarrito().buscarProducto("Arroz").getCantidad() == 3,
                "Un producto repetido debe sumar cantidades.");

        String factura = cliente.generarFactura();
        comprobar(factura.contains("Ana") && factura.contains("Arroz") && factura.contains("Leche"),
                "La factura debe incluir cliente y productos.");
        comprobar(factura.contains("TOTAL A PAGAR"), "La factura debe mostrar el total.");

        boolean fallo = false;
        try {
            new Cliente("X", 4);
        } catch (IllegalArgumentException e) {
            fallo = true;
        }
        comprobar(fallo, "Una prioridad fuera de 1-3 debe rechazarse.");
    }

    private static void probarTiendaCompra() {
        Tienda tienda = new Tienda("Prueba");
        tienda.agregarProducto(producto("Arroz", 1000, 5));

        Cliente cliente = new Cliente("Luis", 3);

        comprobar(!tienda.agregarAlCarrito(cliente, "Arroz", 6), "No se puede comprar más de lo disponible.");
        comprobar(!tienda.agregarAlCarrito(cliente, "Arroz", 0), "La cantidad debe ser positiva.");
        comprobar(!tienda.agregarAlCarrito(cliente, "Queso", 1), "No se puede comprar un producto inexistente.");
        comprobar(tienda.agregarAlCarrito(cliente, "Arroz", 4), "Debe permitir una compra válida.");
        comprobar(tienda.buscarProducto("Arroz").getCantidad() == 1, "Debe descontarse del inventario.");

        tienda.registrarCliente(cliente);
        Cliente atendido = tienda.atenderSiguiente();
        comprobar(atendido == cliente, "Debe atenderse al cliente registrado.");
        comprobar(Math.abs(atendido.calcularTotal() - 4000.0) < 0.001, "El total del cliente debe ser 4000.");
        comprobar(tienda.atenderSiguiente() == null, "Sin clientes, atenderSiguiente devuelve null.");
    }

    private static void probarTiendaModificarNombre() {
        Tienda tienda = new Tienda("Prueba");
        tienda.agregarProducto(producto("Mango", 100, 1));
        tienda.agregarProducto(producto("Zapote", 100, 1));
        tienda.agregarProducto(producto("Banano", 100, 1));

        comprobar(tienda.modificarProducto("Mango", "Aguacate", 150, "Frutas", "N/A", 7),
                "Debe permitir cambiar el nombre.");
        comprobar(tienda.buscarProducto("Mango") == null, "El nombre anterior ya no debe existir.");
        comprobar(tienda.buscarProducto("Aguacate").getCantidad() == 7, "Los datos nuevos deben guardarse.");
        comprobar(String.join(",", tienda.getInventario().obtenerNombresOrdenados()).equals("Aguacate,Banano,Zapote"),
                "El árbol debe reordenarse tras renombrar.");
        comprobar(!tienda.modificarProducto("Banano", "Zapote", 1, "x", "N/A", 1),
                "No debe permitir un nombre ya usado por otro producto.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError("PRUEBA FALLIDA: " + mensaje);
        }
    }
}
