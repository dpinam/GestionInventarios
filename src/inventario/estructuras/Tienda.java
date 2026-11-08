package inventario.estructuras;

/**
 * Tienda en línea: integra el inventario ({@link ArbolProductos}) con la cola
 * de clientes en espera ({@link ColaClientes}).
 *
 * Al agregar un producto al carrito de un cliente, la cantidad comprada se
 * descuenta del inventario (queda reservada para ese cliente).
 */
public class Tienda {
    private String nombre;
    private ArbolProductos inventario;
    private ColaClientes colaClientes;

    /** @param nombre nombre comercial de la tienda. */
    public Tienda(String nombre) {
        this.nombre = nombre;
        this.inventario = new ArbolProductos();
        this.colaClientes = new ColaClientes();
    }

    public String getNombre() { return nombre; }

    public ArbolProductos getInventario() { return inventario; }

    public ColaClientes getColaClientes() { return colaClientes; }

    // ---------------------------------------------------------------------
    //  Inventario
    // ---------------------------------------------------------------------

    /**
     * Agrega un producto al inventario.
     *
     * @return true si se agregó; false si ya existe un producto con ese nombre.
     */
    public boolean agregarProducto(Producto producto) {
        return inventario.insertar(producto);
    }

    /** @return el producto con ese nombre, o null si no existe. */
    public Producto buscarProducto(String nombre) {
        return inventario.buscar(nombre);
    }

    /**
     * Modifica un producto del inventario. Como el nombre es la llave del árbol,
     * si cambia se elimina el nodo y se vuelve a insertar para mantener el orden.
     *
     * @return true si se modificó; false si no existe, los datos son inválidos o
     *         el nuevo nombre ya pertenece a otro producto.
     */
    public boolean modificarProducto(String nombreActual, String nuevoNombre, double nuevoPrecio,
                                     String nuevaCategoria, String nuevaFecha, int nuevaCantidad) {
        Producto producto = inventario.buscar(nombreActual);

        if (producto == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()
                || nuevoPrecio < 0 || nuevaCantidad < 0) {
            return false;
        }

        Producto otro = inventario.buscar(nuevoNombre);
        if (otro != null && otro != producto) {
            return false;
        }

        boolean cambiaNombre = !producto.getNombre().equalsIgnoreCase(nuevoNombre.trim());
        if (cambiaNombre) {
            inventario.eliminar(producto.getNombre());
        }

        producto.setNombre(nuevoNombre.trim());
        producto.setPrecio(nuevoPrecio);
        producto.setCategoria(nuevaCategoria);
        producto.setFechaVencimiento(nuevaFecha);
        producto.setCantidad(nuevaCantidad);

        if (cambiaNombre) {
            inventario.insertar(producto);
        }
        return true;
    }

    /** @return true si se agregó la imagen al producto indicado. */
    public boolean agregarImagenAProducto(String nombreProducto, String rutaImagen) {
        Producto producto = inventario.buscar(nombreProducto);

        if (producto == null || rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return false;
        }
        producto.agregarImagen(rutaImagen.trim());
        return true;
    }

    /** @return true si se eliminó el producto del inventario. */
    public boolean eliminarProducto(String nombre) {
        return inventario.eliminar(nombre);
    }

    // ---------------------------------------------------------------------
    //  Compra y atención de clientes
    // ---------------------------------------------------------------------

    /**
     * Agrega al carrito del cliente una cantidad de un producto del inventario
     * y descuenta esa cantidad de las existencias.
     *
     * @param cliente        cliente que compra.
     * @param nombreProducto producto elegido del inventario.
     * @param cantidad       unidades a comprar (mayor que 0).
     * @return true si se agregó; false si el producto no existe, la cantidad no
     *         es válida o no hay existencias suficientes.
     */
    public boolean agregarAlCarrito(Cliente cliente, String nombreProducto, int cantidad) {
        Producto enInventario = inventario.buscar(nombreProducto);

        if (cliente == null || enInventario == null
                || cantidad <= 0 || cantidad > enInventario.getCantidad()) {
            return false;
        }

        // El carrito guarda una copia con la cantidad comprada; el inventario conserva el original.
        Producto compra = new Producto(enInventario.getNombre(), enInventario.getPrecio(),
                enInventario.getCategoria(), enInventario.getFechaVencimiento(), cantidad);

        cliente.agregarAlCarrito(compra);
        enInventario.setCantidad(enInventario.getCantidad() - cantidad);
        return true;
    }

    /**
     * Inserta un cliente (con su carrito ya lleno) en la cola de prioridad.
     */
    public void registrarCliente(Cliente cliente) {
        colaClientes.encolar(cliente);
    }

    /**
     * Atiende al siguiente cliente según prioridad y orden de llegada.
     *
     * @return el cliente atendido (su factura se obtiene con generarFactura()),
     *         o null si no hay clientes en la cola.
     */
    public Cliente atenderSiguiente() {
        return colaClientes.desencolar();
    }
}
