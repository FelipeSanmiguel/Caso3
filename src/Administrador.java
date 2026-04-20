

import java.util.Random;

public class Administrador extends Thread {

    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private int cantidadClasificadores;
    private Random random;

    public Administrador(Buzon buzonAlertas, Buzon buzonClasificacion, int cantidadClasificadores) {
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
        this.cantidadClasificadores = cantidadClasificadores;
        this.random = new Random();
    }

    @Override
    public void run() {
        boolean terminar = false;

        while (!terminar) {
            Evento evento = buzonAlertas.retirar();

            if (evento.getFin()) {
                for (int i = 0; i < cantidadClasificadores; i++) {
                    depositarSemiActivo(buzonClasificacion, Evento.crearFin());
                }

                System.out.println("Administrador recibio FIN y envio " 
                        + cantidadClasificadores + " FIN a clasificadores.");
                terminar = true;
            } 
            
            else {
                int numero = random.nextInt(21); 

                /*
                multiple de 4 se aprueba si no se descarta
                 */

                if (numero % 4 == 0) {
                    depositarSemiActivo(buzonClasificacion, evento);
                    System.out.println("Administrador aprobo evento " 
                            + evento.getId() + " y lo envio a clasificacion.");
                } else {
                    System.out.println("Administrador descarto evento " 
                            + evento.getId() + " por malicioso.");
                }
            }
        }

        System.out.println("Administrador termino.");
    }

    public void depositarSemiActivo(Buzon buzon, Evento evento) {
        while (!buzon.intentarDepositar(evento)) {
            Thread.yield();
        }
    }
}