
import java.util.Random;

public class BrokerYAnalizador extends Thread {

    private Buzon buzonEntrada;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private int totalEventosEsperados;
    private Random random;

    public BrokerYAnalizador(Buzon buzonEntrada, Buzon buzonAlertas, Buzon buzonClasificacion,
                             int totalEventosEsperados) {
        this.buzonEntrada = buzonEntrada;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
        this.totalEventosEsperados = totalEventosEsperados;
        this.random = new Random();
    }


    /*
    Deposita un evento en los buzones de alerta o clasificacion, 
    si el buzon esta lleno se espera de forma semi activa con yield()
     */
    public void depositarSemiActivo(Buzon buzon, Evento evento) {
        while (!buzon.intentarDepositar(evento)) {
            Thread.yield();
        }
    }

    
    @Override
    public void run() {
        for (int i = 0; i < totalEventosEsperados; i++) {

            /*
            en retair hay un wait que evita que avanze si no hay eventos wait() hace que sea espera pasiva
            */
            Evento evento = buzonEntrada.retirar();


            int numero = random.nextInt(201); 

            /*
            Multiplo de 8 es alerta
            */
            if (numero % 8 == 0) {
                depositarSemiActivo(buzonAlertas, evento);
                System.out.println("Broker envio evento " + evento.getId() + " al buzon de alertas.");
            } 
            
            else {
                depositarSemiActivo(buzonClasificacion, evento);
                System.out.println("Broker envio evento " + evento.getId() + " al buzon de clasificacion.");
            }
        }

        depositarSemiActivo(buzonAlertas, Evento.crearFin());
        System.out.println("Broker termino y envio FIN al administrador.");
    }

    
}