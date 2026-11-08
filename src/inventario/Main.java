package inventario;

import inventario.estructuras.Cliente;
import inventario.estructuras.Producto;
import inventario.estructuras.Tienda;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Clase funcional del sistema de gestión de inventarios.
 *
 * Contiene el menú de consola (menu()) que permite al usuario interactuar con
 * la Tienda (inventario en un árbol binario de búsqueda y cola de clientes por
 * prioridad), y la rutina main() que lo ejecuta.
 *
 * Universidad CENFOTEC - SOFT-10 Estructuras de Datos - Segundo avance.
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

    /** Última opción numerada del menú. */
    private static final int ULTIMA_OPCION = 10;

    /** Tienda que contiene el inventario (ArbolProductos) y la ColaClientes. */
    private static final Tienda tienda = new Tienda("Tienda en línea");

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
            opcion = leerEntero("Seleccione una opción: ", 0, ULTIMA_OPCION);
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
        System.out.println("       Tienda en línea - Segundo avance");
        System.out.println("==================================================");
    }

    private static void mostrarOpciones() {
        System.out.println();
        System.out.println("------------------ MENÚ PRINCIPAL ------------------");
        System.out.println("  --- Inventario ---");
        System.out.println("  1. Agregar producto al inventario");
        System.out.println("  2. Ver inventario (orden alfabético)");
        System.out.println("  3. Buscar un producto");
        System.out.println("  4. Modificar un producto");
        System.out.println("  5. Agregar imagen a un producto");
        System.out.println("  6. Eliminar un producto");
        System.out.println("  --- Clientes ---");
        System.out.println("  7. Registrar cliente en la cola (llenar su carrito)");
        System.out.println("  8. Ver cola de clientes");
        System.out.println("  9. Atender al siguiente cliente (imprimir factura)");
        System.out.println("  --- Otros ---");
        System.out.println(" 10. Cargar datos de ejemplo");
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
                insertarProducto();
                break;
            case 2:
                verInventario();
                break;
            case 3:
                buscarProducto();
                break;
            case 4:
                modificarProducto();
                break;
            case 5:
                agregarImagen();
                break;
            case 6:
                eliminarProducto();
                break;
            case 7:
                registrarCliente();
                break;
            case 8:
                verColaClientes();
                break;
            case 9:
                atenderSiguienteCliente();
                break;
            case 10:
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
    //  OPCIONES DEL MENÚ - INVENTARIO
    // =====================================================================

    /** Opción 1: solicita los datos de un producto y lo inserta en el inventario (árbol). */
    private static void insertarProducto() {
        System.out.println(">> AGREGAR PRODUCTO AL INVENTARIO");

        String nombre = leerTextoNoVacio("Nombre del producto: ");
        if (tienda.buscarProducto(nombre) != null) {
            System.out.println("Ya existe un producto llamado \"" + nombre
                    + "\". Use la opción 4 para modificarlo.");
            return;
        }

        double precio = leerDecimal("Precio unitario: ");
        String categoria = leerTextoNoVacio("Categoría: ");
        String fecha = leerFecha("Fecha de vencimiento (dd/mm/aaaa) o Enter si no aplica: ");
        int cantidad = leerEntero("Cantidad: ", 0, Integer.MAX_VALUE);

        Producto nuevoProducto = new Producto(nombre, precio, categoria, fecha, cantidad);

        if (tienda.agregarProducto(nuevoProducto)) {
            System.out.println("Producto \"" + nombre + "\" agregado al inventario con éxito.");
        } else {
            System.out.println("No se pudo agregar el producto.");
            return;
        }

        if (leerSiNo("¿Desea agregarle una imagen ahora? (s/n): ")) {
            solicitarImagen(nombre);
        }
    }

    /** Opción 2: muestra el inventario ordenado alfabéticamente (recorrido in-orden). */
    private static void verInventario() {
        System.out.println(">> INVENTARIO DE LA TIENDA");
        tienda.getInventario().mostrarProductos();
    }

    /** Opción 3: busca un producto por nombre y muestra su detalle. */
    private static void buscarProducto() {
        System.out.println(">> BUSCAR PRODUCTO");
        if (avisarSiInventarioVacio()) {
            return;
        }

        Producto producto = tienda.buscarProducto(leerTextoNoVacio("Nombre del producto: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        mostrarDetalle(producto);
    }

    /**
     * Opción 4: modifica un producto existente. Para cada dato se muestra el
     * valor actual entre corchetes; si el usuario presiona Enter, se conserva.
     */
    private static void modificarProducto() {
        System.out.println(">> MODIFICAR PRODUCTO");
        if (avisarSiInventarioVacio()) {
            return;
        }

        Producto producto = tienda.buscarProducto(leerTextoNoVacio("Nombre del producto a modificar: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        mostrarDetalle(producto);
        System.out.println("(Presione Enter para conservar el valor actual)");

        String nombreActual = producto.getNombre();
        String nuevoNombre = leerTextoConDefecto("Nuevo nombre", nombreActual);

        Producto otro = tienda.buscarProducto(nuevoNombre);
        if (otro != null && otro != producto) {
            System.out.println("Ya existe otro producto llamado \"" + nuevoNombre
                    + "\". No se realizaron cambios.");
            return;
        }

        double nuevoPrecio = leerDecimalConDefecto("Nuevo precio", producto.getPrecio());
        String nuevaCategoria = leerTextoConDefecto("Nueva categoría", producto.getCategoria());
        String nuevaFecha = leerFechaConDefecto(producto.getFechaVencimiento());
        int nuevaCantidad = leerEnteroConDefecto("Nueva cantidad", producto.getCantidad());

        if (tienda.modificarProducto(nombreActual, nuevoNombre, nuevoPrecio,
                nuevaCategoria, nuevaFecha, nuevaCantidad)) {
            System.out.println("Producto modificado con éxito.");
        } else {
            System.out.println("No se pudo modificar el producto.");
        }
    }

    /** Opción 5: agrega una imagen a un producto existente. */
    private static void agregarImagen() {
        System.out.println(">> AGREGAR IMAGEN A UN PRODUCTO");
        if (avisarSiInventarioVacio()) {
            return;
        }

        Producto producto = tienda.buscarProducto(leerTextoNoVacio("Nombre del producto: "));
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        solicitarImagen(producto.getNombre());
    }

    /** Opción 6: elimina un producto del inventario después de pedir confirmación. */
    private static void eliminarProducto() {
        System.out.println(">> ELIMINAR PRODUCTO");
        if (avisarSiInventarioVacio()) {
            return;
        }

        String nombre = leerTextoNoVacio("Nombre del producto a eliminar: ");
        if (tienda.buscarProducto(nombre) == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        if (!leerSiNo("¿Seguro que desea eliminar \"" + nombre + "\"? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (tienda.eliminarProducto(nombre)) {
            System.out.println("Producto eliminado con éxito.");
        } else {
            System.out.println("No se pudo eliminar el producto.");
        }
    }

    // =====================================================================
    //  OPCIONES DEL MENÚ - CLIENTES
    // =====================================================================

    /**
     * Opción 7: crea un cliente, llena su carrito con productos disponibles en
     * el inventario y lo inserta en la cola de clientes según su prioridad.
     */
    private static void registrarCliente() {
        System.out.println(">> REGISTRAR CLIENTE EN LA COLA");
        if (avisarSiInventarioVacio()) {
            return;
        }

        String nombre = leerTextoNoVacio("Nombre del cliente: ");
        System.out.println("Tipo de cliente:  1 = Básico   2 = Afiliado a la tienda   3 = Premium");
        int prioridad = leerEntero("Prioridad (1-3): ", 1, 3);

        Cliente cliente = new Cliente(nombre, prioridad);

        System.out.println("\n-- Llenado del carrito (productos disponibles) --");
        boolean otro;
        do {
            if (tienda.getInventario().estaVacio()) {
                System.out.println("Ya no quedan productos en el inventario.");
                break;
            }
            tienda.getInventario().mostrarProductos();
            agregarProductoAlCarrito(cliente);
            otro = leerSiNo("¿Desea agregar otro producto al carrito? (s/n): ");
        } while (otro);

        if (cliente.getCarrito().estaVacia()) {
            System.out.println("El carrito quedó vacío: el cliente no fue agregado a la cola.");
            return;
        }

        tienda.registrarCliente(cliente);
        System.out.printf("Cliente \"%s\" (%s) agregado a la cola. Total del carrito: $%.2f%n",
                cliente.getNombre(), cliente.getTipo(), cliente.calcularTotal());
    }

    /** Pide un producto y una cantidad y los agrega al carrito del cliente. */
    private static void agregarProductoAlCarrito(Cliente cliente) {
        String nombreProducto = leerTextoNoVacio("Nombre del producto a comprar: ");
        Producto enInventario = tienda.buscarProducto(nombreProducto);

        if (enInventario == null) {
            System.out.println("Producto no encontrado en el inventario.");
            return;
        }
        if (enInventario.getCantidad() == 0) {
            System.out.println("\"" + enInventario.getNombre() + "\" está agotado.");
            return;
        }

        int cantidad = leerEntero("Cantidad (disponibles: " + enInventario.getCantidad() + "): ",
                1, Integer.MAX_VALUE);

        if (tienda.agregarAlCarrito(cliente, nombreProducto, cantidad)) {
            System.out.println("Agregado al carrito: " + cantidad + " x " + enInventario.getNombre());
        } else {
            System.out.println("No hay existencias suficientes de \"" + enInventario.getNombre()
                    + "\" (disponibles: " + enInventario.getCantidad() + ").");
        }
    }

    /** Opción 8: muestra los clientes en el orden en que serán atendidos. */
    private static void verColaClientes() {
        System.out.println(">> COLA DE CLIENTES (orden de atención)");
        tienda.getColaClientes().mostrarCola();
    }

    /** Opción 9: atiende al siguiente cliente e imprime la factura de su carrito. */
    private static void atenderSiguienteCliente() {
        System.out.println(">> ATENDER AL SIGUIENTE CLIENTE");

        Cliente atendido = tienda.atenderSiguiente();
        if (atendido == null) {
            System.out.println("No hay clientes en la cola.");
            return;
        }

        System.out.println("Atendiendo a: " + atendido.getNombre());
        System.out.print(atendido.generarFactura());
        System.out.println("Clientes restantes en la cola: " + tienda.getColaClientes().getTamano());
    }

    /**
     * Opción 10: carga productos y clientes de prueba para facilitar la
     * demostración. Los clientes solo se cargan junto con los productos.
     */
    private static void cargarDatosEjemplo() {
        System.out.println(">> CARGAR DATOS DE EJEMPLO");

        Producto[] ejemplos = {
            new Producto("Leche entera", 950.00, "Lácteos", "31/12/2026", 24),
            new Producto("Arroz 2kg", 2100.00, "Granos", "15/08/2027", 40),
            new Producto("Audífonos", 15500.00, "Electrónica", SIN_FECHA, 5),
            new Producto("Café molido", 3200.00, "Bebidas", "30/06/2027", 15),
            new Producto("Pan integral", 1800.00, "Panadería", "20/11/2026", 10)
        };
        String[] imagenes = {"leche.png", "arroz.png", "audifonos.png", null, null};

        int agregados = 0;
        for (int i = 0; i < ejemplos.length; i++) {
            if (tienda.agregarProducto(ejemplos[i])) {
                if (imagenes[i] != null) {
                    tienda.agregarImagenAProducto(ejemplos[i].getNombre(),
                            CARPETA_IMAGENES + "/" + imagenes[i]);
                }
                agregados++;
            }
        }
        System.out.println("Se agregaron " + agregados + " producto(s) de ejemplo.");

        if (agregados == ejemplos.length) {
            cargarClienteEjemplo("Ana Rojas", 1, "Leche entera", 2, "Arroz 2kg", 1);
            cargarClienteEjemplo("Luis Mora", 3, "Audífonos", 1, "Café molido", 2);
            cargarClienteEjemplo("María Solano", 2, "Pan integral", 3, "Leche entera", 4);
            cargarClienteEjemplo("Pedro Vargas", 3, "Arroz 2kg", 5, "Café molido", 1);
            System.out.println("Se registraron 4 clientes de ejemplo. Use la opción 8 para ver el orden de atención.");
        }
    }

    /** Crea un cliente de ejemplo con dos productos en el carrito y lo encola. */
    private static void cargarClienteEjemplo(String nombre, int prioridad,
                                             String producto1, int cantidad1,
                                             String producto2, int cantidad2) {
        Cliente cliente = new Cliente(nombre, prioridad);
        tienda.agregarAlCarrito(cliente, producto1, cantidad1);
        tienda.agregarAlCarrito(cliente, producto2, cantidad2);
        tienda.registrarCliente(cliente);
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
        System.out.println("  Disponibles:   " + producto.getCantidad());
        System.out.printf("  Valor en stock: $%.2f%n", producto.getPrecio() * producto.getCantidad());

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

        if (tienda.agregarImagenAProducto(nombreProducto, ruta)) {
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
    private static boolean avisarSiInventarioVacio() {
        if (tienda.getInventario().estaVacio()) {
            System.out.println("El inventario está vacío. Agregue productos primero (opciones 1 o 10).");
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
