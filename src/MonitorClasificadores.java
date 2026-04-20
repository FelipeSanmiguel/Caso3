public class MonitorClasificadores {
    private int clasificadoresRestantes;

    public MonitorClasificadores(int cantidadInicial) {
        this.clasificadoresRestantes = cantidadInicial;
    }

    /*
    Tengo que saber cual es el ultimo para madnar los fin,
    synched por comportamiento siniestro de los threads
    */
    public synchronized boolean registrarTerminacion() {
        clasificadoresRestantes--;
        return clasificadoresRestantes == 0;
    }
}