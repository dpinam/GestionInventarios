package inventario.pruebas;

import inventario.estructuras.ListaProductos;
import inventario.estructuras.Producto;

/**
 * Pruebas básicas de integración para la ListaProductos.
 *
 * No utiliza librerías externas.
 */
public class PruebasListaProductos {

    public static void main(String[] args) {
        probarInsercionYBusqueda();
        probarModificacion();
        probarAdicionDeImagen();
        probarEliminacion();
        probarReporteDeCostos();

        System.out.println("\nTodas las pruebas finalizaron correctamente.");
    }

    private static void probarInsercionYBusqueda() {
        ListaProductos lista = new ListaProductos();

        Producto producto1 =
                new Producto("Arroz", 1200.00, "Granos", "N/A", 5);
        Producto producto2 =
                new Producto("Leche", 900.00, "Lácteos", "2026-12-31", 10);

        lista.insertarInicio(producto1);
        lista.insertarFinal(producto2);

        comprobar(lista.buscarProducto("Arroz") != null,
                "Debe encontrar un producto insertado al inicio.");

        comprobar(lista.buscarProducto("Leche") != null,
                "Debe encontrar un producto insertado al final.");
    }

    private static void probarModificacion() {
        ListaProductos lista = new ListaProductos();

        lista.insertarFinal(
                new Producto("Pan", 1000.00, "Panadería", "N/A", 3)
        );

        boolean resultado =
                lista.modificarProducto("Pan", "Pan Integral", 1500.00, 4);

        comprobar(resultado, "La modificación debe realizarse correctamente.");
        comprobar(
                lista.buscarProducto("Pan Integral").getPrecio() == 1500.00,
                "El precio debe actualizarse."
        );
        comprobar(
                lista.buscarProducto("Pan Integral").getCantidad() == 4,
                "La cantidad debe actualizarse."
        );
    }

    private static void probarAdicionDeImagen() {
        ListaProductos lista = new ListaProductos();

        lista.insertarFinal(
                new Producto("Jugo", 800.00, "Bebidas", "2027-01-01", 2)
        );

        boolean resultado =
                lista.agregarImagenAProducto("Jugo", "/imagenes/jugo.png");

        comprobar(resultado, "La imagen debe agregarse correctamente.");
        comprobar(
                lista.buscarProducto("Jugo").getListImagenes().size() == 1,
                "El producto debe contener una imagen."
        );
    }

    private static void probarEliminacion() {
        ListaProductos lista = new ListaProductos();

        lista.insertarFinal(
                new Producto("Galletas", 500.00, "Snacks", "N/A", 2)
        );

        comprobar(
                lista.eliminarProducto("Galletas"),
                "El producto debe eliminarse correctamente."
        );

        comprobar(
                lista.buscarProducto("Galletas") == null,
                "El producto eliminado no debe existir en la lista."
        );
    }

    private static void probarReporteDeCostos() {
        ListaProductos lista = new ListaProductos();

        lista.insertarFinal(
                new Producto("Arroz", 1200.00, "Granos", "N/A", 5)
        );

        lista.insertarFinal(
                new Producto("Leche", 900.00, "Lácteos", "2026-12-31", 10)
        );

        double totalEsperado = (1200.00 * 5) + (900.00 * 10);
        double totalObtenido = lista.calcularCostoTotalInventario();

        comprobar(
                Math.abs(totalObtenido - totalEsperado) < 0.001,
                "El costo total acumulado debe calcularse correctamente."
        );

        String reporte = lista.generarReporteCostos();

        comprobar(
                reporte.contains("Arroz") && reporte.contains("Leche"),
                "El reporte debe incluir todos los productos."
        );

        comprobar(
                reporte.contains("Costo total acumulado del inventario"),
                "El reporte debe mostrar el costo total acumulado."
        );
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError("PRUEBA FALLIDA: " + mensaje);
        }
    }
}
