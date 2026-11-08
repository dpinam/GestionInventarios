package inventario.estructuras;

/**
 * Cola de prioridad de clientes, implementada con una lista enlazada.
 *
 * Reglas de atención:
 *  - Se atiende primero al cliente con la prioridad más alta (3 > 2 > 1).
 *  - Si hay empate de prioridad, se atiende al que esté más cerca del frente
 *    (es decir, al que llegó primero).
 *
 * Para cumplirlas, {@link #encolar(Cliente)} inserta cada cliente justo después
 * del último cliente que tenga una prioridad mayor o igual a la suya. Así el
 * frente de la lista siempre es el siguiente a atender y {@link #desencolar()}
 * es O(1); la inserción es O(n).
 */
public class ColaClientes {
    private NodoCliente frente;
    private int tamano;

    /** Crea una cola vacía. */
    public ColaClientes() {
        this.frente = null;
        this.tamano = 0;
    }

    /**
     * Inserta un cliente en la cola respetando su prioridad.
     *
     * @param cliente cliente a encolar.
     */
    public void encolar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        NodoCliente nuevo = new NodoCliente(cliente);

        // Entra al frente solo si la cola está vacía o su prioridad supera la del frente.
        if (frente == null || cliente.getPrioridad() > frente.getCliente().getPrioridad()) {
            nuevo.setSiguiente(frente);
            frente = nuevo;
        } else {
            NodoCliente actual = frente;
            while (actual.getSiguiente() != null
                    && actual.getSiguiente().getCliente().getPrioridad() >= cliente.getPrioridad()) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    /**
     * Retira y devuelve al siguiente cliente a atender.
     *
     * @return el cliente atendido, o null si la cola está vacía.
     */
    public Cliente desencolar() {
        if (frente == null) {
            return null;
        }
        Cliente atendido = frente.getCliente();
        frente = frente.getSiguiente();
        tamano--;
        return atendido;
    }

    /**
     * Consulta, sin retirarlo, al siguiente cliente a atender.
     *
     * @return el cliente en el frente, o null si la cola está vacía.
     */
    public Cliente verSiguiente() {
        return (frente == null) ? null : frente.getCliente();
    }

    /** @return true si no hay clientes esperando. */
    public boolean estaVacia() {
        return frente == null;
    }

    /** @return cantidad de clientes en espera. */
    public int getTamano() {
        return tamano;
    }

    /** Imprime los clientes en el orden en que serán atendidos. */
    public void mostrarCola() {
        if (frente == null) {
            System.out.println("No hay clientes en la cola.");
            return;
        }

        NodoCliente actual = frente;
        int turno = 1;
        while (actual != null) {
            Cliente c = actual.getCliente();
            System.out.printf("%d. %s | Tipo: %s (prioridad %d) | Productos en carrito: %s | Total: $%.2f%n",
                    turno++, c.getNombre(), c.getTipo(), c.getPrioridad(),
                    c.getCarrito().estaVacia() ? "ninguno" : "sí",
                    c.calcularTotal());
            actual = actual.getSiguiente();
        }
    }
}
