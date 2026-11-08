package inventario.estructuras;

/**
 * Nodo de la lista enlazada que sostiene la cola de clientes.
 */
public class NodoCliente {
    private Cliente cliente;
    private NodoCliente siguiente;

    public NodoCliente(Cliente cliente) {
        this.cliente = cliente;
        this.siguiente = null;
    }

    public Cliente getCliente() { return cliente; }

    public NodoCliente getSiguiente() { return siguiente; }
    public void setSiguiente(NodoCliente siguiente) { this.siguiente = siguiente; }
}
