import java.util.Random;

public class ServidoresDeDespliegue extends Thread {

    private int idServidor;
    private Buzon buzonConsolidacion;
    private Random random;

    public ServidoresDeDespliegue(int idServidor, Buzon buzonConsolidacion) {
        this.idServidor = idServidor;
        this.buzonConsolidacion = buzonConsolidacion;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            Evento evento = buzonConsolidacion.retirar();

            if (evento.getFin()) {
                System.out.println("Servidor " + idServidor + " recibió FIN y terminó.");
                break;
            }

            procesarEvento(evento);
        }
    }

    public void procesarEvento(Evento evento) {

        /*
        +100 para que en 0 sea 100 y despues con 900 max sea 1000
        */
        int tiempoProcesamiento = random.nextInt(901) + 100; 

        System.out.println("Servidor " + idServidor
                + " procesando evento " + evento.getId()
                + " de tipo " + evento.getTipo()
                + " durante " + tiempoProcesamiento + " ms.");

        try {
            Thread.sleep(tiempoProcesamiento);
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Servidor " + idServidor
                + " terminó de procesar evento " + evento.getId() + ".");
    }
}