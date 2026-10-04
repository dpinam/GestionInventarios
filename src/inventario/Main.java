package inventario;

import inventario.estructuras.ListaProductos;
import inventario.estructuras.Producto;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Clase funcional del sistema de gestión de inventarios.
 *
 * Contiene el menú de consola (menu()) que permite al usuario interactuar
 * con la ListaProductos, y la rutina main() que lo ejecuta.
 *
 * Universidad CENFOTEC - SOFT-10 Estructuras de Datos - Primer avance.
 *
 * @author Integrante 3 (menú, Main e integración)
 */
public class Main {

    /** Carpeta dentro del proyecto donde se guardan las imágenes de los productos. */
    private static final String CARPETA_IMAGENES = "imagenes";

    /** Valor que se registra cuando un producto no tiene fecha de vencimiento. */
    private static final String SIN_FECHA = "N/A";

    /** Formato de fecha solicitado al usuario (uuuu = año, con validación estricta). */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Opción del menú que termina el programa. */
    private static final int OPCION_SALIR = 0;

    /** Lista enlazada simple que almacena los productos del inventario. */
    private static final ListaProductos inventario = new ListaProductos();

    /** Lector de la entrada estándar, compartido por todo el programa. */
    private static final Scanner scanner = new Scanner(System.in);

    // =====================================================================
    //  RUTINA PRINCIPAL
    // =====================================================================

    /**
     * Punto de entrada del programa. Muestra la bienvenida, ejecuta el menú
     * y libera el lector de consola al finalizar.
     *
     * @param args argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        mostrarBienvenida();
        menu();
        scanner.close();
    }

    // =====================================================================
    //  MENÚ
    // =====================================================================

    /**
     * Muestra el menú principal de forma repetida hasta que el usuario elige
     * salir. Cada opción se delega a un método específico.
     */
    public static void menu() {
        int opcion;

        do {
            mostrarOpciones();
            opcion = leerEntero("Seleccione una opción: ", 0, 9);
            System.out.println();

            ejecutarOpcion(opcion);

            if (opcion != OPCION_SALIR) {
                pausar();
            }
        } while (opcion != OPCION_SALIR);
    }

    private static void mostrarBienvenida() {
        System.out.println("==================================================");
        System.out.println("       SISTEMA DE GESTIÓN DE INVENTARIOS");
        System.out.println("        Tienda en línea - Primer avance");
        System.out.println("==================================================");
    }

    private static void mostrarOpciones() {
        System.out.println();
        System.out.println("------------------ MENÚ PRINCIPAL ------------------");
        System.out.println("  1. Agregar producto al INICIO de la lista");
        System.out.println("  2. Agregar producto al FINAL de la lista");
        System.out.println("  3. Ver todos los productos");
        System.out.println("  4. Buscar un producto");
        System.out.println("  5. Modificar un producto");
        System.out.println("  6. Agregar imagen a un producto");
        System.out.println("  7. Eliminar un producto");
        System.out.println("  8. Reporte de costos totales");
        System.out.println("  9. Cargar productos de ejemplo");
        System.out.println("  0. Salir");
        System.out.println("----------------------------------------------------");
    }

