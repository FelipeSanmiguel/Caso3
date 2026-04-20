

public class Clasificador extends Thread {

    private int idClasificador;
    private Buzon buzonClasificacion;
    private Buzon[] buzonesServidores;
    private MonitorClasificadores monitorClasificadores;

    public Clasificador(int idClasificador,
                        Buzon buzonClasificacion,
                        Buzon[] buzonesServidores,
                        MonitorClasificadores monitorClasificadores) {
        
        this.idClasificador = idClasificador;
        this.buzonClasificacion = buzonClasificacion;
        this.buzonesServidores = buzonesServidores;
        this.monitorClasificadores = monitorClasificadores;
    }

    @Override
    public void run() {
        int contadorSeguridad = 0;
        /*
        No olvidar! while porque no se cantidad de eventos que viene a cada clasificador
        por magia (problemas) de threads
        */
        while (true && contadorSeguridad < 100000) { 
            Evento evento = buzonClasificacion.retirar();

            /*
            si es fin se mata, como hay un fin por clasificador no importa el retirar anterior
            */
            if (evento.getFin()) {
                boolean ultimo = monitorClasificadores.registrarTerminacion();

                if (ultimo) {
                    enviarFinAServidores();
                    System.out.println("Clasificador " + idClasificador
                            + " fue el último y envio FIN a los servidores.");
                }

                System.out.println("Clasificador " + idClasificador + " termino.");
                break;
            }

            /*
            -1 por los indices del array
            */
            int indiceServidor = evento.getTipo() - 1; 

            depositarSemiActivo(buzonesServidores[indiceServidor], evento);

            System.out.println("Clasificador " + idClasificador
                    + " envio evento " + evento.getId()
                    + " al servidor " + evento.getTipo());
        }
        

        /*
        En caso de error el contador terminara el while sin que muera mi cmputador
        */
        contadorSeguridad++;
        if (contadorSeguridad >= 100000) {
            System.out.println("Error con While en clasificador");
        }
    }

    public void enviarFinAServidores() {
        for (int i = 0; i < buzonesServidores.length; i++) {
            depositarSemiActivo(buzonesServidores[i], Evento.crearFin());
        }
    }

    public void depositarSemiActivo(Buzon buzon, Evento evento) {
        while (!buzon.intentarDepositar(evento)) {
            Thread.yield();
        }
    }
}