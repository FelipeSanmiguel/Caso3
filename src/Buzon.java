
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
        notify();
        return true;
    }

    public synchronized void depositarPasivo(Evento evento){
        try {
            while (capacidad > 0 && cola.size() >= capacidad) {
                wait();
            }
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        cola.add(evento);
        notify();
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
        notify();
        return eventoProcesado;
    }

    public Evento retirarSemiActivo() {
        while (true) {
            /*
            Como teneog que retornarle un evento a admin al usar semi activo
            no puedo usar la tecnica de returnar un true o falsa por ende pongo el
            yield dentro del metodo de buzon. No obstante para que funcione el metodo 
            de espera semia activa no puedo tener el metodo como syncrhonized asi que el 
            syncrhonized se pone unicamente para la seccion critica del metodo que es revisar si la cola esta vacia
            y en caso de no estarlo sacar el evento. 
            */
            synchronized (this) {
                if (!cola.isEmpty()) {
                    return cola.poll();
                }
            }
            Thread.yield();
        }
    }

    public synchronized int size() {
        return cola.size();
    }

    public Queue<Evento> getCola() {
        return cola;
    }
}