package inventario.estructuras;

/**
 * Lista enlazada simple que administra los productos del inventario.
 *
 * @author Integrante 2
 */
public class ListaProductos {
    private Nodo cabeza;

    /**
     * Crea una lista de productos vacía.
     */
    public ListaProductos() {
        this.cabeza = null;
    }

    public void insertarInicio(Producto producto) {
        validarProducto(producto);

        Nodo nuevoNodo = new Nodo(producto);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
    }

    public void insertarFinal(Producto producto) {
        validarProducto(producto);

        Nodo nuevoNodo = new Nodo(producto);

        if (cabeza == null) {
            cabeza = nuevoNodo;
            return;
        }

        Nodo actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }

        actual.setSiguiente(nuevoNodo);
    }

    public Producto buscarProducto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        Nodo actual = cabeza;

        while (actual != null) {
            if (actual.getProducto().getNombre().equalsIgnoreCase(nombre.trim())) {
                return actual.getProducto();
            }
            actual = actual.getSiguiente();
        }

        return null;
    }

    public boolean modificarProducto(
            String nombreActual,
            String nuevoNombre,
            double nuevoPrecio,
            int nuevaCantidad) {

        Producto producto = buscarProducto(nombreActual);

        if (producto == null) {
            return false;
        }

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()
                || nuevoPrecio < 0 || nuevaCantidad < 0) {
            return false;
        }

        producto.setNombre(nuevoNombre.trim());
        producto.setPrecio(nuevoPrecio);
        producto.setCantidad(nuevaCantidad);

        return true;
    }

    public boolean agregarImagenAProducto(String nombreProducto, String rutaImagen) {
        Producto producto = buscarProducto(nombreProducto);

        if (producto == null || rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return false;
        }

        producto.agregarImagen(rutaImagen.trim());
        return true;
    }

    public boolean eliminarProducto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || cabeza == null) {
            return false;
        }

        if (cabeza.getProducto().getNombre().equalsIgnoreCase(nombre.trim())) {
            cabeza = cabeza.getSiguiente();
            return true;
        }

        Nodo actual = cabeza;

        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getProducto().getNombre().equalsIgnoreCase(nombre.trim())) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }

    /**
     * Genera el reporte de costos del inventario.
     *
     * Para cada producto se calcula:
     * costo del producto = precio unitario * cantidad.
     *
     * También se calcula el costo total acumulado de todos los productos.
     *
     * @return reporte de costos listo para imprimir.
     */
    public String generarReporteCostos() {
        StringBuilder reporte = new StringBuilder();

        reporte.append("\n========== REPORTE DE COSTOS ==========\n");

        if (cabeza == null) {
            reporte.append("El inventario está vacío.\n");
            reporte.append("Costo total acumulado del inventario: $0.00\n");
            reporte.append("========================================\n");
            return reporte.toString();
        }

        Nodo actual = cabeza;
        double costoTotalAcumulado = 0.0;

        while (actual != null) {
            Producto producto = actual.getProducto();

            double costoProducto =
                    producto.getPrecio() * producto.getCantidad();

            costoTotalAcumulado += costoProducto;

            reporte.append(String.format(
                    "Producto: %s | Cantidad: %d | Precio unitario: $%.2f | Costo total: $%.2f%n",
                    producto.getNombre(),
                    producto.getCantidad(),
                    producto.getPrecio(),
                    costoProducto
            ));

            actual = actual.getSiguiente();
        }

        reporte.append("----------------------------------------\n");
        reporte.append(String.format(
                "Costo total acumulado del inventario: $%.2f%n",
                costoTotalAcumulado
        ));
        reporte.append("========================================\n");

        return reporte.toString();
    }

    /**
     * Imprime en consola el reporte de costos.
     * Se mantiene este método para que el Main del Integrante 3
     * pueda utilizarlo directamente.
     */
    public void imprimirReporteCostos() {
        System.out.print(generarReporteCostos());
    }

    /**
     * Calcula únicamente el costo total acumulado del inventario.
     *
     * @return suma de precio * cantidad de todos los productos.
     */
    public double calcularCostoTotalInventario() {
        Nodo actual = cabeza;
        double total = 0.0;

        while (actual != null) {
            Producto producto = actual.getProducto();
            total += producto.getPrecio() * producto.getCantidad();
            actual = actual.getSiguiente();
        }

        return total;
    }

    /**
     * [Integración - Integrante 3] Indica si la lista no tiene productos.
     *
     * @return true si la lista está vacía.
     */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     * [Integración - Integrante 3] Recorre la lista e imprime cada producto
     * con todos sus atributos, en el orden en que están enlazados.
     */
    public void mostrarProductos() {
        if (cabeza == null) {
            System.out.println("El inventario está vacío.");
            return;
        }

        Nodo actual = cabeza;
        int posicion = 1;

        while (actual != null) {
            Producto p = actual.getProducto();
            System.out.printf("%d. %s | Categoría: %s | Precio: $%.2f | Cantidad: %d | Vence: %s | Imágenes: %s%n",
                    posicion++, p.getNombre(), p.getCategoria(), p.getPrecio(), p.getCantidad(),
                    p.getFechaVencimiento(),
                    p.getListImagenes().isEmpty() ? "ninguna" : p.getListImagenes());
            actual = actual.getSiguiente();
        }
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
    }
}
