package inventario.estructuras;

/**
 * Inventario de la tienda, implementado como un árbol binario de búsqueda (ABB).
 *
 * La llave de cada nodo es el nombre del {@link Producto}, comparado sin
 * distinguir mayúsculas de minúsculas. No se permiten nombres repetidos.
 *
 * Complejidad: insertar, buscar y eliminar son O(h), donde h es la altura del
 * árbol (O(log n) en promedio).
 */
public class ArbolProductos {
    private NodoArbol raiz;
    private int tamano;

    /** Crea un inventario vacío. */
    public ArbolProductos() {
        this.raiz = null;
        this.tamano = 0;
    }

    /**
     * Inserta un producto en el árbol según su nombre.
     *
     * @param producto producto a insertar.
     * @return true si se insertó; false si ya existía un producto con ese nombre.
     */
    public boolean insertar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        if (raiz == null) {
            raiz = new NodoArbol(producto);
            tamano++;
            return true;
        }

        NodoArbol actual = raiz;
        while (true) {
            int comparacion = comparar(producto.getNombre(), actual.getProducto().getNombre());

            if (comparacion == 0) {
                return false; // nombre duplicado
            }

            if (comparacion < 0) {
                if (actual.getIzquierdo() == null) {
                    actual.setIzquierdo(new NodoArbol(producto));
                    tamano++;
                    return true;
                }
                actual = actual.getIzquierdo();
            } else {
                if (actual.getDerecho() == null) {
                    actual.setDerecho(new NodoArbol(producto));
                    tamano++;
                    return true;
                }
                actual = actual.getDerecho();
            }
        }
    }

    /**
     * Busca un producto por su nombre.
     *
     * @param nombre nombre del producto (no distingue mayúsculas).
     * @return el producto, o null si no existe.
     */
    public Producto buscar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        NodoArbol actual = raiz;
        while (actual != null) {
            int comparacion = comparar(nombre, actual.getProducto().getNombre());

            if (comparacion == 0) {
                return actual.getProducto();
            }
            actual = (comparacion < 0) ? actual.getIzquierdo() : actual.getDerecho();
        }
        return null;
    }

    /**
     * Elimina el producto con el nombre indicado, manejando los tres casos:
     * nodo hoja, nodo con un hijo y nodo con dos hijos (se reemplaza por su
     * sucesor in-orden).
     *
     * @param nombre nombre del producto a eliminar.
     * @return true si se eliminó; false si no existía.
     */
    public boolean eliminar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || buscar(nombre) == null) {
            return false;
        }

        raiz = eliminarRec(raiz, nombre);
        tamano--;
        return true;
    }

    private NodoArbol eliminarRec(NodoArbol nodo, String nombre) {
        if (nodo == null) {
            return null;
        }

        int comparacion = comparar(nombre, nodo.getProducto().getNombre());

        if (comparacion < 0) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), nombre));
        } else if (comparacion > 0) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), nombre));
        } else {
            // Casos con cero o un hijo.
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            // Dos hijos: se toma el menor del subárbol derecho (sucesor in-orden).
            NodoArbol sucesor = nodo.getDerecho();
            while (sucesor.getIzquierdo() != null) {
                sucesor = sucesor.getIzquierdo();
            }
            nodo.setProducto(sucesor.getProducto());
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getProducto().getNombre()));
        }
        return nodo;
    }

    /** @return true si el inventario no tiene productos. */
    public boolean estaVacio() {
        return raiz == null;
    }

    /** @return cantidad de productos distintos almacenados. */
    public int getTamano() {
        return tamano;
    }

    /**
     * Imprime todos los productos ordenados alfabéticamente por nombre
     * (recorrido in-orden).
     */
    public void mostrarProductos() {
        if (raiz == null) {
            System.out.println("El inventario está vacío.");
            return;
        }
        int[] posicion = {1};
        mostrarInOrden(raiz, posicion);
    }

    private void mostrarInOrden(NodoArbol nodo, int[] posicion) {
        if (nodo == null) {
            return;
        }
        mostrarInOrden(nodo.getIzquierdo(), posicion);

        Producto p = nodo.getProducto();
        System.out.printf("%d. %s | Categoría: %s | Precio: $%.2f | Disponibles: %d | Vence: %s | Imágenes: %s%n",
                posicion[0]++, p.getNombre(), p.getCategoria(), p.getPrecio(), p.getCantidad(),
                p.getFechaVencimiento(),
                p.getListImagenes().isEmpty() ? "ninguna" : p.getListImagenes());

        mostrarInOrden(nodo.getDerecho(), posicion);
    }

    /**
     * Devuelve los nombres de los productos en orden alfabético (in-orden).
     *
     * @return arreglo con los nombres, vacío si no hay productos.
     */
    public String[] obtenerNombresOrdenados() {
        String[] nombres = new String[tamano];
        int[] indice = {0};
        llenarNombres(raiz, nombres, indice);
        return nombres;
    }

    private void llenarNombres(NodoArbol nodo, String[] nombres, int[] indice) {
        if (nodo == null) {
            return;
        }
        llenarNombres(nodo.getIzquierdo(), nombres, indice);
        nombres[indice[0]++] = nodo.getProducto().getNombre();
        llenarNombres(nodo.getDerecho(), nombres, indice);
    }

    /** Compara dos nombres ignorando mayúsculas y espacios en los extremos. */
    private int comparar(String a, String b) {
        return a.trim().compareToIgnoreCase(b.trim());
    }
}
