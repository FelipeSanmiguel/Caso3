import java.util.Random;

public class SensoresIoT extends Thread {
    private int idSensor;
    private int cantidadEventos;
    private int ns;
    private Buzon buzonEntrada;
    private Random random;

    public SensoresIoT(int idSensor, int baseEventos, int ns, Buzon buzonEntrada) {
        this.idSensor = idSensor;
        this.cantidadEventos = baseEventos * idSensor;
        this.ns = ns;
        this.buzonEntrada = buzonEntrada;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 1; i <= cantidadEventos; i++) {
            int tipo = random.nextInt(ns) + 1;
            String idEvento = "S" + idSensor + "-E" + i;

            Evento evento = new Evento(idEvento, tipo, false);

            /*
            Espera semi pasiva con yield creo...
            */
            while (!buzonEntrada.intentarDepositar(evento)) {
                Thread.yield();
            }

            System.out.println("Sensor " + idSensor + " produjo " + idEvento + " tipo " + tipo);
        }

        System.out.println("Sensor " + idSensor + " terminó.");
    }
}