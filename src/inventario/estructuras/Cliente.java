package inventario.estructuras;

/**
 * Cliente de la tienda en línea.
 *
 * Cada cliente tiene una prioridad de atención (1 = básico, 2 = afiliado,
 * 3 = premium) y un carrito, que es una {@link ListaProductos} personal con
 * los productos que desea comprar.
 */
public class Cliente {
    public static final int PRIORIDAD_BASICA = 1;
    public static final int PRIORIDAD_AFILIADO = 2;
    public static final int PRIORIDAD_PREMIUM = 3;

    private String nombre;
    private int prioridad;
    private ListaProductos carrito;

    /**
     * @param nombre    nombre del cliente (no vacío).
     * @param prioridad entero entre 1 y 3.
     * @throws IllegalArgumentException si el nombre es vacío o la prioridad no es válida.
     */
    public Cliente(String nombre, int prioridad) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (prioridad < PRIORIDAD_BASICA || prioridad > PRIORIDAD_PREMIUM) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 3.");
        }
        this.nombre = nombre.trim();
        this.prioridad = prioridad;
        this.carrito = new ListaProductos();
    }

    public String getNombre() { return nombre; }

    public int getPrioridad() { return prioridad; }

    public ListaProductos getCarrito() { return carrito; }

    /** @return descripción textual del tipo de cliente según su prioridad. */
    public String getTipo() {
        switch (prioridad) {
            case PRIORIDAD_PREMIUM:
                return "Premium";
            case PRIORIDAD_AFILIADO:
                return "Afiliado";
            default:
                return "Básico";
        }
    }

    /**
     * Agrega al carrito un producto con la cantidad comprada. Si el producto ya
     * está en el carrito, se suma la cantidad en lugar de duplicarlo.
     *
     * @param producto producto con los datos y la cantidad que se compra.
     */
    public void agregarAlCarrito(Producto producto) {
        Producto existente = carrito.buscarProducto(producto.getNombre());

        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + producto.getCantidad());
        } else {
            carrito.insertarFinal(producto);
        }
    }

    /** @return costo total acumulado del carrito (precio * cantidad de cada producto). */
    public double calcularTotal() {
        return carrito.calcularCostoTotalInventario();
    }

    /**
     * Genera la factura asociada al carrito del cliente, con el detalle por
     * producto y el costo total acumulado.
     *
     * @return factura lista para imprimir.
     */
    public String generarFactura() {
        StringBuilder factura = new StringBuilder();

        factura.append("\n================== FACTURA ==================\n");
        factura.append(String.format("Cliente: %s | Tipo: %s (prioridad %d)%n", nombre, getTipo(), prioridad));
        factura.append("---------------------------------------------\n");

        if (carrito.estaVacia()) {
            factura.append("El carrito está vacío.\n");
        } else {
            factura.append(carrito.generarDetalleLineas());
        }

        factura.append("---------------------------------------------\n");
        factura.append(String.format("TOTAL A PAGAR: $%.2f%n", calcularTotal()));
        factura.append("=============================================\n");

        return factura.toString();
    }
}
