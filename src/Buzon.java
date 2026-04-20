
import java.util.LinkedList;
import java.util.Queue;

public class Buzon {
    private Queue<Evento> cola;
    /* 
    -1 es para los buzones de entrada eventos y alerta con cap. ilimitada!
     */
    private int capacidad; 

    public Buzon(int capacidad) {
        this.cola = new LinkedList<>();
        this.capacidad = capacidad;
    }

    public synchronized boolean intentarDepositar(Evento evento) {
        /*
        Para depositar se revisa que el buzon tenga espacio el capacidad > 0 
        revisa si hay espacio ilimitado y con ola.size() >= capacidad se revisa en caso
        de tener espacio limitado si esta se alcanzo
         */
        if (capacidad > 0 && cola.size() >= capacidad) {
            return false;
        }

        cola.add(evento);
        notifyAll();
        return true;
    }

    public synchronized Evento retirar() {
        /*
        No tiene sentido sacar si no hay nada
        */
        while (cola.isEmpty()) {
            try {
                wait();
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Evento eventoProcesado = cola.poll();
        notifyAll();
        return eventoProcesado;
    }

    public synchronized int size() {
        return cola.size();
    }
}