package inventario.estructuras;

/**
 * Nodo del árbol binario de búsqueda de productos.
 *
 * Cada nodo almacena un {@link Producto}; el nombre del producto es la llave
 * que define si el nodo se ubica a la izquierda (menor) o a la derecha (mayor).
 */
public class NodoArbol {
    private Producto producto;
    private NodoArbol izquierdo;
    private NodoArbol derecho;

    public NodoArbol(Producto producto) {
        this.producto = producto;
        this.izquierdo = null;
        this.derecho = null;
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public NodoArbol getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoArbol izquierdo) { this.izquierdo = izquierdo; }

    public NodoArbol getDerecho() { return derecho; }
    public void setDerecho(NodoArbol derecho) { this.derecho = derecho; }
}