    /**
     * Ejecuta la acción correspondiente a la opción elegida.
     *
     * @param opcion número de opción seleccionado en el menú.
     */
    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                insertarProducto(true);
                break;
            case 2:
                insertarProducto(false);
                break;
            case 3:
                verProductos();
                break;
            case 4:
                buscarProducto();
                break;
            case 5:
                modificarProducto();
                break;
            case 6:
                agregarImagen();
                break;
            case 7:
                eliminarProducto();
                break;
            case 8:
                mostrarReporteCostos();
                break;
            case 9:
                cargarDatosEjemplo();
                break;
            case OPCION_SALIR:
                System.out.println("Saliendo del sistema... ¡Hasta luego!");
                break;
            default:
                System.out.println("Opción no válida. Intente de nuevo.");
        }
    }

    // =====================================================================
    //  OPCIONES DEL MENÚ
    // =====================================================================

    /**
     * Opciones 1 y 2: solicita los datos de un producto y lo inserta al
     * inicio o al final de la lista.
     *
     * @param alInicio true para insertar al inicio, false para insertar al final.
     */
    private static void insertarProducto(boolean alInicio) {
        System.out.println(">> AGREGAR PRODUCTO AL " + (alInicio ? "INICIO" : "FINAL"));

        String nombre = leerTextoNoVacio("Nombre del producto: ");
        if (inventario.buscarProducto(nombre) != null) {
            System.out.println("Ya existe un producto llamado \"" + nombre
                    + "\". Use la opción 5 para modificarlo.");
            return;
        }

        double precio = leerDecimal("Precio unitario: ");
        String categoria = leerTextoNoVacio("Categoría: ");
        String fecha = leerFecha("Fecha de vencimiento (dd/mm/aaaa) o Enter si no aplica: ");
        int cantidad = leerEntero("Cantidad: ", 0, Integer.MAX_VALUE);

        Producto nuevoProducto = new Producto(nombre, precio, categoria, fecha, cantidad);

        if (alInicio) {
            inventario.insertarInicio(nuevoProducto);
        } else {
            inventario.insertarFinal(nuevoProducto);
        }
        System.out.println("Producto \"" + nombre + "\" agregado al "
                + (alInicio ? "inicio" : "final") + " con éxito.");

        if (leerSiNo("¿Desea agregarle una imagen ahora? (s/n): ")) {
            solicitarImagen(nombre);
        }
    }

    /** Opción 3: muestra todos los productos de la lista. */
    private static void verProductos() {
        System.out.println(">> LISTA DE PRODUCTOS");
        inventario.mostrarProductos();
    }

    /** Opción 4: busca un producto por nombre y muestra su detalle. */
    private static void buscarProducto() {
        System.out.println(">> BUSCAR PRODUCTO");
        if (avisarSiVacio()) {
            return;
        }

        Producto producto = inventario.buscarProducto(leerTextoNoVacio("Nombre del producto: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        mostrarDetalle(producto);
    }

    /**
     * Opción 5: modifica un producto existente. Para cada dato se muestra el
     * valor actual entre corchetes; si el usuario presiona Enter, se conserva.
     */
    private static void modificarProducto() {
        System.out.println(">> MODIFICAR PRODUCTO");
        if (avisarSiVacio()) {
            return;
        }

        Producto producto = inventario.buscarProducto(leerTextoNoVacio("Nombre del producto a modificar: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        mostrarDetalle(producto);
        System.out.println("(Presione Enter para conservar el valor actual)");

        String nombreActual = producto.getNombre();
        String nuevoNombre = leerTextoConDefecto("Nuevo nombre", nombreActual);

        Producto otro = inventario.buscarProducto(nuevoNombre);
        if (otro != null && otro != producto) {
            System.out.println("Ya existe otro producto llamado \"" + nuevoNombre
                    + "\". No se realizaron cambios.");
            return;
        }

        double nuevoPrecio = leerDecimalConDefecto("Nuevo precio", producto.getPrecio());
        String nuevaCategoria = leerTextoConDefecto("Nueva categoría", producto.getCategoria());
        String nuevaFecha = leerFechaConDefecto(producto.getFechaVencimiento());
        int nuevaCantidad = leerEnteroConDefecto("Nueva cantidad", producto.getCantidad());

        // Nombre, precio y cantidad se actualizan mediante el método de la lista.
        if (inventario.modificarProducto(nombreActual, nuevoNombre, nuevoPrecio, nuevaCantidad)) {
            // Categoría y fecha se actualizan sobre el mismo producto de la lista.
            producto.setCategoria(nuevaCategoria);
            producto.setFechaVencimiento(nuevaFecha);
            System.out.println("Producto modificado con éxito.");
        } else {
            System.out.println("No se pudo modificar el producto.");
        }
    }

    /** Opción 6: agrega una imagen a un producto existente. */
    private static void agregarImagen() {
        System.out.println(">> AGREGAR IMAGEN A UN PRODUCTO");
        if (avisarSiVacio()) {
            return;
        }

        Producto producto = inventario.buscarProducto(leerTextoNoVacio("Nombre del producto: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        solicitarImagen(producto.getNombre());
    }

    /** Opción 7: elimina un producto después de pedir confirmación. */
    private static void eliminarProducto() {
        System.out.println(">> ELIMINAR PRODUCTO");
        if (avisarSiVacio()) {
            return;
        }

        String nombre = leerTextoNoVacio("Nombre del producto a eliminar: ");
        if (inventario.buscarProducto(nombre) == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        if (!leerSiNo("¿Seguro que desea eliminar \"" + nombre + "\"? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (inventario.eliminarProducto(nombre)) {
            System.out.println("Producto eliminado con éxito.");
        } else {
            System.out.println("No se pudo eliminar el producto.");
        }
    }

    /** Opción 8: imprime el reporte de costos por producto y el total acumulado. */
    private static void mostrarReporteCostos() {
        inventario.imprimirReporteCostos();
    }

    /** Opción 9: carga productos de prueba para facilitar la demostración. */
    private static void cargarDatosEjemplo() {
        System.out.println(">> CARGAR PRODUCTOS DE EJEMPLO");

        Producto[] ejemplos = {
            new Producto("Leche entera", 950.00, "Lácteos", "31/12/2026", 24),
            new Producto("Arroz 2kg", 2100.00, "Granos", "15/08/2027", 40),
            new Producto("Audífonos", 15500.00, "Electrónica", SIN_FECHA, 5)
        };
        String[] imagenes = {"leche.png", "arroz.png", "audifonos.png"};

        int agregados = 0;
        for (int i = 0; i < ejemplos.length; i++) {
            if (inventario.buscarProducto(ejemplos[i].getNombre()) == null) {
                inventario.insertarFinal(ejemplos[i]);
                inventario.agregarImagenAProducto(ejemplos[i].getNombre(),
                        CARPETA_IMAGENES + "/" + imagenes[i]);
                agregados++;
            }
        }
        System.out.println("Se agregaron " + agregados + " producto(s) de ejemplo.");
    }

    // =====================================================================
    //  MÉTODOS AUXILIARES DE PRESENTACIÓN
    // =====================================================================

    /** Muestra todos los datos de un producto, incluidas sus imágenes. */
    private static void mostrarDetalle(Producto producto) {
        System.out.println("  Nombre:        " + producto.getNombre());
        System.out.printf("  Precio:        $%.2f%n", producto.getPrecio());
        System.out.println("  Categoría:     " + producto.getCategoria());
        System.out.println("  Vencimiento:   " + producto.getFechaVencimiento());
        System.out.println("  Cantidad:      " + producto.getCantidad());
        System.out.printf("  Costo total:   $%.2f%n", producto.getPrecio() * producto.getCantidad());

        if (producto.getListImagenes().isEmpty()) {
            System.out.println("  Imágenes:      ninguna");
        } else {
            System.out.println("  Imágenes:");
            for (String ruta : producto.getListImagenes()) {
                System.out.println("     - " + ruta);
            }
        }
    }

    /**
     * Muestra las imágenes disponibles en la carpeta del proyecto, solicita
     * una y la agrega al producto. Se valida que el archivo exista.
     *
     * @param nombreProducto producto al que se le agregará la imagen.
     */
    private static void solicitarImagen(String nombreProducto) {
        File carpeta = new File(CARPETA_IMAGENES);
        String[] disponibles = carpeta.list();

        if (disponibles == null || disponibles.length == 0) {
            System.out.println("No hay imágenes en la carpeta '" + CARPETA_IMAGENES
                    + "/' del proyecto. Copie la imagen ahí e intente de nuevo.");
            return;
        }

        System.out.println("Imágenes disponibles en '" + CARPETA_IMAGENES + "/':");
        for (String archivo : disponibles) {
            System.out.println("   - " + archivo);
        }

        String archivo = leerTextoNoVacio("Nombre del archivo (ej. leche.png): ");
        String ruta = CARPETA_IMAGENES + "/" + archivo;

        if (!new File(ruta).isFile()) {
            System.out.println("El archivo \"" + archivo + "\" no existe en '"
                    + CARPETA_IMAGENES + "/'. Imagen no agregada.");
            return;
        }

        if (inventario.agregarImagenAProducto(nombreProducto, ruta)) {
            System.out.println("Imagen agregada con éxito: " + ruta);
        } else {
            System.out.println("No se pudo agregar la imagen.");
        }
    }

    /**
     * Si el inventario está vacío, muestra un aviso.
     *
     * @return true si el inventario está vacío.
     */
    private static boolean avisarSiVacio() {
        if (inventario.estaVacia()) {
            System.out.println("El inventario está vacío. Agregue productos primero (opciones 1, 2 o 9).");
            return true;
        }
        return false;
    }

    private static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    // =====================================================================
    //  MÉTODOS AUXILIARES DE LECTURA (validan lo que escribe el usuario)
    // =====================================================================

    private static String leerTextoNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("  Este dato es obligatorio.");
        }
    }

    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // El mensaje de error se muestra abajo.
            }
            if (maximo == Integer.MAX_VALUE) {
                System.out.println("  Ingrese un número entero mayor o igual a " + minimo + ".");
            } else {
                System.out.println("  Ingrese un número entero entre " + minimo + " y " + maximo + ".");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            Double valor = convertirDecimal(scanner.nextLine());
            if (valor != null) {
                return valor;
            }
            System.out.println("  Ingrese un número válido mayor o igual a 0 (ej. 1500 o 1500.50).");
        }
    }

    /**
     * Lee una fecha en formato dd/mm/aaaa. Si el usuario presiona Enter,
     * se registra "N/A" (el producto no vence).
     */
    private static String leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty() || texto.equalsIgnoreCase(SIN_FECHA)) {
                return SIN_FECHA;
            }
            if (esFechaValida(texto)) {
                return texto;
            }
            System.out.println("  Fecha inválida. Use dd/mm/aaaa, por ejemplo 31/12/2026.");
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim().toLowerCase();
            if (texto.equals("s") || texto.equals("si") || texto.equals("sí")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            System.out.println("  Responda 's' o 'n'.");
        }
    }

    // --- Lecturas con valor por defecto (usadas en la opción Modificar) ---

    private static String leerTextoConDefecto(String etiqueta, String actual) {
        System.out.print(etiqueta + " [" + actual + "]: ");
        String texto = scanner.nextLine().trim();
        return texto.isEmpty() ? actual : texto;
    }

    private static double leerDecimalConDefecto(String etiqueta, double actual) {
        while (true) {
            System.out.printf("%s [%.2f]: ", etiqueta, actual);
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return actual;
            }
            Double valor = convertirDecimal(texto);
            if (valor != null) {
                return valor;
            }
            System.out.println("  Ingrese un número válido mayor o igual a 0.");
        }
    }

    private static int leerEnteroConDefecto(String etiqueta, int actual) {
        while (true) {
            System.out.print(etiqueta + " [" + actual + "]: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return actual;
            }
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // El mensaje de error se muestra abajo.
            }
            System.out.println("  Ingrese un número entero mayor o igual a 0.");
        }
    }

    /** Enter conserva la fecha actual; "N/A" indica que el producto no vence. */
    private static String leerFechaConDefecto(String actual) {
        while (true) {
            System.out.print("Nueva fecha de vencimiento (dd/mm/aaaa o N/A) [" + actual + "]: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return actual;
            }
            if (texto.equalsIgnoreCase(SIN_FECHA)) {
                return SIN_FECHA;
            }
            if (esFechaValida(texto)) {
                return texto;
            }
            System.out.println("  Fecha inválida. Use dd/mm/aaaa, por ejemplo 31/12/2026.");
        }
    }

    // --- Conversión y validación ---

    /**
     * Convierte un texto a decimal no negativo. Acepta coma o punto decimal.
     *
     * @return el valor, o null si el texto no es un número válido.
     */
    private static Double convertirDecimal(String texto) {
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            return (valor >= 0) ? valor : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean esFechaValida(String texto) {
        try {
            LocalDate.parse(texto, FORMATO_FECHA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
